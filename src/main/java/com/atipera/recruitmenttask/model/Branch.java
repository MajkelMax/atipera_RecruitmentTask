package com.atipera.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Branch(
        @JsonProperty("name")
        String name,

        @JsonProperty("commit")
        Commit commit
) {

}
