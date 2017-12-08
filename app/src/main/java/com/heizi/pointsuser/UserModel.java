package com.heizi.pointsuser;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 用户
 *
 * @author admin
 */
@Table(name = "user_model1", onCreated = "")
public class UserModel implements Serializable {

    public UserModel() {

    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "aid", isId = true, autoGen = true, property = "NOT NULL")
    private int aid;
    @Column(name = "id")
    private String id = "";
    @Column(name = "nickname")
    private String nickname = "";
    @Column(name = "head_img")
    private String head_img = "";
    @Column(name = "user_wxopenid")
    private String user_wxopenid = "";
    @Column(name = "user_wxinfo")
    private String user_wxinfo = "";
    @Column(name = "user_state")
    private String user_state = "";
    @Column(name = "mobile")
    private String mobile = "";
    @Column(name = "password")
    private String password = "";
    @Column(name = "token")
    private String token = "";
    @Column(name = "invitation_code")
    private String invitation_code = "";
    private String service_phone = "";//客服电话


    //融云
    @Column(name = "rongyun_token")
    private String rongyun_token = "";


    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getUser_wxopenid() {
        return user_wxopenid;
    }

    public void setUser_wxopenid(String user_wxopenid) {
        this.user_wxopenid = user_wxopenid;
    }

    public String getUser_wxinfo() {
        return user_wxinfo;
    }

    public void setUser_wxinfo(String user_wxinfo) {
        this.user_wxinfo = user_wxinfo;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRongyun_token() {
        return rongyun_token;
    }

    public void setRongyun_token(String rongyun_token) {
        this.rongyun_token = rongyun_token;
    }
}
