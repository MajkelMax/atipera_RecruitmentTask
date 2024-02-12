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

@Component
@Getter
@Setter
public class GithubUserService {


    public List<ConvertBranchModel> getRepositoryBranches(String username) {

        /*
        That method utilizes a method at the bottom of the class
         to retrieve all names of repositories for creating new
         requests to fetch all branches for each repository.
        */

        List<Repo> repoList = getUserRepositories(username);

        if (repoList == null){
            throw new RepositoryNotFoundException("User does not have anny repository.");
        }

        List<ConvertBranchModel> convertBranchModelList = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        for (Repo repo : repoList) {

            String uri = Constants.getAllBranchesURI(username, repo.getName());
            ResponseEntity<List<Branch>> result = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<>(){});

            ConvertBranchModel convertBranchModel = new ConvertBranchModel();
            convertBranchModel.setBranch(result.getBody());
            convertBranchModel.setRepositoryName(repo.getName());
            convertBranchModel.setOwnerLogin(repo.getOwner().getOwnerLogin());

            convertBranchModelList.add(convertBranchModel);

        }
        return convertBranchModelList;
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
                            .filter(res -> !res.getFork()) // add name of the repository to list only if fork is false
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
