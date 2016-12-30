package com.android.tigerhelp.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.android.tigerhelp.R;
import com.android.tigerhelp.base.BaseActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.Bind;

/**
 * Created by huangTing on 2016/12/28.
 */

public class AddressSelectActivity extends BaseActivity implements AMap.OnMapClickListener,
        LocationSource,
        AMapLocationListener,
        CompoundButton.OnCheckedChangeListener {

    private static final float sDefaultZoomLevel = 16.1f;

    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.address_tv)
    TextView address_tv;
    @Bind(R.id.commit_sure)
    TextView commit_sure;

    /**
     * 用于显示当前的位置
     */
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;

    //定位
    private UiSettings mUiSettings;

    private AMap mAMap;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);//在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        Log.i("TAG","onCreate----------");
        initMap();

        String sHA1 = sHA1(this);
        Log.i("TAG","sHA1========="+sHA1);
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
        }
        if (mAMap != null && mUiSettings != null) {
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setScaleControlsEnabled(true);
            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);

            mAMap.setLocationSource(this);
            mUiSettings.setMyLocationButtonEnabled(true);
            mAMap.setMyLocationEnabled(true);

            MyLocationStyle myLocationStyle = new MyLocationStyle();//自定义系统定位蓝点
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));//设置精度范围圆形边框颜色
            myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));//设置圆形填充颜色
            mAMap.setMyLocationStyle(myLocationStyle);

            CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(sDefaultZoomLevel);
            mAMap.moveCamera(cameraUpdate);
        }
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected int getLayoutResId() {
        Log.i("TAG","getLayoutResId----------");
        return R.layout.address_select;
    }

    @Override
    protected void initView() {
        setTitle("选择地址");
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
        mMapView.onDestroy();//销毁地图
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();//重新绘制加载地图
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();//暂停地图的绘制
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    /***
     * 激活定位
     * @param
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this); // 设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 设置为高精度定位模式
            mLocationOption.setOnceLocation(true);// 只是为了获取当前位置，所以设置为单次定位
            mlocationClient.setLocationOption(mLocationOption);// 设置定位参数
            mLocationOption.setHttpTimeOut(20000);//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setNeedAddress(true);//设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setLocationCacheEnable(true);//缓存机制，默认为true
//            mLocationOption.setWifiActiveScan(false);//设置是否强制刷新WIFI，默认为true，强制刷新。
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            mlocationClient.startLocation();

          /*  //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象：设置发起定位的模式和相关参数。
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。AMapLocationMode.Device_Sensors：仅设备模式。Battery_Saving：低耗能模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);

            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(2000);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否强制刷新WIFI，默认为true，强制刷新。
            mLocationOption.setWifiActiveScan(false);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //缓存机制，默认为true
            mLocationOption.setLocationCacheEnable(true);

            //给定位客户端对象设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //启动定位
            mlocationClient.startLocation();*/
        }
    }

    /***
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                double latitude = aMapLocation.getLatitude();//纬度
                double longitude = aMapLocation.getLongitude();//经度
                String address = aMapLocation.getAddress();
                if(!TextUtils.isEmpty(address)){
                    address_tv.setText(address);
                }

                Log.i("TAG","定位成功==="+aMapLocation.getErrorCode());
                Log.i("TAG","定位成功===latitude="+latitude);
                Log.i("TAG","定位成功===longitude="+longitude);
                Log.i("TAG","定位成功==="+aMapLocation.getStreetNum());
                Log.i("TAG","定位成功==="+aMapLocation.getStreet());
                Log.i("TAG","定位成功==="+aMapLocation.getFloor());
                Log.i("TAG","定位成功==="+aMapLocation.getAddress());

            } else {
                Log.i("TAG","location error-----ErrorCode="+aMapLocation.getErrorCode()
                        +"--errInfo="+aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
}
