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
    private String language;
    private String country;
    private String hobbies;
    private String religion;
    private int isPrivate;

    public static final String TYPE_FREE = "FREE";
    public static final String TYPE_PREMIUM = "PREMIUM";

    public static final int IS_PRIVATE = 1;
    public static final int NOT_PRIVATE = 0;

    public User() {

    }

    public User(int ID, String fullName, int age, String gender, String email, String password, String type, String phoneNumber, String language, String country, String hobbies, String religion, int isPrivate) {
        this.ID = ID;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.language = language;
        this.country = country;
        this.hobbies = hobbies;
        this.religion = religion;
        this.isPrivate = isPrivate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public int getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(int isPrivate) {
        this.isPrivate = isPrivate;
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