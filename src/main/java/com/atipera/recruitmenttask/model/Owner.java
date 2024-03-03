package com.atipera.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Owner (
        @JsonProperty("login")
        String ownerLogin
) {
}
