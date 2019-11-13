package com.example.myapplication.DTO;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DTOPost {

    public List<String> likes;
    public List<DTOComment> comments;
    public Date date;
    public String postId;
    public String image;
    public String videoUrl;
    public String userEmail;
    public String description;

    public DTOPost(){}

    public void getDescription (String description){this.description = description;}

    public DTOPost(String email){
        userEmail = email;
    }

    public void addComment(DTOComment comment){
        comments.add(comment);
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

    public List<DTOComment> getComments() {
        return comments;
    }

    public Date getDate() {
        return date;
    }

    public String getPostId() {
        return postId;
    }

    public String getImage() {
        return image;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
