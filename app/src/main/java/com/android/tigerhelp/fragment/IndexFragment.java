package com.android.tigerhelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.tigerhelp.R;
import com.android.tigerhelp.adapter.HomeAdapter;
import com.android.tigerhelp.base.BaseFragment;
import com.android.tigerhelp.db.ShareUtils;
import com.android.tigerhelp.entity.HomeAllDataModel;
import com.android.tigerhelp.recyclerview.LayoutManager.ChLinearLayoutManager;
import com.android.tigerhelp.recyclerview.Listener.LoadDataListener;
import com.android.tigerhelp.recyclerview.View.PullRefreshRecycleView;
import com.android.tigerhelp.util.BussinessUtil;
import com.android.tigerhelp.util.MD5;

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

    @Bind(R.id.viewTitleBarBg)
    View viewTitleBarBg;

    private List<HomeAllDataModel> homeAllDatas = new ArrayList<>();

    private List<String> bannerDatas = new ArrayList<>();
    private List<String> catoryDatas = new ArrayList<>();
    private List<String> godListDatas = new ArrayList<>();

    private HomeAdapter homeAdapter ;
    private ChLinearLayoutManager layoutManager;

    private boolean isScrollIdle = true; // 是否在滑动
    private View itemTopView;
    private int topViewTop;
    private int topViewHeight;

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
        layoutManager = new ChLinearLayoutManager(getActivity());
        refreshRecycleView.setLayoutManager(layoutManager);
        refreshRecycleView.setNoLoadMore(true);
        refreshRecycleView.setAdapter(homeAdapter);
    }

    private void initView() {
        refreshRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrollIdle = (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (itemTopView == null) {
                    itemTopView = refreshRecycleView.getChildAt(1 - layoutManager.findFirstVisibleItemPosition());
                }
                if (itemTopView != null) {
                    topViewTop = itemTopView.getTop();
                    topViewHeight = itemTopView.getHeight();
                }
                if ((isScrollIdle && topViewTop < 0)) {
                    return;
                }
                setTitleBarAlpha();
            }
        });

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
        String userId = (String) ShareUtils.get(getActivity(), "userId");
        String token = (String) ShareUtils.get(getActivity(), "token");
        if(BussinessUtil.isValid(userId) && BussinessUtil.isValid(token)){
            String sign = MD5.getMD5Str(String.valueOf(System.currentTimeMillis() + userId + token + userId));
        }

    }

    // 处理标题栏颜色渐变
    private void setTitleBarAlpha() {
        float titleBarAlpha;
        float space = Math.abs(topViewTop) * 1f;
        float moveMaxHeight = topViewHeight - rlTitle.getHeight();

        if (topViewTop >= 0 || moveMaxHeight <= 0) {
            titleBarAlpha = 0f;
        } else {
            float titleRate = space / moveMaxHeight;
            titleBarAlpha = getTitleBarAlpha(titleRate);
        }

        viewTitleBarBg.setAlpha(titleBarAlpha);

        if (titleBarAlpha == 0f) {
        } else if (titleBarAlpha == 1f) {
            rlTitle.setVisibility(View.VISIBLE);
        }
    }

    private float getTitleBarAlpha(float rate) {
        if (rate < 0.01f) {
            rate = 0f;
        } else if (rate >= 1f) {
            rate = 1f;
        }
        return rate;
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
