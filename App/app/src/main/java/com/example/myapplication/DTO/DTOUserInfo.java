package com.example.myapplication.DTO;

import android.net.Uri;

import java.util.List;

public class DTOUserInfo {

    public String name;
    public String lastName;
    public String profilePhoto;
    public String bornDate;
    public String phone;
    public String city;
    public String email;
    public List<DTOEducation> education;

    public DTOUserInfo(){}

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getBornDate() {
        return bornDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public List<DTOEducation> getDTOEducation() {
        return education;
    }
}
