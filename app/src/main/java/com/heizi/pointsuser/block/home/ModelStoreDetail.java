package com.heizi.pointsuser.block.home;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/9/30.
 */

public class ModelStoreDetail extends BaseModel {
    private String name;
    private String integral_ratio;//返利折扣
    private String head_img;
    private String introduction;//介绍
    private String comment_num;//评论数
    private String ave_score;//分数
    private double longitude;
    private double latitude;
    private String address;//地址
    private String mobile;//电话
    private String type;//距离

    private String business_hours;//营业时间
    private String use_range;//使用范围

    private String business_permit;//营业执照
    private String business_license;//经营许可证


    private String image;
    private String image2;
    private String image3;
    private String desc = "";
    private String desc2 = "";
    private String desc3 = "";

    public String getBusiness_hours() {
        return business_hours;
    }

    public void setBusiness_hours(String business_hours) {
        this.business_hours = business_hours;
    }

    public String getUse_range() {
        return use_range;
    }

    public void setUse_range(String use_range) {
        this.use_range = use_range;
    }

    public String getBusiness_permit() {
        return business_permit;
    }

    public void setBusiness_permit(String business_permit) {
        this.business_permit = business_permit;
    }

    public String getBusiness_license() {
        return business_license;
    }

    public void setBusiness_license(String business_license) {
        this.business_license = business_license;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAve_score() {
        return ave_score;
    }

    public void setAve_score(String ave_score) {
        this.ave_score = ave_score;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }
}
