package com.example.samuel.sawft.Models;

/**
 * Created by Samuel on 18/09/2017.
 */

public class User {
    private String email,username;
    private long phone_number;

    public User(String email, String username, long phone_number) {
        this.email = email;
        this.username = username;
        this.phone_number = phone_number;
    }

    public User() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone_number=" + phone_number +
                '}';
    }
}
