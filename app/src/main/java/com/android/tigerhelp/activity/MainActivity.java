package com.android.tigerhelp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.tigerhelp.R;
import com.android.tigerhelp.adapter.MainAdapter;
import com.android.tigerhelp.base.BaseActivity;
import com.android.tigerhelp.fragment.ActivityFragment;
import com.android.tigerhelp.fragment.ConsultFragment;
import com.android.tigerhelp.fragment.IndexFragment;
import com.android.tigerhelp.fragment.MessageFragment;
import com.android.tigerhelp.fragment.MyselfFragment;
import com.android.tigerhelp.ui.Tab;
import com.android.tigerhelp.ui.TabIndicator;
import com.android.tigerhelp.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements TabIndicator.OnTabClickListener {
    @Bind(R.id.fm_content)
    NoScrollViewPager viewPager;
    @Bind(R.id.llToolbarParent)
    RelativeLayout toolbarParent;

    public static final int INDEX = 0;
    public static final int ZIXUN = 1;
    public static final int HUODONG = 2;
    public static final int XIAOXI = 3;
    public static final int MYSELF = 4;

    private int currentTabIndex = 0;

    private List<Fragment> fragmentList;
    private ConsultFragment consultFragment;
    private ActivityFragment activityFragment;
    private MessageFragment messageFragment;
    private MyselfFragment myselfFragment;
    private IndexFragment indexFragment;

    private FragmentManager fragmentManager;
    private MainAdapter mainAdapter;
    private TabIndicator mMainIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setTitle("首页");
        toolbarParent.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        initViewPager();
    }

    @Override
    protected void getRemoteData() {

    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        indexFragment = IndexFragment.newInstance();
        consultFragment = ConsultFragment.newInstance();
        activityFragment = ActivityFragment.newInstance();
        messageFragment = MessageFragment.newInstance();
        myselfFragment = MyselfFragment.newInstance();
        fragmentList.add(indexFragment);
        fragmentList.add(consultFragment);
        fragmentList.add(activityFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(myselfFragment);

        fragmentManager = getSupportFragmentManager();
        mainAdapter = new MainAdapter(fragmentManager,fragmentList);
        viewPager.setAdapter(mainAdapter);
        viewPager.setNoScroll(true);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new PageListener());

        mMainIndicator = (TabIndicator) findViewById(R.id.mMainIndicator);
        mMainIndicator.setOnTabClickListener(this);

        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(R.drawable.main_tab_index_selector, "首页", null));
        tabs.add(new Tab(R.drawable.main_tab_zixun_selector, "咨询", null));
        tabs.add(new Tab(R.drawable.main_tab_huodong_selector, "活动", null));
        tabs.add(new Tab(R.drawable.main_tab_xiaoxi_selector, "消息", null));
        tabs.add(new Tab(R.drawable.main_tab_wode_selector, "我的", null));
        mMainIndicator.initializeData(tabs);
        mMainIndicator.setCurrentTab(0);
    }

    @Override
    public boolean onCheckClickable(int position) {
        return true;
    }

    @Override
    public void onTabClick(int position) {
        if (position == INDEX) {
            viewPager.setCurrentItem(INDEX, false);
        } else if (position == ZIXUN) {
            viewPager.setCurrentItem(ZIXUN, false);
        } else if (position == HUODONG) {
            viewPager.setCurrentItem(HUODONG, false);
        }else if(position == XIAOXI){
            viewPager.setCurrentItem(XIAOXI, false);
        } else if (position == MYSELF) {
            viewPager.setCurrentItem(MYSELF, false);
        }
    }

    public class PageListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            currentTabIndex = position;
            switch (currentTabIndex){
                case INDEX:
                    switchTitleName(position,true);
                    break;
                case ZIXUN:
                    switchTitleName(position,false);
                    break;
                case HUODONG:
                    switchTitleName(position,false);
                    break;
                case XIAOXI:
                    switchTitleName(position,false);
                    break;
                case MYSELF:
                    switchTitleName(position,false);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void switchTitleName(int currentTabIndex, boolean isHideToolBar){
        switch (currentTabIndex){
            case INDEX:
                if(isHideToolBar){
                    toolbarParent.setVisibility(View.GONE);
                }else{
                    toolbarParent.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("我的");
                break;
            case ZIXUN:
                if(isHideToolBar){
                    toolbarParent.setVisibility(View.GONE);
                }else {
                    toolbarParent.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("咨询");
                break;
            case HUODONG:
                if(isHideToolBar){
                    toolbarParent.setVisibility(View.GONE);
                }else{
                    toolbarParent.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("活动");
                break;
            case XIAOXI:
                if(isHideToolBar){
                    toolbarParent.setVisibility(View.GONE);
                }else{
                    toolbarParent.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("消息");
                break;
            case MYSELF:
                if(isHideToolBar){
                    toolbarParent.setVisibility(View.GONE);
                }else{
                    toolbarParent.setVisibility(View.VISIBLE);
                }
                tvTitle.setText("我的");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        clearFragments();
    }

    private void clearFragments() {
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (null != fragments && fragments.size() != 0) {
                fragments.clear();
            }
        }
    }

}
