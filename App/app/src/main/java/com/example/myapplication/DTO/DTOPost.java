package com.example.myapplication.DTO;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DTOPost {

    private List<String> likes;
    private List<DTOComment> DTOComments;
    public Date date;
    public String postId;
    public Uri image;
    public String videoUrl;
    public String userEmail;

    public DTOPost(){}
    public DTOPost(String email){
        likes = new ArrayList<>();
        DTOComments = new ArrayList<>();
        userEmail = email;
    }

    public void addComment(DTOComment DTOComment){
        DTOComments.add(DTOComment);
    }

    public void addLike(String userId){
        likes.add(userId);
    }

    public void removeLike(String userId){
        likes.remove(userId);
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<DTOComment> getDTOComments() {
        return DTOComments;
    }

    public Date getDate() {
        return date;
    }

    public String getPostId() {
        return postId;
    }

    public Uri getImage() {
        return image;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
