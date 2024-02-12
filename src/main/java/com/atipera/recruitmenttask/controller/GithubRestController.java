package com.atipera.recruitmenttask.controller;

import com.atipera.recruitmenttask.model.ConvertBranchModel;
import com.atipera.recruitmenttask.utils.GithubUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GithubRestController {

    private final GithubUserService githubUserService;

    GithubRestController (GithubUserService githubUserService) {
        this.githubUserService = githubUserService;
    }



    @GetMapping("/branch/{user}")
    public ResponseEntity<?> getUserBranches(@PathVariable String user) {

        List<ConvertBranchModel> convertBranchModelList = githubUserService.getRepositoryBranches(user);

        if (!convertBranchModelList.isEmpty()) {
            return ResponseEntity.ok(convertBranchModelList);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something went wrong");
    }


}



