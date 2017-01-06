package com.android.tigerhelp.entity;

import java.io.Serializable;

/**
 * Created by huangTing on 2016/12/27.
 */

public class RelationBabyItemModel implements Serializable {

    private int index;
    private String name;

    private boolean isSelected;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
