package com.android.tigerhelp.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Charles on 2016/12/22.
 */

public class HomeAllDataModel<T> implements MultiItemEntity {

    public static final int BANNER_HEAD = 1;
    public static final int CATEGORY_GOD = 2;
    public static final int GOD_LIST = 3;
    private int itemType;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType){
        this.itemType = itemType;
    }

    private T model;

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

}
