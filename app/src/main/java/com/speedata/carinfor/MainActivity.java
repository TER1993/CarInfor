package com.speedata.carinfor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

    private long firstTime = 0;


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

        //查一下数据库，没信息就填一下表头

        List<BaseInfor> baseInforList = new ArrayList<>();
        baseInforList = baseInforDao.imQueryList();
        if (baseInforList.size() == 0) { //无内容则添加表头
            BaseInfor baseInfor = new BaseInfor();
            baseInfor.setACardEPC("卡号EPC");
            baseInfor.setBFrameNumber("车架号");
            baseInfor.setCBrand("品牌");
            baseInfor.setDColor("颜色");
            baseInfor.setEKilometers("公里数");
            baseInfor.setFParkingLocation("存车位置");
            baseInfor.setGApproachTime("进场时间");
            baseInforDao.imInsert(baseInfor);
        }


        // 创建退出时的对话框，此处根据需要显示的先后顺序决定按钮应该使用Neutral、Negative或Positive
        DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
        mDialog = new AlertDialog.Builder(this)
                .setTitle("清空数据库")
                .setMessage("确定要清空数据库所有数据？")
                .setCancelable(false)
                .setNeutralButton("确定", dialogButtonOnClickListener)
                .setPositiveButton(R.string.out_miss, dialogButtonOnClickListener)
                .create();

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

            case R.id.btn_clear:

               mDialog.show();

                break;

        }
    }

    /**
     * 退出时的对话框的按钮点击事件
     */
    private class DialogButtonOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: // 取消
                    // 取消显示对话框
                    mDialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEUTRAL: // 清空数据库

                    baseInforDao.imDeleteAll();

                    break;
            }
        }
    }


    @Override
    protected void onResume() {
        ProgressDialogUtils.dismissProgressDialog();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.onTerminate();
        System.exit(0);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断是否按下“BACK”(返回)键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 弹出退出时的对话框
            //finish();
            // 返回true以表示消费事件，避免按默认的方式处理“BACK”键的事件
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime; //更新firstTime
                    return true;
                } else {                    //两次按键小于2秒时，退出应用
                    finish();
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}
