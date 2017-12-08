package com.heizi.pointsuser.block.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.heizi.mycommon.utils.LoadingD;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseObjectProtocol;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.UserModel;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;
import com.heizi.pointsuser.activity.MainTabsActivity;
import com.heizi.pointsuser.utils.RefreshUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 登录fragment
 *
 * @author Administrator
 */
@SuppressLint("NewApi")
public class ActLogin extends BaseSwipeBackCompatActivity implements OnClickListener,
        Constants {
    private TextView tv_forget_pwd, tv_register;
    private Button btn_login;
    private EditText et_name, et_pwd;
    private ParseObjectProtocol loginProtocol;
    private IResponseCallback<DataSourceModel<UserModel>> cb;
    private final int MSG_TOMAIN = 0x00000001;// 打开主页
    private Handler mHandler;
    private boolean isBusy = false;

    static ActLogin instance;

    public static ActLogin getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (userModel != null && !Utils.isNull(userModel.getMobile())
                && !Utils.isNull(userModel.getPassword())) {
            et_name.setText(userModel.getMobile());
            et_pwd.setText(userModel.getPassword());
        }
    }

    @Override
    public void onDestroy() {
        cb = null;
        super.onDestroy();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.act_login;
    }


    protected void initView() {
        super.initView();
        tv_title.setText("登录");
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        if (userModel != null && !TextUtils.isEmpty(userModel.getPassword())
                && !TextUtils.isEmpty(userModel.getMobile())) {
            et_name.setText(userModel.getMobile());
            et_pwd.setText(userModel.getPassword());
        }
        mHandler = new Handler(this.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {

                    case MSG_TOMAIN:
                        startActivity(new Intent(ActLogin.this, MainTabsActivity.class));
                        ActLogin.this.finish();
                        break;
                    default:
                        break;
                }
            }

            ;
        };
    }

    // 初始化数据
    protected void initData() {
        loginProtocol = new ParseObjectProtocol(this, SERVER_URL_NEW + LOGIN, UserModel.class);
        cb = new IResponseCallback<DataSourceModel<UserModel>>() {

            @Override
            public void onSuccess(DataSourceModel<UserModel> t) {
                isBusy = false;
                LoadingD.hideDialog();
                try {
                    userModel = t.temp;
                    userModel.setPassword(et_pwd.getText().toString());
                    if (!TextUtils.isEmpty(userModel.getId())) {
                        SaveThread saveThread = new SaveThread();
                        Utils.toastShow(ActLogin.this, "登录成功!");
                        saveThread.msg = MSG_TOMAIN;
                        saveThread.start();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                LoadingD.showDialog(ActLogin.this);
                isBusy = true;
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                // TODO Auto-generated method stub
                isBusy = false;
                LoadingD.hideDialog();
                Utils.toastShow(ActLogin.this, Utils.isNull(errorModel
                        .getMsg()) ? "登录失败,请稍后重试!"
                        : errorModel.getMsg());
            }
        };
    }

    // 校验数据
    private boolean checkData() {
        if (Utils.isNull(et_name.getText().toString())) {
            Utils.toastShow(ActLogin.this, "请输入手机号");
            return false;
        } else if (Utils.isNull(et_pwd.getText().toString())) {
            Utils.toastShow(ActLogin.this, "请输入密码");
            return false;
        }
        return true;
    }

    private void login() {
        if (isBusy) return;
        Map<String, String> maps = new HashMap<>();
        maps.put("mobile", et_name.getText().toString());
        maps.put("password", et_pwd.getText().toString());
        loginProtocol.postData(maps, cb);
    }

    public void login(String openid, String user_name, String user_avatar, String user_wxinfo) {
        ParseObjectProtocol login = new ParseObjectProtocol(this, SERVER_URL + "/index.php/Api/user/third_login/", UserModel.class);
        Map<String, String> maps = new HashMap<>();
        maps.put("type", "2");
        maps.put("openid", openid);
        maps.put("client", "android");
        maps.put("user_name", user_name);
        maps.put("user_avatar", user_avatar);
        maps.put("user_wxinfo", user_wxinfo);
        login.postData(maps, cb);
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
            RefreshUtils.sendBroadcast(ActLogin.this, userModel);
            mHandler.sendEmptyMessage(msg);
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(this, ActRegister.class));
                break;
            case R.id.tv_forget_pwd:
                Intent intent = new Intent();
                intent.setClass(this, ActRetrievePwd.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                if (checkData()) {
                    login();
                }

                //{"code":200,"userId":"298000","token":"i/x9E9ir59nQNIcjyDIFy3BmeaL655Pwn3bjilMn4KcCwepZ9lpI7hk1ULMMXd2z6a8MPlAkjr/l3bQmU6U3cA=="}
//                connect("i/x9E9ir59nQNIcjyDIFy3BmeaL655Pwn3bjilMn4KcCwepZ9lpI7hk1ULMMXd2z6a8MPlAkjr/l3bQmU6U3cA==");
                break;
            case R.id.btn_back:
                ActLogin.this.finish();
                break;

            default:
                break;
        }
    }


}
