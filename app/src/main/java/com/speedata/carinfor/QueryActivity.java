package com.speedata.carinfor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

import com.speedata.carinfor.adapter.WriteAdapter;
import com.speedata.carinfor.application.CustomerApplication;
import com.speedata.carinfor.db.bean.BaseInfor;
import com.speedata.carinfor.db.dao.BaseInforDao;
import com.speedata.carinfor.dialog.SearchTagDialog;
import com.speedata.carinfor.interfaces.DialogListener;
import com.speedata.carinfor.utils.MyDateAndTime;
import com.speedata.libutils.excel.ExcelUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.format.Colour;
import win.reginer.adapter.CommonRvAdapter;

import static com.speedata.carinfor.application.CustomerApplication.iuhfService;

public class QueryActivity extends Activity implements View.OnClickListener, DialogListener, CommonRvAdapter.OnItemClickListener {
    private Button btnSearch; //寻卡选卡
    private Button btnBrandSearch; //查询品牌
    private Button btnInput; //录入内容

    private EditText etCardEPC; //卡片EPC

    private Spinner spBrand; //品牌
    private EditText etBrand; //输入品牌

    private List<BaseInfor> mList;
    private CustomerApplication application;
    private BaseInforDao baseInforDao;
    private Context mContext;
    protected TextView mBarTitle;
    protected ImageView mBarLeft;
    private AlertDialog mExitDialog; //按退出时弹出对话框

    private WriteAdapter mAdapter;
    private String TAG = "carinfor";
    private TextView tvTxt;
    //item控件点击显示
    private android.app.AlertDialog mDialogItem;
    /*
    品牌以及颜色的spinner列表文件
    */
    private String[] brandList;
    boolean firstbrand;
    private String mBrand;

