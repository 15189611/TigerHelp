package com.android.tigerhelp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.android.tigerhelp.R;
import com.android.tigerhelp.adapter.BabyDataAdapter;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.db.ShareUtils;
import com.android.tigerhelp.entity.FileUploadModel;
import com.android.tigerhelp.entity.RelationBabyItemModel;
import com.android.tigerhelp.events.MainEvents;
import com.android.tigerhelp.http.AppException;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.android.tigerhelp.request.PersonDataRequest;
import com.android.tigerhelp.util.BussinessUtil;
import com.android.tigerhelp.util.GalleryUtil;
import com.android.tigerhelp.util.MD5;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by huangTing on 2016/12/23.
 */

public class PersonDataActivity  extends BaseActivity{

    @Bind(R.id.iv_head)
    ImageView iv_head;

    @Bind(R.id.name_et)
    EditText name_et;
    @Bind(R.id.baby_relation)
    TextView baby_relation;
    @Bind(R.id.realityName_et)
    EditText realityName_et;
    @Bind(R.id.birthday_et)
    EditText birthday_et;
    @Bind(R.id.personalized_signature)
    EditText personalized_signature;

    @Bind(R.id.baby_name)
    EditText baby_name;
    @Bind(R.id.baby_name_reality)
    EditText baby_name_reality;
    @Bind(R.id.baby_sex)
    EditText baby_sex;
    @Bind(R.id.baby_birthday)
    EditText baby_birthday;
    @Bind(R.id.baby_school)
    EditText baby_school;
    @Bind(R.id.baby_class)
    EditText baby_class;

    @Bind(R.id.baby_name_add)
    EditText baby_name_add;
    @Bind(R.id.baby_name_reality_add)
    EditText baby_name_reality_add;
    @Bind(R.id.baby_sex_add)
    EditText baby_sex_add;
    @Bind(R.id.baby_birthday_add)
    EditText baby_birthday_add;
    @Bind(R.id.baby_school_add)
    EditText baby_school_add;
    @Bind(R.id.baby_class_add)
    EditText baby_class_add;

    @Bind(R.id.address_et)
    EditText address_et;

    @Bind(R.id.baby_add_rl)
    LinearLayout baby_add_rl;
    @Bind(R.id.add_baby_bt)
    RelativeLayout add_baby_bt;

    @Bind(R.id.commit)
    TextView commit;

    /** 相机设置头像生成的临时文件 */
    protected File mCameraTempFile;
    /** 头像 */
    private Bitmap mBitmap;

    private BabyDataAdapter babyDataAdapter;
    private List<RelationBabyItemModel> relationData = new ArrayList<>();

    private String name;//选择与宝宝的关系
    private int index;

