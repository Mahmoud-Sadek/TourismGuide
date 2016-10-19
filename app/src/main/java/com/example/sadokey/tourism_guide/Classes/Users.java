package com.example.sadokey.tourism_guide.Classes;

/**
 * Created by Ahmed AboMazin on 9/27/2016.
 */
public class Users
{
    private String username;
    private String image;
    private String job;

    public Users(String username, String image, String job) {
        this.username = username;
        this.image = image;
        this.job = job;
    }

    public Users() {
    }

    public Users(String username, String image) {
        this.username = username;
        this.image = image;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
