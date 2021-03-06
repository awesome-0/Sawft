package com.example.samuel.sawft.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Samuel on 21/09/2017.
 */

public class Photo implements Serializable {
    private String caption,date_created,tags,image_url
            ,photo_id, user_id;
    private List<Like> likes;
    private List<Comment> comments;



    public Photo() {

    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Photo(String caption, String date_created, String tags, String image_url, String photo_id, String user_id, List<Like> likes, List<Comment> comments) {
        this.caption = caption;
        this.date_created = date_created;
        this.tags = tags;
        this.image_url = image_url;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.likes = likes;
        this.comments = comments;
    }
    public Photo(String caption, String date_created, String tags, String image_url, String photo_id, String user_id) {
        this.caption = caption;
        this.date_created = date_created;
        this.tags = tags;
        this.image_url = image_url;
        this.photo_id = photo_id;
        this.user_id = user_id;

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "caption='" + caption + '\'' +
                ", date_created='" + date_created + '\'' +
                ", tags='" + tags + '\'' +
                ", image_url='" + image_url + '\'' +
                ", photo_id='" + photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                '}';
    }
}
