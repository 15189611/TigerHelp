package com.android.tigerhelp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangTing on 2016/12/27.
 */

public class RelationBabyModel implements Serializable {

    private List<RelationBabyItemModel> data;


    public List<RelationBabyItemModel> getData() {
        return data;
    }

    public void setData(List<RelationBabyItemModel> data) {
        this.data = data;
    }
}
