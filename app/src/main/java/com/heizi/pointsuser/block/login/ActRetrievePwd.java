package com.heizi.pointsuser.block.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.heizi.mycommon.utils.LoadingD;
import com.heizi.mycommon.utils.SharePreferenceUtil;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mycommon.utils.UtilsLog;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseObjectProtocol;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.UserModel;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;
import com.heizi.pointsuser.activity.MainTabsActivity;
import com.heizi.pointsuser.utils.RefreshUtils;

import org.xutils.common.util.MD5;

import java.util.HashMap;
import java.util.Map;


/**
 * 找回密码
 *
 * @author Administrator
 */
public class ActRetrievePwd extends BaseSwipeBackCompatActivity implements
        OnClickListener, Constants {
    private Button btn_ok, btn_get_code;
    private EditText et_phone, et_verification_code, et_pwd;
    private TextView tv_title;
    private long leftTime = 0;
    private long closeTime = 0L;
    private SharePreferenceUtil su;
    private MyCount mc;
    private ParseStringProtocol gcp;// 获取验证码接口
    private IResponseCallback<DataSourceModel<String>> gcpcb;
    //注册接口
    private ParseObjectProtocol rp;
    private IResponseCallback<DataSourceModel<UserModel>> rpcb;
    private Handler mHandler;

    @Override
    protected int getLayoutResource() {
        return R.layout.act_retrieve_pwd;
    }


    @Override
    protected void initView() {
        super.initView();
        su = new SharePreferenceUtil(this);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_get_code = (Button) findViewById(R.id.btn_get_code);
        btn_get_code.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
//				if (Utils.checkIsCellphone(et_phone.getText().toString())
//						&& btn_get_code.getText().toString()
//								.equals("获取验证码")) {
//					btn_get_code.setEnabled(true);
//				} else {
//					btn_get_code.setEnabled(false);
//				}
            }
        });
        et_verification_code = (EditText)
                findViewById(R.id.et_verification_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("找回密码");

        mHandler = new Handler(this.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {

                startActivity(new Intent(ActRetrievePwd.this, MainTabsActivity.class));
                ActRetrievePwd.this.finish();
            }

            ;
        };
    }

    // 初始化数据

    @Override
    protected void initData() {
        super.initData();
        rp = new ParseObjectProtocol(this, SERVER_URL_NEW + RETRIEVIEPWD, UserModel.class);
        rpcb = new IResponseCallback<DataSourceModel<UserModel>>() {

            @Override
            public void onSuccess(DataSourceModel<UserModel> t) {
                // TODO Auto-generated method stub
                LoadingD.hideDialog();
                try {
                    userModel = t.temp;
                    userModel.setPassword(et_pwd.getText().toString());
                    if (!TextUtils.isEmpty(userModel.getId())) {
                        SaveThread saveThread = new SaveThread();
                        //saveThread.msg = MSG_TOMAIN;
                        saveThread.start();
                    }
                    Utils.toastShow(ActRetrievePwd.this, "密码成功找回!");

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                LoadingD.showDialog(ActRetrievePwd.this);
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                // TODO Auto-generated method stub
                LoadingD.hideDialog();
                Utils.toastShow(ActRetrievePwd.this, errorModel.getMsg());
            }
        };


        gcp = new ParseStringProtocol(this, SERVER_URL_NEW + SENDSMS);
        gcpcb = new IResponseCallback<DataSourceModel<String>>() {

            @Override
            public void onSuccess(DataSourceModel<String> t) {
                // TODO Auto-generated method stub
                LoadingD.hideDialog();
                Utils.toastShow(ActRetrievePwd.this, "发送成功");
                if (mc != null)
                    mc.cancel();
                mc = new MyCount(60 * 1000, 1000); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
                mc.start();
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                // TODO Auto-generated method stub
                LoadingD.hideDialog();
                UtilsLog.d("====", "code==" + errorModel.getCode());
                Utils.toastShow(ActRetrievePwd.this, "" + errorModel.getMsg());
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                LoadingD.showDialog(ActRetrievePwd.this);
            }
        };
    }

    // 注册
    private void initRegister() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("mobile", et_phone.getText().toString());
        maps.put("password",et_pwd.getText().toString());
        maps.put("code", et_verification_code.getText().toString());
        rp.postData(maps, rpcb);
    }

    // 获取验证码
    private void getCode() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("mobile", et_phone.getText().toString());
        gcp.postData(maps, gcpcb);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.btn_ok:
                if (checkData()) {
                    initRegister();
                }

                break;
            case R.id.btn_get_code:
                if (Utils.isNull(et_phone.getText().toString())
                        || !Utils.checkIsCellphone(et_phone.getText().toString())) {
                    Utils.toastShow(this, "请输入正确的手机号码!");
                    return;
                }
                getCode();
                break;

            case R.id.btn_back:
                finish();
            default:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //恢复验证码相关状态
        leftTime = su.getRegisterLeftTiming();
        closeTime = su.getRegisterTime();
        if (leftTime < 3)
            return;

        long curT = System.currentTimeMillis();
        if (closeTime != 0 && curT - closeTime >= leftTime * 1000) {

        } else {
            mc = new MyCount(leftTime * 1000 - (curT - closeTime), 1000);
            mc.start();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
//		if (leftTime < 3) {
        su.setRegisterLeftTiming(0);
        su.setRegisterTime(0);
//		} else {
//			su.setRegisterLeftTiming(leftTime);
//			closeTime = System.currentTimeMillis();
//			su.setRegisterTime(closeTime);
        if (mc != null)
            mc.cancel();
//		}
    }

    // 倒计时
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_get_code.setEnabled(true);
            btn_get_code.setText("获取验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_get_code.setEnabled(false);
            btn_get_code.setText(String.format(
                    getString(R.string.register_countdown),
                    millisUntilFinished / 1000));
            leftTime = millisUntilFinished / 1000;
        }
    }

    // 验证输入信息是否正确
    private boolean checkData() {
        if (Utils.isNull(et_phone.getText().toString())
                || !Utils.checkIsCellphone(et_phone.getText().toString())) {
            Utils.toastShow(this, "请输入正确的手机号!");
            return false;
        } else if (Utils.isNull(et_verification_code.getText().toString())) {
            Utils.toastShow(this, "请输入正确的验证码!");
            return false;
        } else if (Utils.isNull(et_pwd.getText().toString())
                || et_pwd.getText().toString().length() < 6) {
            Utils.toastShow(this, "请输入正确的密码!");
            return false;
        }
        return true;
    }

    /**
     * 加载用户信息
     */
    public class SaveThread extends Thread {
        public int msg;

        @Override
        public void run() {
            application.setUserModel(userModel);
            MyApplication.getInstance().refreshUserModel();
            RefreshUtils.sendBroadcast(ActRetrievePwd.this, userModel);
            mHandler.sendEmptyMessage(msg);
        }
    }


}
