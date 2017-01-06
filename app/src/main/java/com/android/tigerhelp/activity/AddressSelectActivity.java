package com.android.tigerhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.android.tigerhelp.R;
import com.android.tigerhelp.adapter.MapAroundAdapter;
import com.android.tigerhelp.base.BaseActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * Created by huangTing on 2016/12/28.
 */

public class AddressSelectActivity extends BaseActivity implements AMap.OnMapClickListener,
        LocationSource, AMapLocationListener, CompoundButton.OnCheckedChangeListener ,PoiSearch.OnPoiSearchListener{

    private static final float sDefaultZoomLevel = 14.1f;
    private static final int sDefaultPageSize = 20;
    private static final int sDefaultPageNum = 0;

    @Bind(R.id.map)
    MapView mMapView;
    @Bind(R.id.aroundRecyclerView)
    RecyclerView aroundRecyclerView;

    /**
     * 用于显示当前的位置
     */
    private AMapLocationClient mLocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;

    //定位
    private UiSettings mUiSettings;
    private AMap mAMap;

    //Poi条件查询类
    private PoiSearch.Query query;
    private PoiSearch poiSearch;

    private PoiResult mPoiResult;
    private List<PoiItem> poiItemList = new ArrayList<>();
    private MapAroundAdapter mapAroundAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);//在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        initMap();
        initRecyclerView();

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

            CameraUpdate cameraUpdate = CameraUpdateFactory.zoomTo(sDefaultZoomLevel);
            mAMap.moveCamera(cameraUpdate);
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

        }
    }

    private void initRecyclerView(){
        aroundRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mapAroundAdapter = new MapAroundAdapter(poiItemList);
        aroundRecyclerView.setAdapter(mapAroundAdapter);
        recyclerViewOnItem();
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
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
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
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this); // 设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); // 设置为高精度定位模式
            mLocationOption.setOnceLocation(true);// 只是为了获取当前位置，所以设置为单次定位
            mLocationClient.setLocationOption(mLocationOption);// 设置定位参数
            mLocationOption.setHttpTimeOut(20000);//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setNeedAddress(true);//设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setLocationCacheEnable(true);//缓存机制，默认为true
//            mLocationOption.setWifiActiveScan(false);//设置是否强制刷新WIFI，默认为true，强制刷新。
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient.startLocation();
        }
    }

    /***
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
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

                doSearchQery(latitude,longitude);

                LatLng marker1 = new LatLng(latitude, longitude);//设置中心点和缩放比例
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(sDefaultZoomLevel));
            } else {
                Log.i("TAG","location error-----ErrorCode="+aMapLocation.getErrorCode()
                        +"--errInfo="+aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    /**
     * poi搜索
     */
    private void doSearchQery(double latitude,double longitude){
        query = new PoiSearch.Query("", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(sDefaultPageSize);
        query.setPageNum(sDefaultPageNum);

        poiSearch = new PoiSearch(this,query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latitude,longitude),500,true));
        poiSearch.searchPOIAsyn();//异步搜索
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int code) {
        if(code == AMapException.CODE_AMAP_SUCCESS){
           if(poiResult != null && poiResult.getQuery() != null){
                if(poiResult.getQuery().equals(query)){//是否是同一条
                    mPoiResult = poiResult;
                    poiItemList = mPoiResult.getPois();

                    Log.i("TAG","poiItemList========="+poiItemList.size());
                    mapAroundAdapter.getData().clear();
                    mapAroundAdapter.addData(poiItemList);
                }
           }
        }

    }

    @Override
    public void onPoiItemSearched(com.amap.api.services.core.PoiItem poiItem, int i) {

    }


    private void recyclerViewOnItem(){
        aroundRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(poiItemList.size() == 0){
                    return;
                }

                PoiItem poiItem = poiItemList.get(position);
                if(null == poiItem){
                    return;
                }

                String address = poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet();
                Intent intent = new Intent(AddressSelectActivity.this,AddressSelectDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("address",address);
                intent.putExtras(bundle);
                startActivity(intent);

                AddressSelectActivity.this.finish();
            }
        });
    }
}
