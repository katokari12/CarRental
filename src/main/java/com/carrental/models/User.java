package com.carrental.models;

public class User {
    public Integer userId;
    public String email;
    public Integer phoneNumber;
    public Byte status;
    public Integer age;
    public String fullName;
    public String password;
    public Byte isAdmin;

    public User(Integer userId, String email, Integer phoneNumber, Byte status, Integer age, String fullName, String password, Byte isAdmin) {
        this.userId = userId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.age = age;
        this.fullName = fullName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public Integer getUserId(int i) {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail(int i) {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNumber(int i) {
        return this.phoneNumber;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", status=" + status +
                ", age=" + age +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
