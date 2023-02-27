package acgn.jessynow.comment.pojo;

import acgn.jessysnow.gson.pojo.Json;
import lombok.Getter;

public class Comments extends Json {
    public static class Comment{
        @Getter
        private String content;
    }

    @Getter
    private Comment[] comments;
}
