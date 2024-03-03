package com.atipera.recruitmenttask.controller;

import com.atipera.recruitmenttask.model.Branch;
import com.atipera.recruitmenttask.model.ConvertBranchModel;
import com.atipera.recruitmenttask.utils.GithubUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GithubRestController {

    private final GithubUserService githubUserService;

    @GetMapping("/branch/{user}")
    public ResponseEntity<List<ConvertBranchModel>> getUserBranches(@PathVariable String user) {
        try {
            CompletableFuture<CompletableFuture<List<ConvertBranchModel>>> outerCompletableFuture = githubUserService.getRepositoryBranches(user);
            CompletableFuture<List<ConvertBranchModel>> innerCompletableFuture = outerCompletableFuture.get(); // Get the inner future
            List<ConvertBranchModel> convertBranchModelList = innerCompletableFuture.get(); // Extract the List
            return ResponseEntity.ok(convertBranchModelList);
        } catch (Exception e) {
            // Handle exceptions gracefully
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}



