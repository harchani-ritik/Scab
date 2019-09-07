package com.example.android.scab69;

import java.util.ArrayList;

public class Room {


    public final static int VACANT=0;
    public final static int FULL=1;

    private User user1=new User();
    private User user2=new User();
    private User user3=new User();
    private User user4=new User();
    private String source;
    private String destination;
    private String roomTag;
    private String roomId;
    private String journeyTime;
    private String roomCreationTime;
    private ArrayList<User> tempUserList=new ArrayList<>();

    private int roomStatus;

    public Room(){
    }

    public Room(User user1,String roomCreationTime,String source,String destination,String roomTag,String roomId,String journeyTime){
        this.user1=user1;
        this.roomCreationTime=roomCreationTime;
        this.source=source;
        this.destination=destination;
        this.roomTag=roomTag;
        this.roomId=roomId;
        this.journeyTime=journeyTime;
        this.roomStatus=Room.VACANT;

        this.tempUserList=new ArrayList<>();
        user2=user3=user4=null;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public User getUser3() {
        return user3;
    }

    public User getUser4() {
        return user4;
    }

    public int getRoomStatus() {
        return roomStatus;
    }

    public String getRoomCreationTime() {
        return roomCreationTime;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setUser3(User user3) {
        this.user3 = user3;
    }

    public void setUser4(User user4) {
        this.user4 = user4;
    }


    public void setTime(String roomCreationTime) {
        this.roomCreationTime = roomCreationTime;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setRoomStatus(int roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(String roomTag) {
        this.roomTag = roomTag;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(String journeyTime) {
        this.journeyTime = journeyTime;
    }

    public ArrayList<User> getTempUserList() {
        return tempUserList;
    }

    public void setTempUserList(ArrayList<User> tempUserList) {
        this.tempUserList = tempUserList;
    }
}
