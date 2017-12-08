package com.heizi.pointsuser.block.my;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by leo on 17/9/26.
 */

public class ActivityGivePoint extends BaseSwipeBackCompatActivity implements View.OnClickListener {

    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_point)
    EditText et_point;
    @InjectView(R.id.et_pwd)
    EditText et_pwd;

    @InjectView(R.id.tv_point)
    TextView tv_point;

    @InjectView(R.id.btn_ok)
    Button btn_ok;

    private int points = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_givepoint;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("积分转赠");
        getUser();
    }

    private void postData() {
        ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + GIVE_POINT);
        Map<String, String> map = new HashMap<>();
        map.put("token", userModel.getToken());
        map.put("tomobile", et_phone.getText().toString());
        map.put("amount", et_point.getText().toString());
        map.put("remark", "");
        map.put("password", et_pwd.getText().toString());
        parseStringProtocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    Utils.toastShow(ActivityGivePoint.this, "转赠成功");
                    finish();
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    Utils.toastShow(ActivityGivePoint.this, errorModel.getMsg());
                    mDialog.hideDialog();
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
                mDialog.showDialog();

            }
        });
    }

    private void getUser() {
        ParseStringProtocol protocol = new ParseStringProtocol(this
                , SERVER_URL_NEW + MY_ACCOUNT);
        Map<String, String> map = new HashMap<>();
        map.put("token", userModel.getToken());
        protocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                try {
                    JSONObject jsonObject = new JSONObject(data.json);
                    points = jsonObject.getInt("integral_balance");
                    tv_point.setText("账户当前剩余" + points + "积分");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                Utils.toastShow(ActivityGivePoint.this, "获取积分余额失败");
            }

            @Override
            public void onStart() {

            }
        });
    }

    @OnClick({R.id.btn_back, R.id.btn_ok})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_ok:
                if (et_phone.getText().toString().trim().equals("")) {
                    Utils.toastShow(ActivityGivePoint.this, "请输入要转赠的手机帐号");
                } else if (!Utils.checkIsCellphone(et_phone.getText().toString().trim())) {
                    Utils.toastShow(ActivityGivePoint.this, "请输入正确的手机号");
                } else if (et_point.getText().toString().trim().equals("") || !Utils.isNumeric(et_point.getText().toString().trim()) && Integer.parseInt(et_point.getText().toString().trim()) > 0) {
                    Utils.toastShow(ActivityGivePoint.this, "转赠数量应为大于0的数字");
                } else if (et_pwd.getText().toString().trim().equals("")) {
                    Utils.toastShow(ActivityGivePoint.this, "请输入登录密码");
                } else {
                    postData();
                }
                break;
        }
    }
}
