package com.eipna.weavein.data;

public class Preferences {

    private int ID;
    private int userID;
    private String language;
    private String country;
    private String religion;
    private String gender;
    private String age;
    private String hobbies;

    public Preferences(int ID, int userID, String language, String country, String religion, String gender, String age, String hobbies) {
        this.ID = ID;
        this.userID = userID;
        this.language = language;
        this.country = country;
        this.religion = religion;
        this.gender = gender;
        this.age = age;
        this.hobbies = hobbies;
    }

    public Preferences() {

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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}