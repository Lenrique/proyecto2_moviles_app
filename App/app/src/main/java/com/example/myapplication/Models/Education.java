package com.example.myapplication.Models;

import java.util.Date;

public class Education {

    public String institution;
    public Date date;

    Education(){}

    Education(String institution, Date date){
        this.institution = institution;
        this.date = date;
    }
}
