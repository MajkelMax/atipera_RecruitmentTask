package com.atipera.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Commit(

        @JsonProperty("sha")
        String sha
) {
}
