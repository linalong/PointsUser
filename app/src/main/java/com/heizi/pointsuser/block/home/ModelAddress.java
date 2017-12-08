package com.heizi.pointsuser.block.home;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/9/20.
 */

public class ModelAddress extends BaseModel {

    String city;
    String city_id;
    String address;
    double longitude = 0;
    double latitude = 0;

    public String getCity() {
        return city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
