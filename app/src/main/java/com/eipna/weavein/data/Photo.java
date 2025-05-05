package com.eipna.weavein.data;

public class Photo {

    public int ID;
    public int userID;
    public byte[] photo;

    public Photo(int ID, int userID, byte[] photo) {
        this.ID = ID;
        this.userID = userID;
        this.photo = photo;
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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}