package com.atipera.recruitmenttask.utils;

import com.atipera.recruitmenttask.Constants;
import com.atipera.recruitmenttask.exception.ApiRateLimitExceededException;
import com.atipera.recruitmenttask.exception.RepositoryNotFoundException;
import com.atipera.recruitmenttask.exception.UserNotFoundException;
import com.atipera.recruitmenttask.model.Branch;
import com.atipera.recruitmenttask.model.ConvertBranchModel;
import com.atipera.recruitmenttask.model.Repo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
public class GithubUserService {

    private final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

    /*
     * Pobiera listę modeli ConvertBranchModel dla podanej nazwy użytkownika.
     * Wykorzystuje wirtualne wątki do asynchronicznego pobierania danych.
     *
     * @param username Nazwa użytkownika GitHub
     * @return CompletableFuture zawierające listę modeli ConvertBranchModel
     */
    public CompletableFuture<CompletableFuture<List<ConvertBranchModel>>> getRepositoryBranches(String username) {

        // Pobieranie listy repozytoriów użytkownika w tle
        CompletableFuture<List<Repo>> repoCompletableFuture = CompletableFuture.supplyAsync(() -> getUserRepositories(username), virtualThreadExecutor);

        // Kontynuacja po pobraniu listy repozytoriów
        return repoCompletableFuture.thenApplyAsync(repoList -> {
            if (repoList.isEmpty()) {
                throw new RepositoryNotFoundException("Użytkownik nie posiada żadnego repozytorium.");
            }

            // Lista CompletableFuture do pobrania gałęzi dla każdego repozytorium
            List<CompletableFuture<ConvertBranchModel>> branchCompletableFutures = new ArrayList<>();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(headers);

            for (Repo repo : repoList) {
                String uri = Constants.getAllBranchesURI(username, repo.name());

                // Pobieranie gałęzi dla repozytorium w tle
                CompletableFuture<ConvertBranchModel> branchCompletableFuture = CompletableFuture.supplyAsync(() -> {
                    ResponseEntity<List<Branch>> result = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                    });
                    return new ConvertBranchModel(
                            repo.name(),
                            repo.owner().ownerLogin(),
                            result.getBody());
                }, virtualThreadExecutor);

                branchCompletableFutures.add(branchCompletableFuture);
            }

            // Oczekiwanie na pobranie wszystkich gałęzi (poprawione)
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(branchCompletableFutures.toArray(new CompletableFuture[0]));

            // Pobieranie i łączenie wyników
            return allFutures.thenApplyAsync(v -> branchCompletableFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()), virtualThreadExecutor);

        }, virtualThreadExecutor);
    }


    private List<Repo> getUserRepositories(String username) {
        /*
        Returns list of Repositories with are not a fork
        */
        List<Repo> repoList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        int page = 1;

        while (true) {
            String uri = new StringBuilder(Constants.GITHUB_USER_REPOSITORY_URI)
                    .append(username)
                    .append("/repos")
                    .append("?page=")
                    .append(page)
                    .toString();

            try {
                ResponseEntity<Repo[]> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Repo[].class);

                if(result.getBody().length > 0) {
                    repoList.addAll(Arrays.stream(result.getBody())
                            .filter(res -> !res.fork()) // add name of the repository to list only if fork is false
                            .toList());
                    page++;
                } else {
                    return repoList;
                }

            } catch (HttpClientErrorException.NotFound ex) {
                throw new UserNotFoundException("User not found.");

            } catch (HttpClientErrorException.Forbidden ex) {
                throw new ApiRateLimitExceededException("API rate limit exceeded");
            }
        }
    }
}
