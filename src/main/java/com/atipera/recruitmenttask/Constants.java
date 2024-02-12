package com.atipera.recruitmenttask;

public class Constants {

    public static final String GITHUB_USER_REPOSITORY_URI = "https://api.github.com/users/";


    public static String getAllBranchesURI(String nickname, String repositoryName) {

        String uri = "https://api.github.com/repos/";

        StringBuilder stringBuilder = new StringBuilder()
                .append(uri)
                .append(nickname).append("/")
                .append(repositoryName).append("/")
                .append("branches");

        return stringBuilder.toString();
    }


}
