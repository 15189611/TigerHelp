package com.android.tigerhelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.tigerhelp.R;
import com.android.tigerhelp.adapter.HomeAdapter;
import com.android.tigerhelp.base.BaseFragment;
import com.android.tigerhelp.entity.HomeAllDataModel;
import com.android.tigerhelp.recyclerview.LayoutManager.ChLinearLayoutManager;
import com.android.tigerhelp.recyclerview.Listener.LoadDataListener;
import com.android.tigerhelp.recyclerview.View.PullRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.android.tigerhelp.R.id.recyclerIndex;

/**
 * Created by Charles on 2016/12/21.
 */

public class IndexFragment extends BaseFragment {

    private static final String TAG = IndexFragment.class.getSimpleName();
    private View rootView;

    @Bind(R.id.rlTitle)
    RelativeLayout rlTitle;
    @Bind(recyclerIndex)
    PullRefreshRecycleView refreshRecycleView;
    @Bind(R.id.ivScan)
    ImageView ivScan;

    private List<HomeAllDataModel> homeAllDatas = new ArrayList<>();

    private List<String> bannerDatas = new ArrayList<>();
    private List<String> catoryDatas = new ArrayList<>();
    private List<String> godListDatas = new ArrayList<>();

    private HomeAdapter homeAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_index, null);
        ButterKnife.bind(this,rootView);
        initView();
        initData();
        return rootView;
    }

    private void initData() {
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        bannerDatas.add("https://yppphoto.yupaopao.cn/upload/f63a7eb5-8543-41a3-8c39-9c868ecdcfee.jpg");
        for (int i = 0; i <= 15;i++){
            catoryDatas.add(i+"");
            godListDatas.add(i+"");
        }

        HomeAllDataModel<List<String>> banners = new HomeAllDataModel<>();
        HomeAllDataModel<List<String>> catoryDatas = new HomeAllDataModel<>();
        HomeAllDataModel<List<String>> godListDatas = new HomeAllDataModel<>();
        catoryDatas.setItemType(HomeAllDataModel.CATEGORY_GOD);
        catoryDatas.setModel(bannerDatas);
        godListDatas.setItemType(HomeAllDataModel.GOD_LIST);
        godListDatas.setModel(bannerDatas);
        banners.setItemType(HomeAllDataModel.BANNER_HEAD);
        banners.setModel(bannerDatas);

        homeAllDatas.add(0,banners);
        homeAllDatas.add(1,catoryDatas);
        homeAllDatas.add(2,godListDatas);

        homeAdapter = new HomeAdapter(getActivity(),homeAllDatas);
        ChLinearLayoutManager layoutManager = new ChLinearLayoutManager(getActivity());
        refreshRecycleView.setLayoutManager(layoutManager);
        refreshRecycleView.setNoLoadMore(true);
        refreshRecycleView.setAdapter(homeAdapter);
    }

    private void initView() {
        refreshRecycleView.setLoadDataListener(new LoadDataListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    @Override
    protected void fetchObjectData() {
    }

    
    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public static IndexFragment newInstance(){
        IndexFragment indexFragment = new IndexFragment();
        Bundle bundle = new Bundle();
        indexFragment.setArguments(bundle);
        return indexFragment;
    }

}
