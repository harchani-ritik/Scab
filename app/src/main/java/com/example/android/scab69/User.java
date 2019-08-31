package com.example.android.scab69;

public class User {


    public final static int IDLE=0;
    public final static int AVAILABLE=1;
    public final static int INAROOM=2;

    private String name = "ASSIGNED";
    private String gender;
    private String phoneNumber;
    private String communityStatus;
    //basically community status is an additional variable which we will use here for storing Roll Number
    private int userStatus = 2;


    public User(){
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getCommunityStatus() {
        return communityStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setCommunityStatus(String communityStatus) {
        this.communityStatus = communityStatus;
    }

    public int getStatus() {
        return userStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(int status) {
        this.userStatus = status;
    }
}
