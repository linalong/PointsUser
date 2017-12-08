package com.heizi.pointsuser.block.maidan;

import android.widget.ImageView;


import java.io.Serializable;

public class DeviceSignedActionImage implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;

    public String localUrl;//图片显示路径
    public String httpUrl;
    public int type = 0;//判断是否是最后一个(0:不是最后一个，1：是最后一个)
    public int isSusses = -1;//0是上传成功,1是失败,2上传中
    public ImageView processImageView;


    public DeviceSignedActionImage() {
        // TODO Auto-generated constructor stub
    }


    public DeviceSignedActionImage(String localUrl, String httpUrl, int type,
                                   int isSusses, ImageView processImageView) {
        super();
        this.localUrl = localUrl;
        this.httpUrl = httpUrl;
        this.type = type;
        this.isSusses = isSusses;
        this.processImageView = processImageView;
    }


    public int getIsSusses() {
        return isSusses;
    }


    public void setIsSusses(int isSusses) {
        this.isSusses = isSusses;
    }


    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DeviceSignedActionImage [localUrl=" + localUrl + ", httpUrl="
                + httpUrl + ", type=" + type + ", isSusses=" + isSusses + "]";
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof DeviceSignedActionImage) {
            DeviceSignedActionImage info = (DeviceSignedActionImage) o;
            try {
                if (this.getHttpUrl().equals(info.getHttpUrl())) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.equals(o);
    }


    public ImageView getProcessImageView() {
        return processImageView;
    }


    public void setProcessImageView(ImageView processImageView) {
        this.processImageView = processImageView;
    }

}
