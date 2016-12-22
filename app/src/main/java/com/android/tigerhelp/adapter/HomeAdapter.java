package com.android.tigerhelp.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tigerhelp.R;
import com.android.tigerhelp.banner.AutoScrollViewPager;
import com.android.tigerhelp.banner.BaseViewPagerAdapter;
import com.android.tigerhelp.entity.HomeAllDataModel;
import com.android.tigerhelp.imgageLoad.ImageLoadUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Charles on 2016/12/22.
 */

public class HomeAdapter extends BaseMultiItemQuickAdapter<HomeAllDataModel> {

    private Context mContext;
    public HomeAdapter(Context context,List<HomeAllDataModel> data) {
        super(data);
        this.mContext = context;
        addItemType(HomeAllDataModel.BANNER_HEAD, R.layout.fragment_index_banner);
        addItemType(HomeAllDataModel.CATEGORY_GOD, R.layout.fragment_index_category);
        addItemType(HomeAllDataModel.GOD_LIST, R.layout.fragment_index_god_list);

    }

    @Override
    protected void convert(BaseViewHolder helper, HomeAllDataModel item) {
        switch (item.getItemType()){
            case HomeAllDataModel.BANNER_HEAD:
                handleHeadBanner(helper, item);
                break;
            case HomeAllDataModel.CATEGORY_GOD:
                handleCategory(helper, item);
                break;

            case HomeAllDataModel.GOD_LIST:
                handleGoList(helper, item);
                break;
        }
    }

    private void handleGoList(BaseViewHolder helper, HomeAllDataModel item) {
        List<String> goList = (List<String>) item.getModel();
        TextView tv = helper.getView(R.id.god_list);
        tv.setText(goList.get(2));
    }

    private void handleCategory(BaseViewHolder helper, HomeAllDataModel item) {
        List<String> categoryList = (List<String>) item.getModel();
        TextView textView = helper.getView(R.id.category);
        textView.setText(categoryList.get(3));
    }

    private void handleHeadBanner(BaseViewHolder helper, HomeAllDataModel item) {
        List<String> banners = (List<String>) item.getModel();
        final AutoScrollViewPager mViewPager = helper.getView(R.id.autoViewPager);
        BaseViewPagerAdapter<String> viewPagerAdapter = new BaseViewPagerAdapter<String>(mContext, listener) {
            @Override
            public void loadImage(ImageView view, int position, String url) {
                ImageLoadUtils.displayImage(mContext,url,view,R.mipmap.message_red);
            }

            @Override
            public void setSubTitle(TextView textView, int position, String s) {

            }
        };
        mViewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.add(banners);
    }

    private BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.OnAutoViewPagerItemClickListener<String>() {

        @Override
        public void onItemClick(int position, String s) {
            Toast.makeText(mContext, "position="+position, Toast.LENGTH_SHORT).show();
        }
    };

}
