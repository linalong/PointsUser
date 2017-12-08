package com.heizi.pointsuser.block.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heizi.mycommon.utils.SystemUtil;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leo on 17/12/5.
 */

public class ActivityFeedback extends BaseSwipeBackCompatActivity {

    EditText et_remark;//反馈内容
    EditText et_tel;//填写电话
    EditText et_email;//填写邮箱
    Button btn_ok;
    LinearLayout ll_tel;
    TextView tv_tel;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_feedback;
    }


    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("意见反馈");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("常见问题");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ActivityFeedback.this, ActivityFAQ.class, null);
            }
        });
        et_remark = (EditText) findViewById(R.id.et_remark);
        et_tel = (EditText) findViewById(R.id.et_tel);
        et_email = (EditText) findViewById(R.id.et_email);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        ll_tel = (LinearLayout) findViewById(R.id.ll_tel);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        tv_tel.setText("TEL:" + userModel.getService_phone());

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_remark.getText().toString())) {
                    Utils.toastShow(ActivityFeedback.this, "请输入意见");
                } else {
                    postData();
                }
            }
        });

        ll_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtil.call(ActivityFeedback.this, userModel.getService_phone());
            }
        });
    }

    private void postData() {
        ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + feedback);
        Map<String, String> map = new HashMap<>();
        map.put("token", userModel.getToken());
        map.put("content", et_remark.getText().toString());
        map.put("phone", et_tel.getText().toString());
        map.put("email", et_email.getText().toString());
        parseStringProtocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    Utils.toastShow(ActivityFeedback.this, "感谢您的宝贵意见");
                    finish();
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    Utils.toastShow(ActivityFeedback.this, errorModel.getMsg());
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
                mDialog.showDialog();
            }
        });
    }


}
