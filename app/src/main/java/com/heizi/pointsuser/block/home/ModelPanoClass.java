package com.heizi.pointsuser.block.home;

import com.heizi.mycommon.model.BaseModel;

/**
 * 获取场景分类
 * Created by leo on 17/5/29.
 */

public class ModelPanoClass extends BaseModel {
    private String gc_id;
    private String gc_name;
    private String gc_parent_id;
    private String gc_sort;
    private String gc_show;


    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
    }

    public String getGc_parent_id() {
        return gc_parent_id;
    }

    public void setGc_parent_id(String gc_parent_id) {
        this.gc_parent_id = gc_parent_id;
    }

    public String getGc_sort() {
        return gc_sort;
    }

    public void setGc_sort(String gc_sort) {
        this.gc_sort = gc_sort;
    }

    public String getGc_show() {
        return gc_show;
    }

    public void setGc_show(String gc_show) {
        this.gc_show = gc_show;
    }
}
