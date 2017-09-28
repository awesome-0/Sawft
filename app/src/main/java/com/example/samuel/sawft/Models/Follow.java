package com.example.samuel.sawft.Models;

/**
 * Created by Samuel on 28/09/2017.
 */

public class Follow {
    private String user_id;

    public Follow(String user_id) {
        this.user_id = user_id;
    }

    public Follow() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
