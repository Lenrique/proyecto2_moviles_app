package com.example.myapplication.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private List<String> likes;
    private List<Comment> comments;
    public Date date;
    public String postId;

    public Post(){
        likes = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void addLike(String userId){
        likes.add(userId);
    }

    public void removeLike(String userId){
        likes.remove(userId);
    }

}
