package com.atipera.recruitmenttask.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Owner {

    @JsonProperty("login")
    private String ownerLogin;

}
