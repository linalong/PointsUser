package com.heizi.pointsuser.block.my;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/12/7.
 */

public class ModelReward extends BaseModel {

    private String num;
    private String remark;//邀请直接
    private String create_time;//2017-12-03 16:06:01
    private String type;
    private String transaction_id;
    private String transaction_name;
    private String transaction_head_img;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_name() {
        return transaction_name;
    }

    public void setTransaction_name(String transaction_name) {
        this.transaction_name = transaction_name;
    }

    public String getTransaction_head_img() {
        return transaction_head_img;
    }

    public void setTransaction_head_img(String transaction_head_img) {
        this.transaction_head_img = transaction_head_img;
    }
}
