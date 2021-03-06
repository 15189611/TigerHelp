package com.android.tigerhelp.recyclerview.LayoutManager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.android.tigerhelp.recyclerview.Listener.OverScrollListener;


/**
 * Created by Charles
 * <br/>
 * 增加了{@link OverScrollListener}的LinearLayoutManager
 */
public class ChLinearLayoutManager extends LinearLayoutManager {

    private OverScrollListener mListener;

    public ChLinearLayoutManager(Context context) {
        super(context);
    }

    public ChLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ChLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
        if(mListener != null){
            mListener.overScrollBy(dy - scrollRange);
        }

        return scrollRange;
    }

    /**
     * 设置滑动过度监听
     *
     * @param listener
     */
    public void setOverScrollListener(OverScrollListener listener) {
        mListener = listener;
    }

}
