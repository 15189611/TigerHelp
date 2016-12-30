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
import android.widget.TextView;

import com.android.tigerhelp.R;
import com.android.tigerhelp.adapter.BabyDataAdapter;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.entity.FileUploadModel;
import com.android.tigerhelp.entity.RelationBabyItemModel;
import com.android.tigerhelp.http.AppException;
import com.android.tigerhelp.http.responselistener.ResponseListener;
import com.android.tigerhelp.request.PersonDataRequest;
import com.android.tigerhelp.util.GalleryUtil;
import com.bumptech.glide.Glide;

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

    @Bind(R.id.baby_relation)
    TextView baby_relation;

    @Bind(R.id.address_et)
    TextView address_et;



    /** 相机设置头像生成的临时文件 */
    protected File mCameraTempFile;
    /** 头像 */
    private Bitmap mBitmap;

    private BabyDataAdapter babyDataAdapter;
    private List<RelationBabyItemModel> relationData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @OnClick(R.id.address_et)
    public void addressOnClick(){
        startActivity(new Intent(this,AddressSelectActivity.class));
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


        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        PersonDataRequest.newInstance().babyRelation(this, "relation/get", new ResponseListener<List<RelationBabyItemModel>>() {
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
                Log.i("TAG","图库----------");
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
                Log.i("TAG","拿到图片-------");
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
                        // object
                        byte[] b = bao.toByteArray();
                        // 为base64从字节编码
                       String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                        Log.i("TAG","拿到图片-------"+encoded);
                        //上传图片
                        fileUpload(encoded);
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
        PersonDataRequest.newInstance().fileUpload(this, image, "upload/image", new ResponseListener<FileUploadModel>() {
            @Override
            public void onSuccess(FileUploadModel fileUploadModel) {
                String url = fileUploadModel.getUrl();
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
}
