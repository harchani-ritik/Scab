package com.example.android.scab69;

public class User {


    public final static int IDLE=0;
    public final static int INAROOM=1;

    public final static int MALE=0;
    public final static int FEMALE=0;

    private String name;
    private int gender;
    private int age;
    private String phoneNumber;
    private String communityStatus;
    private String uid;
    //basically community status is an additional variable which we will use here for storing Roll Number
    private int userStatus;


    public User(){
    }

    public String getName() {
        return name;
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


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(int status) {
        this.userStatus = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
