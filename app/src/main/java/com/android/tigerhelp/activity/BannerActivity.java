package com.android.tigerhelp.activity;

import android.app.Activity;
import android.os.Bundle;

import com.android.tigerhelp.R;
import com.android.tigerhelp.banner2.Banner;
import com.android.tigerhelp.banner2.BannerConfig;
import com.android.tigerhelp.banner2.GlideImageLoader;
import com.android.tigerhelp.banner2.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charles on 2017/3/9.
 */

public class BannerActivity extends Activity {
    private Banner banner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        banner = (Banner) findViewById(R.id.banner);
        List<String> bannerDatas = new ArrayList<>();
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        banner.setImages(bannerDatas);
        banner.setBannerAnimation(Transformer.Default);
        banner.setImageLoader(new GlideImageLoader());
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

}
