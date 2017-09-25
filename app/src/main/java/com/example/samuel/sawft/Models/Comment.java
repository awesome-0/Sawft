package com.example.samuel.sawft.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Samuel on 25/09/2017.
 */

public class Comment implements Serializable{
    private String user_id,timestamp,comment;
    private List<Like> likes;

    public Comment(String user_id, String timestamp, String comment, List<Like> likes) {
        this.user_id = user_id;
        this.timestamp = timestamp;
        this.comment = comment;
        this.likes = likes;
    }

    public Comment() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "user_id='" + user_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", comment='" + comment + '\'' +
                ", likes=" + likes +
                '}';
    }
}
