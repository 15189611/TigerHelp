package com.android.tigerhelp.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Charles on 2017/1/3.
 */

public class CategoryModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String typeId;
    public String typeName;
    public ArrayList<CategoryActivityModel> activityList;

}
