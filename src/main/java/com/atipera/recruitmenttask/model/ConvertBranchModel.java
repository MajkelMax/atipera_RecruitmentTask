package com.atipera.recruitmenttask.model;

import javax.validation.constraints.NotNull;
import java.util.List;


public record ConvertBranchModel(
        @NotNull
        String repositoryName,
        @NotNull
        String OwnerLogin,
        @NotNull
        List<Branch> branch

) {

}
