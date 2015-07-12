package com.m1905.mobile.dao;

import java.io.Serializable;

/**
 * Created by Eric on 14-1-25.
 */
public class Buy implements Serializable {

    int imgId;
    String title;
    String subTitle;

    public Buy(int imgId, String title, String subTitle){
        this.imgId = imgId;
        this.title = title;
        this.subTitle = subTitle;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
