package com.heizi.pointsuser.block.home;


import com.heizi.mycommon.model.BaseModel;

/**
 * Created by leo on 16/11/3.
 */

public class ModelCategory extends BaseModel {
    String id = "";
    String icon_url = "";
    String name = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
