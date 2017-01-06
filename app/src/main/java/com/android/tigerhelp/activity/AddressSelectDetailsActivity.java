package com.android.tigerhelp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.events.MainEvents;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by huangTing on 2017/1/6.
 */

public class AddressSelectDetailsActivity extends BaseActivity {

    @Bind(R.id.address_show)
    TextView address_show;
    @Bind(R.id.commit_sure)
    TextView commit_sure;

    @Bind(R.id.common_residence_ly)
    LinearLayout common_residence_ly;


    @Bind(R.id.common_residence)
    ImageView common_residence;
    @Bind(R.id.villa_ly)
    LinearLayout villa_ly;
    @Bind(R.id.villa)
    ImageView villa;

    @Bind(R.id.plot_address)
    EditText plot_address;
    @Bind(R.id.number_floor)
    EditText number_floor;
    @Bind(R.id.which_floor)
    EditText which_floor;
    @Bind(R.id.number_room)
    EditText number_room;

    @Bind(R.id.number_floor_tv)
    TextView number_floor_tv;
    @Bind(R.id.which_floor_tv)
    TextView which_floor_tv;
    @Bind(R.id.number_room_tv)
    TextView number_room_tv;
    @Bind(R.id.number_room_mark)
    TextView number_room_mark;





    private int isCommonOrVilla = 1;//默认为普通住宅

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAddress();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.address_details_select;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void getRemoteData() {

    }

    @OnClick(R.id.common_residence_ly)
    public void commonResidenceOnClick(){
        if(isCommonOrVilla== 2){
            common_residence.setVisibility(View.VISIBLE);
            villa.setVisibility(View.GONE);
            isCommonOrVilla = 1;

            setVis();
        }
    }

    @OnClick(R.id.villa_ly)
    public void villaOnClick(){
        if(isCommonOrVilla == 1){
            common_residence.setVisibility(View.GONE);
            villa.setVisibility(View.VISIBLE);
            isCommonOrVilla = 2;

            setVis();
        }
    }

    private void setAddress(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String address = bundle.getString("address");
            if(!TextUtils.isEmpty(address)){
                address_show.setText(address);
            }
        }
    }


    @OnClick(R.id.commit_sure)
    public void commitOnClick(){
        String address = address_show.getText().toString().trim();
        String plotAddress = plot_address.getText().toString().trim();
        String numberFloor = number_floor.getText().toString().trim();
        String whichFloor = which_floor.getText().toString().trim();
        String numberRoom = number_room.getText().toString().trim();

        if(isCommonOrVilla == 1){

        }else if(isCommonOrVilla == 2){

        }
        if(TextUtils.isEmpty(address)){
            return;
        }
        MainEvents mainEvents = new MainEvents();
        mainEvents.setType(MainEvents.SELECT_ADDRESS);
//        mainEvents.setObj(mLocation);
        EventBus.getDefault().post(mainEvents);
        this.finish();
    }

    private void setVis(){
        if(isCommonOrVilla == 1){
            number_floor.setVisibility(View.VISIBLE);
            which_floor.setVisibility(View.VISIBLE);
            number_floor_tv.setVisibility(View.VISIBLE);
            which_floor_tv.setVisibility(View.VISIBLE);
            number_room_tv.setVisibility(View.VISIBLE);

            number_room_mark.setVisibility(View.GONE);
        }else if(isCommonOrVilla == 2){
            number_floor.setVisibility(View.GONE);
            which_floor.setVisibility(View.GONE);
            number_floor_tv.setVisibility(View.GONE);
            which_floor_tv.setVisibility(View.GONE);
            number_room_tv.setVisibility(View.GONE);

            number_room_mark.setVisibility(View.VISIBLE);
        }
    }
}
