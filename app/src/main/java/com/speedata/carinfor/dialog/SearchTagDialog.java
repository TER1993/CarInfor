package com.speedata.carinfor.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.speedata.carinfor.R;
import com.speedata.carinfor.application.CustomerApplication;
import com.speedata.carinfor.interfaces.DialogListener;
import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.bean.Tag_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张明_ on 2016/12/28.
 */

public class SearchTagDialog extends Dialog implements
        View.OnClickListener, AdapterView.OnItemClickListener {

    private Button Cancle;
    private Button Action;
    private TextView Status;
    private ListView EpcList;
    private boolean inSearch = false;
    private List<EpcDataBase> firm = new ArrayList<EpcDataBase>();
    private Handler handler = null;
    private ArrayAdapter<EpcDataBase> adapter;
    private Context cont;
    private SoundPool soundPool;
    private int soundId;
    private long scant = 0;
    private CheckBox cbb;
    private IUHFService iuhfService;


    private DialogListener mSListener = null;
    public void setOnSettingListener(DialogListener listener) {
        mSListener = listener;
    }


    public SearchTagDialog(Context context, IUHFService iuhfService) {
        super(context);
        // TODO Auto-generated constructor stub
        cont = context;
        this.iuhfService = iuhfService;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setreader);


//        iuhfService.SetInvMode(3, 0, 10);
        Cancle = (Button) findViewById(R.id.btn_search_cancle);
        Cancle.setOnClickListener(this);
        Action = (Button) findViewById(R.id.btn_search_action);
        Action.setOnClickListener(this);

        cbb = (CheckBox) findViewById(R.id.checkBox_beep);

        Status = (TextView) findViewById(R.id.textView_search_status);
        EpcList = (ListView) findViewById(R.id.listView_search_epclist);
        EpcList.setOnItemClickListener(this);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        if (soundPool == null) {
            Log.e("as3992", "Open sound failed");
        }
        soundId = soundPool.load("/system/media/audio/ui/VideoRecord.ogg", 0);
        Log.w("as3992_6C", "id is " + soundId);

        //inventory_start(handler) 方法参考代码

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    scant++;
                    if (!cbb.isChecked()) {
                        if (scant % 10 == 0) {
                            soundPool.play(soundId, 1, 1, 0, 0, 1);
                        }
                    }
                    ArrayList<Tag_Data> ks = (ArrayList<Tag_Data>) msg.obj;
                    int i, j;
                    for (i = 0; i < ks.size(); i++) {
                        for (j = 0; j < firm.size(); j++) {
                            if (ks.get(i).epc.equals(firm.get(j).epc)) {
                                firm.get(j).valid++;
                                firm.get(j).setRssi(ks.get(i).rssi);
                                break;
                            }
                        }
                        if (j == firm.size()) {
                            firm.add(new EpcDataBase(ks.get(i).epc, 1,
                                    ks.get(i).rssi,ks.get(i).tid ));
                            if (cbb.isChecked()) {
                                soundPool.play(soundId, 1, 1, 0, 0, 1);
                            }
                        }
                    }
                }
                adapter = new ArrayAdapter<EpcDataBase>(
                        cont, android.R.layout.simple_list_item_1, firm);
                EpcList.setAdapter(adapter);
                Status.setText("Total: " + firm.size());

            }
        };


    }


    @Override
    protected void onStop() {
        Log.w("stop", "im stopping");
        if (inSearch) {
            iuhfService.inventory_stop();
            inSearch = false;
        }
        soundPool.release();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (v == Cancle) {
            soundPool.release();
            firm.clear();
            dismiss();
        } else if (v == Action) {
            if (inSearch) {
                inSearch = false;
                this.setCancelable(true);
                int inventoryStop = iuhfService.inventory_stop();
                if (inventoryStop != 0) {
                    Toast.makeText(cont, "停止失败", Toast.LENGTH_SHORT).show();
                }

                Action.setText("开始");
                Cancle.setEnabled(true);
            } else {
                inSearch = true;
                this.setCancelable(false);
                scant = 0;
                iuhfService.inventory_start(handler);
                Action.setText("停止");
                Cancle.setEnabled(false);
            }
        }
    }

    class EpcDataBase {
        String epc;
        int valid;
        String rssi;
        String tid_user;

        public EpcDataBase(String e, int v, String rssi, String tid_user) {
            // TODO Auto-generated constructor stub
            epc = e;
            valid = v;
            this.rssi = rssi;
            this.tid_user = tid_user;
        }

        public String getRssi() {
            return rssi;
        }

        public void setRssi(String rssi) {
            this.rssi = rssi;
        }

        @Override
        public String toString() {
            if (TextUtils.isEmpty(tid_user)) {
                return "EPC:" + epc + "\n"
                        + "(" + "COUNT:" + valid + ")" + " RSSI:" + rssi+"\n";
            } else {
//                return "EPC:" + epc + "\n"
//                        + "T/U:" + tid_user + "\n"
//                        + "(" + "COUNT:" + valid + ")" + " RSSI:" + rssi+"\n";
                return "EPC:" + epc + "\n"
                        + "(" + "COUNT:" + valid + ")" + " RSSI:" + rssi+"\n";
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                            long arg3) {
        // TODO Auto-generated method stub
        if (inSearch) {
            return;
        }
        int res = iuhfService.select_card(1,firm.get(arg2).epc,true);
        if (res == 0) {

            Toast.makeText(cont, "选卡成功", Toast.LENGTH_SHORT).show();
            if (mSListener != null) {
                mSListener.onSetting(firm.get(arg2).epc);
            }
            CustomerApplication.getInstance().setEPC(firm.get(arg2).epc);
            dismiss();

        } else {
            Toast.makeText(cont, "选卡失败", Toast.LENGTH_SHORT).show();
        }
    }


}
