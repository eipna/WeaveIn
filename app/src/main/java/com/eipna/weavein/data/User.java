package com.eipna.weavein.data;

public class User {
    private int ID;
    private String fullName;
    private int age;
    private String gender;
    private String email;
    private String password;
    private String type;
    private String phoneNumber;

    public static String TYPE_FREE = "FREE";
    public static String TYPE_PREMIUM = "PREMIUM";

    public User(int ID, String fullName, int age, String gender, String email, String password, String type, String phoneNumber) {
        this.ID = ID;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.type = type;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}