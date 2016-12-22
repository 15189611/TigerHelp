package com.android.tigerhelp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.tigerhelp.util.Logger;


/**
 * Created by Charles on 2016/10/10.
 */

public abstract class BaseFragment extends Fragment {
    private final String TAG = this.getClass().getName();
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareToFetchData();
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) super.getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareToFetchData();
    }

    protected boolean  prepareToFetchData(){
        if(getBaseActivity() != null && !getBaseActivity().isFinishing() && isVisibleToUser && !isDataInitiated){
            fetchObjectData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("FragmentName=" + TAG);
    }

    /**
     * 该方法当fragment第一次显示可见时调用,加载网络数据调用
     */
    protected abstract void fetchObjectData();

}
