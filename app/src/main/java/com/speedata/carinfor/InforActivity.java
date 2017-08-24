package com.speedata.carinfor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.speedata.carinfor.application.CustomerApplication;
import com.speedata.carinfor.db.bean.BaseInfor;
import com.speedata.carinfor.db.dao.BaseInforDao;
import com.speedata.carinfor.dialog.SearchTagDialog;
import com.speedata.carinfor.interfaces.DialogListener;
import com.speedata.carinfor.utils.MyDateAndTime;

import java.util.ArrayList;
import java.util.List;

import static com.speedata.carinfor.application.CustomerApplication.iuhfService;

public class InforActivity extends Activity implements View.OnClickListener, DialogListener {

    private Button btnSearch; //寻卡选卡
    private Button btnInput; //录入内容
    private EditText etCardEPC; //卡片EPC
    private EditText etFrameNumber; //车架号

    private Spinner spBrand; //品牌
    private EditText etBrand; //输入品牌

    private Spinner spColor; //颜色
    private EditText etColor; //输入颜色

    private EditText etKilometers; //公里数
    private EditText etParkingLocation; //存车位置
    private EditText etApproachTime; //进场时间

    private List<BaseInfor> mList;
    private CustomerApplication application;
    private BaseInforDao baseInforDao;
    private Context mContext;
    protected TextView mBarTitle;
    protected ImageView mBarLeft;
    private AlertDialog mExitDialog; //按退出时弹出对话框

    /*
    品牌以及颜色的spinner列表文件
    */
    private String[] brandList;
    boolean firstbrand;
    private String mBrand;

    private String[] colorList;
    boolean firstcolor;
    private String mColor;


    //输入法管理器
    protected InputMethodManager mimm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_infor);
        initTitle();
        initView();
    }

    private void initTitle() {
        setNavigation(1, getString(R.string.one_title));
    }

    /**
     * 这是导航
     *
     * @param left  左侧图标
     * @param title 标题
     */
    protected void setNavigation(int left, String title) {
        mBarTitle = (TextView) findViewById(R.id.tv_bar_title);
        mBarLeft = (ImageView) findViewById(R.id.iv_left);
        if (!TextUtils.isEmpty(title)) {
            mBarTitle.setText(title);
        }
        mBarLeft.setVisibility(left == 0 ? View.GONE : View.VISIBLE);
    }


    private void initView() {

        //输入法管理
        mimm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mList = new ArrayList<>();
        btnInput = (Button) findViewById(R.id.btn_input);

        btnSearch = (Button) findViewById(R.id.btn_search);
        etCardEPC = (EditText) findViewById(R.id.et_card_epc);
        etFrameNumber = (EditText) findViewById(R.id.et_frame_number);
        etBrand = (EditText) findViewById(R.id.et_brand);
        etColor = (EditText) findViewById(R.id.et_color);
        etKilometers = (EditText) findViewById(R.id.et_kilometers);
        etParkingLocation = (EditText) findViewById(R.id.et_parking_location);
        etApproachTime = (EditText) findViewById(R.id.et_approach_time);

        spBrand = (Spinner) findViewById(R.id.spinner_brand);
        spColor = (Spinner) findViewById(R.id.spinner_color);

        btnSearch.setOnClickListener(this);
        btnInput.setOnClickListener(this);
        mBarLeft.setOnClickListener(this);

        application = (CustomerApplication) getApplication();

        application.setEPC("未选卡");

        mList = application.getList();
        mContext = InforActivity.this;
        baseInforDao = new BaseInforDao(mContext);

        //准备spinner

        firstbrand = true;
        firstcolor = true;

        brandList = this.getResources().getStringArray(R.array.brand);
        colorList = this.getResources().getStringArray(R.array.color);


        //各个spinner初始化
        //brand
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, brandList);
        brandAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBrand.setAdapter(brandAdapter);

        spBrand
                .setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        arg0.setVisibility(View.VISIBLE);

                        if (firstbrand) {
                            firstbrand = false;
                            return;
                        }
                        //count就是结果要大于等于的数字
                        mBrand = spBrand
                                .getSelectedItem().toString();

                        Toast.makeText(mContext, "选择成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


        //color
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, colorList);
        colorAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColor.setAdapter(colorAdapter);

        spColor
                .setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int position, long id) {
                        arg0.setVisibility(View.VISIBLE);

                        if (firstcolor) {
                            firstcolor = false;
                            return;
                        }
                        //count就是结果要大于等于的数字
                        mColor = spColor
                                .getSelectedItem().toString();

                        Toast.makeText(mContext, "选择成功", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

        //时间说明
        etApproachTime.setText("自动写入当前系统时间");
        etCardEPC.setText("未选卡");
        etBrand.setText("");
        etColor.setText("");
        etKilometers.setText("");
        etParkingLocation.setText("");


        // 创建退出时的对话框，此处根据需要显示的先后顺序决定按钮应该使用Neutral、Negative或Positive
        DialogButtonOnClickListener dialogButtonOnClickListener = new DialogButtonOnClickListener();
        mExitDialog = new AlertDialog.Builder(this)
                .setTitle("退出此页面")
                .setMessage(R.string.out_message)
                .setCancelable(false)
                .setNeutralButton(R.string.out_sure, dialogButtonOnClickListener)
                .setPositiveButton(R.string.out_miss, dialogButtonOnClickListener)
                .create();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:

                //盘点选卡
                SearchTagDialog searchTag = new SearchTagDialog(this, iuhfService);
                searchTag.setTitle("选卡");
                searchTag.setOnSettingListener(this);
                searchTag.show();

                break;

            case R.id.btn_input:

                writeInfor();

                break;

            case R.id.iv_left:
                finish();
                break;
        }
    }

    private void writeInfor() {
        //向数据库写入数据

        List<BaseInfor> baseInfors = new ArrayList<>();
        BaseInfor baseInfor = new BaseInfor();

        if ("未选卡".equals(etCardEPC.getText().toString())) {
            Toast.makeText(this, "请先选择一个卡EPC再写入数据", Toast.LENGTH_SHORT).show();
            return;
        }

        baseInfor.setACardEPC(application.getEPC());
        baseInfor.setBFrameNumber(etFrameNumber.getText().toString());
        String brand = etBrand.getText().toString();
        if ("".equals(brand)) {
            baseInfor.setCBrand(mBrand);
        } else {
            baseInfor.setCBrand(etBrand.getText().toString());
        }
        String color = etColor.getText().toString();
        if ("".equals(color)) {
            baseInfor.setDColor(mColor);
        } else {
            baseInfor.setDColor(etColor.getText().toString());
        }
        String kilometers = etKilometers.getText().toString();
        baseInfor.setEKilometers(kilometers);
        String parkinglocation = etParkingLocation.getText().toString();
        baseInfor.setFParkingLocation(parkinglocation);
        baseInfor.setGApproachTime(MyDateAndTime.getTimeString());

        baseInfors.add(baseInfor);

        baseInforDao.imDelete("ACardEPC=?", new String[]{baseInfor.getACardEPC()});
        baseInforDao.imInsert(baseInfor);

        Toast.makeText(this, "信息写入成功", Toast.LENGTH_SHORT).show();

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
                    mExitDialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEUTRAL: // 退出程序
                    // 结束，将导致onDestroy()方法被回调

                    finish();
                    break;
            }
        }
    }


    @Override
    public void onSetting(String name) {
        // TODO Auto-generated method stub
        etCardEPC.setText(name);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断是否按下“BACK”(返回)键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 弹出退出时的对话框
            mExitDialog.show();
            // 返回true以表示消费事件，避免按默认的方式处理“BACK”键的事件
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
