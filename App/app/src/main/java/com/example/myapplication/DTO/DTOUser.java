package com.example.myapplication.DTO;

import android.net.Uri;

import java.util.List;

public class DTOUser {

    public DTOUserInfo DTOUserInfo;
    public List<DTOEducation> DTOEducationList;
    public List<Uri> photos;
    public List<String> friends;
    public List<String> request;
    public List<DTOPost> DTOPosts;

    public DTOUser(){}


//#region Getters
    public DTOUserInfo getDTOUserInfo() {
        return DTOUserInfo;
    }

    public List<DTOEducation> getDTOEducationList() {
        return DTOEducationList;
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

    public List<DTOPost> getDTOPosts() {
        return DTOPosts;
    }
//#endregion
}

