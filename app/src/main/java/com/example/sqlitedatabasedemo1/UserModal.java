package com.example.sqlitedatabasedemo1;

import java.io.Serializable;

public class UserModal implements Serializable {

    private String name;
    private String email;
    private String gender;
    private String image;

    UserModal(){}

    //press ALT+Insert
    //Contructor
    //getter and setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
