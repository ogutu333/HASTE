package com.haste.ReportObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Deborah on 29/02/24.
 */

public class SOS {
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private String time;

    public SOS(){}

    public SOS(String phoneNumber, Double latitude, Double longitude){
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        time = format.format(new Date());
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
