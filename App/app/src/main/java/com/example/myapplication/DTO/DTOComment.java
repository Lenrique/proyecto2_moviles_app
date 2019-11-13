package com.example.myapplication.DTO;

public class DTOComment {

    public String userId;
    public String comment;

    public DTOComment(){}

    public DTOComment(String userEmail, String comment){
        this.userId = userEmail;
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public String getComment() {
        return comment;
    }

}
