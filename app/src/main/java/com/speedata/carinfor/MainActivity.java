package com.speedata.carinfor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.speedata.carinfor.application.CustomerApplication;
import com.speedata.carinfor.db.bean.BaseInfor;
import com.speedata.carinfor.db.dao.BaseInforDao;
import com.speedata.utils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private AlertDialog mDialog;
    private Context mContext;
    private EditText etTxtName;
    private BaseInforDao baseInforDao;
    private BaseInfor baseInfor;
    private List<BaseInfor> mlist;
    private List<BaseInfor> daolist;
    private CustomerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }

    private void initView() {
        mContext = MainActivity.this;
        baseInforDao = new BaseInforDao(mContext);
        findViewById(R.id.iv_write).setOnClickListener(this);
        findViewById(R.id.iv_check).setOnClickListener(this);
        findViewById(R.id.btn_import).setOnClickListener(this); //原导入导出按钮
        findViewById(R.id.btn_clear).setOnClickListener(this); //原导入导出按钮
        baseInfor = new BaseInfor();
        mlist = new ArrayList<>();
        daolist = new ArrayList<>();
        mlist = baseInforDao.imQueryList();
        application = (CustomerApplication) getApplication();
        application.setList(mlist);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_write:
                ProgressDialogUtils.dismissProgressDialog();
                ProgressDialogUtils.showProgressDialog(mContext, "正在进入信息录入页面...");
                Intent intent1 = new Intent(MainActivity.this, InforActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_check:
                ProgressDialogUtils.dismissProgressDialog();
                ProgressDialogUtils.showProgressDialog(mContext, "正在进入信息查询页面...");
                Intent intent2 = new Intent(MainActivity.this, QueryActivity.class);
                startActivity(intent2);
                break;

        }
    }



}
