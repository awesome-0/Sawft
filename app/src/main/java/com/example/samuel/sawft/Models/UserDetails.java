package com.example.samuel.sawft.Models;

/**
 * Created by Samuel on 18/09/2017.
 */

public class UserDetails {
    private String description,display_name,
    username,website,
    profile_photo;
    private long followers,following,posts;

    public UserDetails(String description, String display_name, String username
            , String website, String profile_photo, long followers, long following, long posts) {
        this.description = description;
        this.display_name = display_name;
        this.username = username;
        this.website = website;
        this.profile_photo = profile_photo;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
    }

    public UserDetails() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", username='" + username + '\'' +
                ", website='" + website + '\'' +
                ", profile_photo='" + profile_photo + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", posts=" + posts +
                '}';
    }
}
