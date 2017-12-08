package com.heizi.pointsuser.block.home;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/9/30.
 */

public class ModelProduct extends BaseModel {

    private String id;
    private double latitude;
    private double longitude;
    private String name;
    private String integral_ratio;//返利折扣
    private String head_img;
    private int comment_num;//评论数
    private String distance;//距离
    private String ave_score;//平均分数

    private int delete_state = 1;//是否需要删除收藏

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntegral_ratio() {
        return integral_ratio;
    }

    public void setIntegral_ratio(String integral_ratio) {
        this.integral_ratio = integral_ratio;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getAve_score() {
        return ave_score;
    }

    public void setAve_score(String ave_score) {
        this.ave_score = ave_score;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
