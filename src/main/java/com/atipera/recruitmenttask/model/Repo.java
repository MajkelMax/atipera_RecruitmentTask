package com.atipera.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Repo(
        @JsonProperty("name")
        String name,
        @JsonProperty("fork")
        Boolean fork,
        @JsonProperty("owner")
        Owner owner
) {



}
