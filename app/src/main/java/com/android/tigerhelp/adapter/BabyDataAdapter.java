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
            boolean selected = item.isSelected();
            if(selected){
                helper.setImageResource(R.id.select_iv,R.mipmap.selected);
            }else{
                helper.setImageResource(R.id.select_iv,R.mipmap.un_select);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
