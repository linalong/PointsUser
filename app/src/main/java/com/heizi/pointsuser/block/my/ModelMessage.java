package com.heizi.pointsuser.block.my;

import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 17/12/8.
 */

public class ModelMessage extends BaseModel {
    private String id;
    private String title;
    private String message;
    private String is_read;//0:未读 1:已读
    private String create_time;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }
}
