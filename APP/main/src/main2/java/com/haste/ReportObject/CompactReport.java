package com.haste.ReportObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Deborah on 29/02/24.
 */

public class CompactReport implements Parcelable{

    public Map<String, ArrayList<String>> compactReports = new HashMap<>();
    public Map<String, String> rssFeedReport = new HashMap<>();

    public String author;
    public String title;
    public Double longitude;
    public Double latitude;
    public String phoneNumber;
    public String type;
    public String timestamp;
    public boolean verified;
    public boolean isdeclined;
    public long eat_timestamp;

    public List<String> pathToServer;


    public CompactReport(){}

    public Map<String, ArrayList<String>> getCompactReports() {
        return compactReports;
    }

    public void setCompactReports(Map<String, ArrayList<String>> compactReports) {
        this.compactReports = compactReports;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isIsdeclined() {
        return isdeclined;
    }

    public void setIsdeclined(boolean isdeclined) {
        this.isdeclined = isdeclined;
    }

    public List<String> getPathToServer() {
        return pathToServer;
    }

    public void setPathToServer(List<String> pathToServer) {
        this.pathToServer = pathToServer;
    }

    public Map<String, String> getRssFeedReport() {
        return rssFeedReport;
    }

    public long getUtc_timestamp() {
        return utc_timestamp;
    }

    public void setUtc_timestamp(long utc_timestamp) {
        this.utc_timestamp = utc_timestamp;
    }

    public void setRssFeedReport(Map<String, String> rssFeedReport) {
        this.rssFeedReport = rssFeedReport;
    }

    public CompactReport(IncidentReport incidentReport, Double longitude, Double latitude,
                         String phone, String type, String timestamp, boolean isVerified){

        for(Report r : incidentReport.reports){
            Log.d("CompactReport", r.toString());

            ArrayList<String> questions = new ArrayList<>();

            for(Question q : r.questions){
                Log.d("CompactReport" + r.type, q.getQuestion()+", "+q.getChoices());
                questions.add(q.getQuestion()+":"+q.getChoices());
            }

            compactReports.put(r.getType(), questions);
        }

        if(incidentReport.reports.size() == 0) Log.d("CompactReport", "Empty");

        this.longitude = longitude;
        this.latitude = latitude;
        this.phoneNumber = phone;
        this.type = type;
        this.timestamp = timestamp;
        this.verified = isVerified;
    }

    //parceble implementation
    //Parceble implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(phoneNumber);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(type);
        dest.writeString(timestamp);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(compactReports.size());
        for(Map.Entry<String, ArrayList<String>> entry : compactReports.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeList(entry.getValue());
        }

        dest.writeInt(rssFeedReport.size());
        for(Map.Entry<String, String> entry: rssFeedReport.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public static final Parcelable.Creator<CompactReport> CREATOR
            = new Parcelable.Creator<CompactReport>() {
        public CompactReport createFromParcel(Parcel in) {
            return new CompactReport(in);
        }

        public CompactReport[] newArray(int size) {
            return new CompactReport[size];
        }
    };

    private CompactReport(Parcel in) {

        phoneNumber = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        type = in.readString();
        timestamp = in.readString();
        title = in.readString();
        author = in.readString();
        int size = in.readInt();

        for(int i = 0; i < size; i++){
            String key = in.readString();
            ArrayList<String> val = in.readArrayList(String.class.getClassLoader());
            compactReports.put(key, val);
        }

        int rssReportSize = in.readInt();

        for(int j = 0; j < rssReportSize; j++){
            String key = in.readString();
            String value = in.readString();
            rssFeedReport.put(key, value);
        }
    }
}
