package com.atipera.recruitmenttask.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ConvertBranchModel {

    @NotNull
    String repositoryName;
    @NotNull
    String OwnerLogin;
    @NotNull
    List<Branch> branch;

}