    //输入法管理器
    protected InputMethodManager mimm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_query);
        initTitle();
        initView();
    }



    private void initTitle() {
        setNavigation(1, getString(R.string.count_title));
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
        btnBrandSearch = (Button) findViewById(R.id.btn_brand_search);
        etCardEPC = (EditText) findViewById(R.id.et_card_epc);
        etBrand = (EditText) findViewById(R.id.et_brand);

        spBrand = (Spinner) findViewById(R.id.spinner_brand);

        btnSearch.setOnClickListener(this);
        btnBrandSearch.setOnClickListener(this);
        btnInput.setOnClickListener(this);
        mBarLeft.setOnClickListener(this);

        application = (CustomerApplication) getApplication();

        application.setEPC("未选卡");

        mContext = QueryActivity.this;
        baseInforDao = new BaseInforDao(mContext);


        //准备spinner
        firstbrand = true;
        brandList = this.getResources().getStringArray(R.array.brand);


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

                        String input = mBrand;
                        if ("未选择".equals(input)) {
                            mList.clear();
                            mList.addAll(baseInforDao.imQueryList());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            search(input);
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


        etCardEPC.setText("未选卡");
        etBrand.setText("");


        mList = baseInforDao.imQueryList();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_one_content);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)); //adapter横线

        mAdapter = new WriteAdapter(this, R.layout.view_one_item_layout, mList);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);




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

                //导出excel文档
                outPutFile();

                break;

            case R.id.btn_brand_search:

                String input = etBrand.getText().toString();
                if ("".equals(input)) {
                    mList.clear();
                    mList.addAll(baseInforDao.imQueryList());
                    mAdapter.notifyDataSetChanged();
                } else {
                    search(input);
                }

                break;

            case R.id.iv_left:
                finish();
                break;

        }

    }

    private void outPutFile() {

        if (mList.size() == 0) {
            Toast.makeText(this, "当前没有可导出的数据，请添加数据", Toast.LENGTH_SHORT).show();
            return;
        }
        List<BaseInfor> resultList = new ArrayList<>();

        if ("卡号EPC".equals(mList.get(0).getACardEPC())) { //有头需要处理，无头不需要处理
                mList.remove(0);
        }

        resultList.addAll(mList); //要导出的内容


        //导出到文件, 自定义一个文件名
        try {
            String filename = createFilename();

            ExcelUtils.getInstance()
                    .setSHEET_NAME("sheet1")
                    .setFONT_COLOR(Colour.BLACK)
                    .setFONT_TIMES(8)
                    .setFONT_BOLD(false)
                    .setBACKGROND_COLOR(Colour.WHITE)
                    .setContent_list_Strings(resultList)
                    .setWirteExcelPath(filename)
                    .createExcel(this);
            Log.d("excel", resultList.toString());
            scanFile(this, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //创建导出文件的名字
    public String createFilename() throws IOException {
        String checktime = MyDateAndTime.getMakerDate();

        String date = checktime.substring(0, 8); //获得年月日
        String time = checktime.substring(8, 12); //获得年月日
        String name = "exportExcel_" + date + "_" + time;

        return  "/sdcard/" + name + ".xls";

    }



    @Override
    public void onSetting(String name) {
        etCardEPC.setText(name);
        search(name);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int position) {
        changeCount(position);
    }

    //显示点击item后的匹配信息
    private void changeCount(int position) {
        BaseInfor message = mList.get(position);

        //item的解决方案按钮
        if (("卡号EPC").equals(message.getACardEPC())) {
            return;
        }

        String show = "";
        show = showResult(message);

        //item的解决方案按钮
        DialogItemOnClickListener dialogButtonOnClickListener = new  DialogItemOnClickListener();
        tvTxt = new TextView(this);
        mDialogItem = new android.app.AlertDialog.Builder(this)
                .setTitle("显示匹配结果")
                .setView(tvTxt)
                .setPositiveButton("确定", dialogButtonOnClickListener)
                .show();

        tvTxt.append(show);
    }

    private String showResult(BaseInfor message) {
        String result = "";
        List<BaseInfor> list = new ArrayList<>();
        list = baseInforDao.imQueryList();
        BaseInfor baseInfor = list.get(0);

        Log.d(TAG, "数据库0位置数据" + baseInfor.toString());
        for (int i = 0; i < 7; i++) {
            result += quzhi(i, baseInfor) + " : " + quzhi(i, message) + "\n";
        }

        return result;
    }

    private String quzhi(int i, BaseInfor baseInfor) {
        String quzhi = "";
        switch (i) {
            case 0:
                quzhi = baseInfor.getACardEPC();
                quzhi = quzhi.replaceAll("\n", "");
                break;
            case 1:
                quzhi = baseInfor.getBFrameNumber();
                quzhi = quzhi.replaceAll("\n", "");
                break;
            case 2:
                quzhi = baseInfor.getCBrand();
                quzhi = quzhi.replaceAll("\n", "");
                break;
            case 3:
                quzhi = baseInfor.getDColor();
                quzhi = quzhi.replaceAll("\n", "");
                break;
            case 4:
                quzhi = baseInfor.getEKilometers();
                quzhi = quzhi.replaceAll("\n", "");
                break;
            case 5:
                quzhi = baseInfor.getFParkingLocation();
                quzhi = quzhi.replaceAll("\n", "");
                break;
            case 6:
                quzhi = baseInfor.getGApproachTime();
                quzhi = quzhi.replaceAll("\n", "");
                break;


        }
        return quzhi;
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


    /**
     * 显示信息退出时的对话框的按钮点击事件
     */
    private class DialogItemOnClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE: // 确定
                    // 取消显示对话框
                    mDialogItem.dismiss();
                    break;
            }
        }
    }


    private void search(String input) {
        Log.d(TAG, "开始查询");
        List<BaseInfor> baseInfors = baseInforDao.imQueryList("CBrand=?", new String[]{input});
        if (baseInfors.size() == 0) { //品牌没搜到，搜EPC
            baseInfors = baseInforDao.imQueryList("ACardEPC=?", new String[]{input});
            Log.d(TAG, "没搜到品牌");
            if (baseInfors.size() == 0) { //没搜到结果
                Toast.makeText(this, "没有搜索到匹配的结果，请确认搜索内容是否正确", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "没搜到EPC");
            } else { //搜到匹配的EPC结果
                mList.clear();
                mList.addAll(baseInfors);
                Log.d(TAG, "搜到EPC" + mList.toString());
            }
        } else { //搜到匹配的品牌结果
            mList.clear();
            mList.addAll(baseInfors);
            Log.d(TAG, "搜到品牌" + mList.toString());
        }
        Log.d(TAG, "结束查询");

        mAdapter.notifyDataSetChanged();
    }

    //更新文件显示的广播，在生成文件后调用一次。
    public static void scanFile(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
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
