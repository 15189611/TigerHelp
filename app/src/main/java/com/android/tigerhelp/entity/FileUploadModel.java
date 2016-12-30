package com.android.tigerhelp.entity;

import java.io.Serializable;

/**
 * Created by huangTing on 2016/12/27.
 */

public class FileUploadModel implements Serializable{

    private String url;
    private String imageName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
