package com.haste.ReportObject;

/**
 * Created by Deborah on 29/02/24.
 */

public class Notification {
    String body;
    String key;
    String zipcode;
    String type;

    public Notification(){}


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
