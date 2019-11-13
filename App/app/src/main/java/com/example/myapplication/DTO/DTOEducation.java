package com.example.myapplication.DTO;

import java.util.Date;

public class DTOEducation {

    public String institution;
    public Date date;

    DTOEducation(){}

    DTOEducation(String institution, Date date){
        this.institution = institution;
        this.date = date;
    }

    public String getInstitution() {
        return institution;
    }

    public Date getDate() {
        return date;
    }
}