    private int visibility = -1;//添加宝宝新布局显示隐藏标志
    private int sexAdd;
    private String imageName;
    private AMapLocation aMapLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.personal_data;
    }

    @Override
    protected void initView() {
        setTitle("个人资料");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getRemoteData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.address_et)
    public void addressOnClick(){
        startActivity(new Intent(this,AddressSelectActivity.class));
    }

    @OnClick(R.id.commit)
    public void commitOnClick(){
        saveUpdateData();
    }

    @OnClick(R.id.add_baby_bt)
    public void addBabyOnClick(){
        visibility  = baby_add_rl.getVisibility();
        if(visibility == 0){
            showToast("每个用户只能添加两个宝宝");
            return;
        }
        baby_add_rl.setVisibility(View.VISIBLE);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MainEvents mainEvents){
        try{
            if(mainEvents.getType() == MainEvents.SELECT_ADDRESS){
                aMapLocation = (AMapLocation) mainEvents.getObj();
                if(!TextUtils.isEmpty(aMapLocation.getAddress())){
                    address_et.setText(aMapLocation.getAddress());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @OnClick(R.id.baby_relation)
    public void babyRelation(){
        final Dialog dialog = new Dialog(this, R.style.dialog_style);
        View view = LayoutInflater.from(this).inflate(R.layout.persron_select_value, null);
        TextView title = (TextView) view.findViewById(R.id.title_data);
        RecyclerView dataRecycler = (RecyclerView) view.findViewById(R.id.dataRecycler);
        View commit = view.findViewById(R.id.commit_data);

        getRelation(dialog,view);
        dataRecycler.setLayoutManager(new LinearLayoutManager(PersonDataActivity.this));
        babyDataAdapter = new BabyDataAdapter(relationData);
        dataRecycler.setAdapter(babyDataAdapter);

        dataRecycler.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(relationData.size() == 0){
                    return;
                }

                RelationBabyItemModel model = relationData.get(position);
                if(null == model){
                    return;
                }

                int index1 = model.getIndex();
                for (RelationBabyItemModel item: relationData) {
                    if(index1 == item.getIndex()){//当前点击的条目
                        item.setSelected(true);
                        index = index1;
                        name = item.getName();
                    }else{
                        item.setSelected(false);
                    }
                }
                babyDataAdapter.notifyDataSetChanged();
            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(name)){
                    baby_relation.setText(name);
                }
                dialog.dismiss();
            }
        });

    }

    @OnClick(R.id.iv_head)
    public void headOnClick(){
       final Dialog dialog = new Dialog(this, R.style.dialog_style);
        View view = LayoutInflater.from(this).inflate(R.layout.select_photo, null);
        // 拍照
        View takePhoto = view.findViewById(R.id.photograph_v);
        View selectPhoto = view.findViewById(R.id.photos_v);
        View cancel = view.findViewById(R.id.cancel_tv);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraTempFile = GalleryUtil.generateTempFiles();
                boolean isCalled = GalleryUtil.openImageCamera(PersonDataActivity.this, mCameraTempFile);
                if (!isCalled) {
                    showToast("抱歉,您的手机无法修改头像");
                }
                dialog.dismiss();
            }
        });

        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryUtil.openGalleryFromLocal(PersonDataActivity.this);
                dialog.dismiss();
            }
        });
        showDialog(dialog,view);
    }

    /***
     * 宝宝与自己的关系
     * @param dialog
     * @param view
     */
    private void getRelation(final Dialog dialog, final View view){
        PersonDataRequest.newInstance().babyRelation(this, "appUser/relation/get", new ResponseListener<List<RelationBabyItemModel>>() {
                    @Override
                    public void onSuccess(List<RelationBabyItemModel> data) {
                        babyDataAdapter.getData().clear();
                        if(data.size() > 0){
                            showDialog(dialog,view);
                        }
                        babyDataAdapter.addData(data);
                    }
                    @Override
                    public void onFailure(AppException e) {
                        setErrorMsg(e.errorMsg,"暂时没有数据额");
                    }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case GalleryUtil.REQUEST_GALLERT_FROM_LOACL:// 图库
                try {
                    if (intent.getData() != null) {
                        Uri uri = intent.getData();
                        GalleryUtil.cropImageByUri(this, uri);
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            case GalleryUtil.REQUEST_CODE_CAMERA:// 相机
                if (mCameraTempFile == null || !mCameraTempFile.exists()) {
                    return;
                }
                Uri uri = Uri.fromFile(mCameraTempFile);
                GalleryUtil.cropImageByUri(this, uri);
                break;
            case GalleryUtil.CROP_IMAGE:// 可以直接显示图片,或者进行其他处理(如压缩等)
                try {
                    if (null != intent) {
                        Uri imageUri = intent.getData();
                        if (null == imageUri) {
                            mBitmap = intent.getParcelableExtra("data");
                        } else {
                            mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                    imageUri);
                        }
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
                        byte[] b = bao.toByteArray();
                       String encoded = Base64.encodeToString(b, Base64.DEFAULT); // 为base64从字节编码
                        fileUpload(encoded);//上传图片
                    }
                } catch (Exception e) {
                } finally {
                    if (mCameraTempFile != null && mCameraTempFile.exists()) {
                        mCameraTempFile.delete();
                        mCameraTempFile = null;
                    }
                }
                break;
        }
    }

    /***
     * 上传图片
     * @param image
     */
    public void fileUpload(String image){
        PersonDataRequest.newInstance().fileUpload(this, image, "appUser/upload/image", new ResponseListener<FileUploadModel>() {
            @Override
            public void onSuccess(FileUploadModel fileUploadModel) {
                String url = fileUploadModel.getUrl();
                imageName = fileUploadModel.getImageName();
                if(!TextUtils.isEmpty(url)){
                    Glide.with(PersonDataActivity.this).load(url).placeholder(R.mipmap.my_head_icon).error(R.mipmap.my_head_icon).into(iv_head);
                    showToast("照片修改成功");
                }else{
                    showToast("照片修改失败");
                }
            }
            @Override
            public void onFailure(AppException e) {
                setErrorMsg(e.errorMsg,"照片修改失败");
            }
        });
    }

    private void setErrorMsg(String errorMsg,String customMsg){
        if(!TextUtils.isEmpty(errorMsg)){
            showToast(errorMsg);
        }else{
            showToast(customMsg);
        }
    }

    private void showDialog(Dialog dialog,View view){
        dialog.getWindow().setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    private void saveUpdateData(){
        int sex;
        String nickName = name_et.getText().toString().trim();
        if(TextUtils.isEmpty(nickName)){
            showToast("请输入昵称");
            return;
        }
        String babyRelation = baby_relation.getText().toString().trim();
        if(TextUtils.isEmpty(babyRelation)){
            showToast("请选择与宝宝的关系");
            return;
        }

        String address = address_et.getText().toString().trim();
        if(TextUtils.isEmpty(address)){
            showToast("请输入详细地址");
            return;
        }

        String realityName = realityName_et.getText().toString().trim();
        if(TextUtils.isEmpty(realityName)){
            showToast("请输入真实名字");
            return;
        }
        String birthday = birthday_et.getText().toString().trim();
        String personalizedSignature = personalized_signature.getText().toString().trim();
        if(!TextUtils.isEmpty(personalizedSignature) && personalizedSignature.length() > 50){
            showToast("个性签名不可超过50个字额");
            return;
        }

        String babyName = baby_name.getText().toString().trim();
        if(TextUtils.isEmpty(babyName)){
           showToast("请输入宝宝昵称");
            return;
        }

        String babyRealityName = baby_name_reality.getText().toString().trim();
        if(TextUtils.isEmpty(babyRealityName)){
           showToast("请输入宝宝真实姓名");
            return;
        }
        String babySex = baby_sex.getText().toString().trim();
        if(TextUtils.isEmpty(babySex)){
           showToast("请输入宝宝性别");
            return;
        }
        if(babySex.equals("男")){
            sex = 1;
        }else if(babySex.equals("女")){
            sex = 2;
        }else{
            showToast("请输入合法的性别额");
            return;
        }
        String babyBirthday = baby_birthday.getText().toString().trim();
        if(TextUtils.isEmpty(babyBirthday)){
           showToast("请输入宝宝出生年月日");
            return;
        }
        String babySchool = baby_school.getText().toString().trim();
        if(TextUtils.isEmpty(babySchool)){
           showToast("请输入宝宝学校");
            return;

        }
        String babyClass = baby_class.getText().toString().trim();
        if(TextUtils.isEmpty(babyClass)){
           showToast("请输入宝宝班级");
            return;

        }
        String babyNameAdd = baby_name_add.getText().toString().trim();
        String babyRealityNameAdd = baby_name_reality_add.getText().toString().trim();
        String babySexAdd = baby_sex_add.getText().toString().trim();
        String babyBirthdayAdd = baby_birthday_add.getText().toString().trim();
        String babySchoolAdd = baby_school_add.getText().toString().trim();
        String babyClassAdd = baby_class_add.getText().toString().trim();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject babyObject = new JSONObject();
        JSONObject addBabyObject = new JSONObject();

        try {
            babyObject.put("babyId",0);
            babyObject.put("nikeName",babyName);
            babyObject.put("realName",babyRealityName);
            babyObject.put("sex",sex);
            babyObject.put("birthday",babyBirthday);
            babyObject.put("mySchool",babySchool);
            babyObject.put("myClass",babyClass);

            Log.i("TAG","visibility==="+visibility);

            if(visibility == 0) {
                boolean addBaby = addBaby(babyNameAdd, babyRealityNameAdd, babySexAdd, babyBirthdayAdd, babySchoolAdd, babyClassAdd);

                Log.i("TAG","addBaby==="+addBaby);

                if (addBaby) {//添加宝宝属性非空判断 true:宝宝属性字段不为空
                    addBabyObject.put("babyId", 0);//新增宝宝id=0;
                    addBabyObject.put("nikeName", babyNameAdd);
                    addBabyObject.put("realName", babyRealityNameAdd);
                    addBabyObject.put("sex", sexAdd);
                    addBabyObject.put("birthday", babyBirthdayAdd);
                    addBabyObject.put("mySchool", babySchoolAdd);
                    addBabyObject.put("myClass", babyClassAdd);
                }
            }

            jsonArray.put(0,babyObject);
            jsonArray.put(1,addBabyObject);
            jsonObject.put("babyList",jsonArray);

            Log.i("TAG","baby list==="+jsonObject.toString());
            Log.i("TAG","baby list==="+jsonArray.toString());

            commitRequest(nickName,index,address,aMapLocation.getLongitude(),aMapLocation.getLatitude(),1,1,1,1,realityName,birthday,personalizedSignature,"",jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private boolean addBaby(String babyNameAdd,String babyRealityNameAdd,String babySexAdd,String babyBirthdayAdd,String babySchoolAdd,String babyClassAdd){
        if(TextUtils.isEmpty(babyNameAdd)){
            showToast("请输入第二个宝宝昵称");
            return false;
        }
        if(TextUtils.isEmpty(babyRealityNameAdd)){
            showToast("请输入第二个宝宝真实姓名");
            return false;
        }
        if(TextUtils.isEmpty(babySexAdd)){
            showToast("请输入第二个宝宝性别");
            return false;
        }
        if(babySexAdd.equals("男")){
            sexAdd = 1;
        }else if(babySexAdd.equals("女")){
            sexAdd = 2;
        }else{
            showToast("请输入合法的性别额");
            return false;
        }
        if(TextUtils.isEmpty(babyBirthdayAdd)){
            showToast("请输入第二个宝宝出生年月日");
            return false;
        }
        if(TextUtils.isEmpty(babySchoolAdd)){
            showToast("请输入第二个宝宝学校");
            return false;
        }
        if(TextUtils.isEmpty(babyClassAdd)){
            showToast("请输入第二个宝宝班级");
            return false;
        }
        return true;
    }


    private void commitRequest(String nikeName, int relation, String address, double longitude, double latitude, int addrType, int floor, int layer, int room, String realName, String birthday, String notes, String deleteBabyIds, JSONArray babyList){
        String userId = (String) ShareUtils.get(this, "userId");
        String token = (String) ShareUtils.get(this, "token");
        String sign = null;
        if(BussinessUtil.isValid(userId) && BussinessUtil.isValid(token)){
            sign = MD5.getMD5Str(String.valueOf(System.currentTimeMillis() + userId + token + userId));
        }

        PersonDataRequest.newInstance().updateUserData(this, token, sign, imageName, nikeName, relation, address, longitude, latitude, addrType, floor, layer, room, realName, birthday, notes, deleteBabyIds, babyList, new ResponseListener<String>() {
            @Override
            public void onSuccess(String s) {
            }
            @Override
            public void onFailure(AppException e) {
                Log.i("TAG","getCode--->onFailure=============="+e.errorMsg);
                Log.i("TAG","getCode--->onFailure=============="+e.errorCode);
                if(TextUtils.isEmpty(e.errorCode)){
                    if(!TextUtils.isEmpty(e.errorMsg)){
                        showToast(e.errorMsg);
                    }else{
                        showToast("用户信息提交失败");
                    }
                    return;
                }
                if(e.errorCode.equals("0")){/**修改成功，则都是失败*/
                    showToast("用户信息提交成功");
                }else{
                    showToast("用户信息提交失败");
                }
            }
        });
    }
}
