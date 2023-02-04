package com.example.hosptial;

public class User {

    String userID;
    String userPassword;
    String userName;
    String userBirth;
    String userEmail;
    String userAddress;
    String userPhone;
    String userGender;

    //Getter and Setter 만들어 준다
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserBirth() {
        return userBirth;
    }
    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserAddress() {
        return userAddress;
    }
    public void setUserAddress(String userAddress) { this.userAddress = userAddress; }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public String getUserGender() {
        return userGender;
    }
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    //이 각각의 변수에 대한 Constructor(생성자)를 만들어 준다.
    public User(String userID, String userPassword, String userName, String userBirth,String userEmail,String userAddress,String userPhone,String userGender) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userBirth = userBirth;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
        this.userGender = userGender;
    }
}