package com.calendar.Entities;

import java.util.ArrayList;

public class User {

    private String email;
//    private ArrayList<Authentication> authentications;

    private String token;
    private String refreshToken;

    public User(String email, String token, String refreshToken) {
        this.email = email;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
