package com.example.android.scab69;

public class Room {


    public final static int VACANT=0;
    public final static int FULL=1;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User tempUser;
    private String time;
    private String source;
    private String destination;
    private String roomTag;
    private String roomId;

    public User getTempUser() {
        return tempUser;
    }

    private int roomStatus;

    public Room(){
    }

    public Room(User user1,String time,String source,String destination,String roomTag,String roomId){
        this.user1=user1;
        this.time=time;
        this.source=source;
        this.destination=destination;
        this.roomTag=roomTag;
        this.roomId=roomId;
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

    public String getTime() {
        return time;
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

    public void setTempUser(User tempUser) {
        this.tempUser = tempUser;
    }

    public void setTime(String time) {
        this.time = time;
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
}
