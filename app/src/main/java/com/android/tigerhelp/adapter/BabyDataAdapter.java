package com.android.tigerhelp.adapter;

import com.android.tigerhelp.R;
import com.android.tigerhelp.entity.RelationBabyItemModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by huangTing on 2016/12/27.
 */

public class BabyDataAdapter extends BaseQuickAdapter<RelationBabyItemModel> {

    public BabyDataAdapter( List<RelationBabyItemModel> data) {
        super(R.layout.select_value_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RelationBabyItemModel item) {
        try {
            helper.setText(R.id.item_name,item.getName());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
