package com.speedata.carinfor.adapter;

import android.content.Context;

import com.speedata.carinfor.R;
import com.speedata.carinfor.db.bean.BaseInfor;

import java.util.List;

import win.reginer.adapter.BaseAdapterHelper;
import win.reginer.adapter.CommonRvAdapter;


/**
 * Created by xu on 2017/8/14.
 */

public class WriteAdapter extends CommonRvAdapter<BaseInfor> {
    private List<BaseInfor> mList;

    public WriteAdapter(Context context, int layoutResId, List<BaseInfor> data) {
        super(context, layoutResId, data);
        mList = data;
    }


    @Override
    public void convert(BaseAdapterHelper helper, BaseInfor item, int position) {
        helper.setText(R.id.tv_one_line1, item.getCardEPC());
        helper.setText(R.id.tv_one_line2, item.getFrameNumber());
        helper.setText(R.id.tv_one_line3, item.getBrand());
        helper.setText(R.id.tv_one_line4, item.getColor());

    }
}
