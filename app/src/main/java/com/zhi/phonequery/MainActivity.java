package com.zhi.phonequery;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.zhi.service.QueryService;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final int MESSAGE_SUCCESS = 0x1;
    private static final int MESSAGE_FAIL = 0x2;

    private String phoneNumber;
    private String result;

    private EditText mEtPhone;
    private Button mBtnQuery;
    private TextView mTvResult;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_SUCCESS:
                    mTvResult.setText(result);
                    break;
                case MESSAGE_FAIL:
                    Toast.makeText(MainActivity.this, R.string.str_query_fail, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mBtnQuery.setOnClickListener(this);
    }

    private void initViews() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mBtnQuery = (Button) findViewById(R.id.btn_query);
        mTvResult = (TextView) findViewById(R.id.tv_result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                query();
                break;
        }
    }

    private void query() {
        Editable phone = mEtPhone.getText();
        if (null == phone || (null != phone && "".equals(phone.toString().trim()))) {
            Toast.makeText(MainActivity.this, R.string.str_query_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        phoneNumber = phone.toString();
        new Thread() {
            @Override
            public void run() {
                try {
                    result = QueryService.query(MainActivity.this, phoneNumber);
                    mHandler.sendEmptyMessage(MESSAGE_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MESSAGE_FAIL);
                }
            }
        }.start();
    }
}