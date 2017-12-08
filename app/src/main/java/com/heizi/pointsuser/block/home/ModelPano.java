package com.heizi.pointsuser.block.home;

import com.heizi.mycommon.model.BaseModel;

import java.util.ArrayList;

/**
 * 首页全景model
 * Created by leo on 17/6/2.
 */

public class ModelPano extends BaseModel {

    private String id;
    private String title;
    private String member_id;
    private String keyword;
    private String class_id;
    private String province_id;
    private String city_id;
    private String spot_id;
    private String area_info;
    private String preview_img;
    private String description;
    private String pano_url;
    private ArrayList<String> keyword_arr = new ArrayList<>();
    private double latitude;
    private double longitude;


    private int delete_state = 1;//是否需要删除收藏

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDelete_state() {
        return delete_state;
    }

    public void setDelete_state(int delete_state) {
        this.delete_state = delete_state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(String spot_id) {
        this.spot_id = spot_id;
    }

    public String getArea_info() {
        return area_info;
    }

    public void setArea_info(String area_info) {
        this.area_info = area_info;
    }

    public String getPreview_img() {
        return preview_img;
    }

    public void setPreview_img(String preview_img) {
        this.preview_img = preview_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPano_url() {
        return pano_url;
    }

    public void setPano_url(String pano_url) {
        this.pano_url = pano_url;
    }

    public ArrayList<String> getKeyword_arr() {
        return keyword_arr;
    }

    public void setKeyword_arr(ArrayList<String> keyword_arr) {
        this.keyword_arr = keyword_arr;
    }
}
