package com.heizi.pointsuser.block.home;


import com.heizi.mycommon.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.type;

/**
 * Created by leo on 16/11/3.
 */

public class ModelHome extends BaseModel {
    String id;
    String image_url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
