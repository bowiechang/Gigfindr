package com.gigfindr.admin.app;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WindowInfoObject {

    private String markerID;
    private Boolean multipleShow;
    private ArrayList<ShowDetails> showDetailsArrayList;

    WindowInfoObject(String markerID, Boolean multipleShow, ArrayList<ShowDetails> showDetailsArrayList){
        this.markerID = markerID;
        this.multipleShow = multipleShow;
        this.showDetailsArrayList = showDetailsArrayList;
    }

    public String getMarkerID() {
        return markerID;
    }

    public void setMarkerID(String markerID) {
        this.markerID = markerID;
    }

    public Boolean getMultipleShow() {
        return multipleShow;
    }

    public void setMultipleShow(Boolean multipleShow) {
        this.multipleShow = multipleShow;
    }

    public ArrayList<ShowDetails> getShowDetailsArrayList() {
        return showDetailsArrayList;
    }

    public void setShowDetailsArrayList(ArrayList<ShowDetails> showDetailsArrayList) {
        this.showDetailsArrayList = showDetailsArrayList;
    }
}
