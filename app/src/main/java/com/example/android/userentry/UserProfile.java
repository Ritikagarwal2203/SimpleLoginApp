package com.example.android.userentry;

import java.text.NumberFormat;

public class UserProfile {
    public String name;
    public String email;
    public String mobileNo;
    public UserProfile(){

    }

    public UserProfile(String username, String userEmail, String userNo) {
        this.name = username;
        this.email = userEmail;
        this.mobileNo = userNo;
    }

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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
