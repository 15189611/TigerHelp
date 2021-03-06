package com.android.tigerhelp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tigerhelp.base.BaseFragment;

/**
 * Created by Charles on 2016/12/21.
 */

public class ConsultFragment extends BaseFragment {

    private static final String TAG = ConsultFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView  = new TextView(getActivity());
        textView.setText(TAG);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    protected void fetchObjectData() {

    }

    public static ConsultFragment newInstance(){
        ConsultFragment consultFragment = new ConsultFragment();
        Bundle bundle = new Bundle();
        consultFragment.setArguments(bundle);
        return consultFragment;
    }
}
