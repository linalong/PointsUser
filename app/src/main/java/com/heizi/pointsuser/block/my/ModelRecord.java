package com.heizi.pointsuser.block.my;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/10/10.
 */

public class ModelRecord extends BaseModel {
    private String transaction_name;
    private String num;
    private String type;
    private String create_time;

    public String getTransaction_name() {
        return transaction_name;
    }

    public void setTransaction_name(String transaction_name) {
        this.transaction_name = transaction_name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
