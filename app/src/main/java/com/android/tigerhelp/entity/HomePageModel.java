package com.android.tigerhelp.entity;


import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Charles on 2017/1/3.
 */

public class HomePageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<BannerModel> advList; //广告条
    public ArrayList<BannerModel> quickList; //品类数
    public ArrayList<HotConsultModel> informationList; //热门咨询
    public ArrayList<CategoryModel> typeList;   //分类

}
