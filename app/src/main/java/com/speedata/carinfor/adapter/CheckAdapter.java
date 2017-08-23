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

public class CheckAdapter extends CommonRvAdapter<BaseInfor> {
    private List<BaseInfor> mList;

    public CheckAdapter(Context context, int layoutResId, List<BaseInfor> data) {
        super(context, layoutResId, data);
        mList = data;
    }

    @Override
    public void convert(BaseAdapterHelper helper, BaseInfor item, int position) {
        helper.setText(R.id.tv_count_line1, item.getACardEPC());
        helper.setText(R.id.tv_count_line2, item.getBFrameNumber());
        setOnItemChildClickListener(helper, position, R.id.tv_count_line2);

    }
}
