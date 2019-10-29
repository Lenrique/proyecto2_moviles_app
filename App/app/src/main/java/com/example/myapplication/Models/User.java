package com.example.myapplication.Models;

import java.util.List;

public class User {
    public String name, lastName, city, phone, email, birthDate, profileImage, id;
    public List<Education> educationList;

    private class Education{
        public String institution, date;
        Education(){}
        Education(String institution, String date){
            this.institution = institution;
            this.date = date;
        }
    }

    public User(){}

    public User(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }
}

