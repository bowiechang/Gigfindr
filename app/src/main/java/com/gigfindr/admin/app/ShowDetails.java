package com.gigfindr.admin.app;

/**
 * Created by admin on 28/4/17.
 */

public class ShowDetails{

    //LocationName, Address, LatLng, Date, startTime, endTime, entryFee, userid

    private String locationName;
    private String address;
    private String latLng;
    private String date; //can be a string as well?
    private String startTime;
    private String endTime;
    private String entryFee;
    private String userid;
    private String bandName;
    private String genre;
    private String placeid;



    public ShowDetails(){}

    public ShowDetails(String locationName, String address, String latLng, String date, String startTime, String endTime, String entryFee, String userid, String bandName, String genre, String placeid){
        this.locationName = locationName;
        this.address = address;
        this.latLng = latLng;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entryFee = entryFee;
        this.userid = userid;
        this.bandName = bandName;
        this.genre = genre;
        this.placeid = placeid;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getLocationName() {
        return locationName;
    }

//    public void setLocationName(String locationName) {
//        this.locationName = locationName;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatLng() {
        return latLng;
    }

//    public void setLatLng(String latLng) {
//        this.latLng = latLng;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

//    public void setStartTime(String startTime) {
//        this.startTime = startTime;
//    }

    public String getEndTime() {
        return endTime;
    }

//    public void setEndTime(String endTime) {
//        this.endTime = endTime;
//    }

    public String getEntryFee() {
        return entryFee;
    }

//    public void setEntryFee(String entryFee) {
//        this.entryFee = entryFee;
//    }

    public String getUserid() {
        return userid;
    }

//    public void setUserid(String userid) {
//        this.userid = userid;
//    }


}
