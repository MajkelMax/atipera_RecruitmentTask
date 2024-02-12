package com.atipera.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Repo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("fork")
    private Boolean fork;

    @JsonProperty("owner")
    private Owner owner;

}
