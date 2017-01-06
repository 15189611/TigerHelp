package com.android.tigerhelp.adapter;

import com.amap.api.services.core.PoiItem;
import com.android.tigerhelp.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by huangTing on 2017/1/6.
 */

public class MapAroundAdapter extends BaseQuickAdapter<PoiItem> {

    public MapAroundAdapter(List<PoiItem> data) {
        super(R.layout.map_around_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, PoiItem item) {
        try{
            String addressDetails = item.getCityName() + item.getAdName() + item.getSnippet();
            holder.setText(R.id.address_title,item.getTitle()).setText(R.id.address_details,addressDetails);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
