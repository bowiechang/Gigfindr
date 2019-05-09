package com.gigfindr.admin.app;

/**
 * Created by admin on 25/4/17.
 */

public class UserDetails {

    private String name;
    private String about;
    private String instagramName;

    public UserDetails(){}

    public UserDetails(String name, String about, String instagramName){
        this.name = name;
        this.about = about;
        this.instagramName = instagramName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getinstagramName() {
        return instagramName;
    }

    public void setInstagramName(String instagramName) {
        this.instagramName = instagramName;
    }
}
