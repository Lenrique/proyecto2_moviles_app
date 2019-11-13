package com.example.myapplication.DTO;

import android.net.Uri;

import java.util.List;

public class DTOUser {

    public DTOUserInfo userInfo;
    public List<DTOEducation> education;
    public List<Uri> photos;
    public List<String> friends;
    public List<String> request;
    public List<String> posts;

    public DTOUser(){}


//#region Getters
    public DTOUserInfo getUserInfo() {
        return userInfo;
    }

    public List<DTOEducation> getEducation() {
        return education;
    }

    public List<Uri> getPhotos() {
        return photos;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getRequest() {
        return request;
    }

    public List<String> getPosts() {
        return posts;
    }
//#endregion
}

