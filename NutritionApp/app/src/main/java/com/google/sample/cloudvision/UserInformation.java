package com.google.sample.cloudvision;

/**
 * Created by Jin on 2017-05-18.
 */

public class UserInformation {

    public String fName;
    public String lName;
    public String gender;
    public int auth;

    public UserInformation(){

    }
    public UserInformation(String fName, String lName, String gender, int auth ) {
        this.fName = fName;
        this.lName = lName;
        this.gender = gender;
        this.auth = auth;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfName() {
        return fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getlName() {
        return lName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public int getAuth() {
        return auth;
    }

}
