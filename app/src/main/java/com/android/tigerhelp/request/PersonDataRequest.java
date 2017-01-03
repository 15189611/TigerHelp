package com.android.tigerhelp.request;

import android.app.Activity;

import com.android.tigerhelp.entity.FileUploadModel;
import com.android.tigerhelp.entity.RelationBabyItemModel;
import com.android.tigerhelp.http.TigerRequest;
import com.android.tigerhelp.http.request.RequestParamBuilder;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by huangTing on 2016/12/27.
 */

public class PersonDataRequest extends TigerRequest {

    public static PersonDataRequest newInstance() {
        return new PersonDataRequest();
    }

    /***
     * 修改个人资料图片
     * @param activity
     * @param image
     * @param requestMethod
     * @param listener
     */
    public void fileUpload(Activity activity, String image, String requestMethod, ResponseListener<FileUploadModel> listener){
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();
        requestParamBuilder.putParam("image",image);

        Type type = new TypeToken<FileUploadModel>() {
        }.getType();

        requestDataWithDialog(activity,requestMethod,requestParamBuilder.create(),type,listener);
    }

    /***
     * 选择与宝宝之间的关系
     * @param activity
     * @param requestMethod
     * @param listener
     */
    public void babyRelation(Activity activity, String requestMethod, ResponseListener<List<RelationBabyItemModel>> listener){
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();

        Type type = new TypeToken<List<RelationBabyItemModel>>() {
        }.getType();

        requestDataWithDialog(activity,requestMethod,requestParamBuilder.create(),type,listener);

    }

    /***
     *
     * @param activity
     * @param token
     * @param sign
     * @param icon 图像：图片名称
     * @param nikeName 昵称
     * @param relation 用户与宝宝之间的关系 index
     * @param address 小区地址
     * @param longitude 经度
     * @param latitude 纬度
     * @param addrType 住宅类型 1：普通住宅，2：别墅
     * @param floor 几号楼
     * @param layer 几层
     * @param room 几室或者门牌号
     * @param realName 真实姓名
     * @param birthday 出生年月日
     * @param notes 个性签名
     * @param deleteBabyIds 删除宝宝id，删除多个，逗号隔开
     * @param babyList 用户宝宝信息集合
     *      babyList：用户的宝宝信息集合
                [{
                     babyId：宝宝id ，如果是新增的id传0，修改的传查询到的宝宝id
                     nikeName：宝宝昵称
                     realName：真实姓名
                     sex：宝宝性别 1-男，2-女,传数字
                     birthday：宝宝出生年月 格式 yyyy-MM-dd
                     mySchool：宝宝学校
                     myClass：宝宝班级
                }]
     */
    public void updateUserData(Activity activity, String token, String sign, String icon, String nikeName, int relation, String address, double longitude, double latitude, int addrType, int floor, int layer, int room, String realName, String birthday, String notes, String deleteBabyIds, JSONArray babyList,String requestMethod,ResponseListener<String> listener){
        RequestParamBuilder requestParamBuilder = RequestParamBuilder.newInstance();

    //    requestParamBuilder.putParam("token",token);
   //     requestParamBuilder.putParam("sign",sign);
        requestParamBuilder.putParam("icon",icon);
        requestParamBuilder.putParam("nikeName",nikeName);
        requestParamBuilder.putParam("relation",relation);
        requestParamBuilder.putParam("address",address);
        requestParamBuilder.putParam("longitude",longitude);
        requestParamBuilder.putParam("latitude",latitude);
        requestParamBuilder.putParam("addrType",addrType);
        requestParamBuilder.putParam("floor",floor);
        requestParamBuilder.putParam("layer",layer);
        requestParamBuilder.putParam("room",room);
        requestParamBuilder.putParam("realName",realName);
        requestParamBuilder.putParam("birthday",birthday);
        requestParamBuilder.putParam("notes",notes);
        requestParamBuilder.putParam("deleteBabyIds",deleteBabyIds);

        Type type = new TypeToken<String>() {
        }.getType();

        requestDataWithDialog(activity,requestMethod,requestParamBuilder.create(),type,listener);

    }


}
