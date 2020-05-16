package com.example.loginapp;

public class UserProfile {

    public String age;
    public String email;
    public String name;

    public UserProfile(String userAge, String userEmail, String userName) {
        this.age = userAge;
        this.email = userEmail;
        this.name = userName;
    }
}
