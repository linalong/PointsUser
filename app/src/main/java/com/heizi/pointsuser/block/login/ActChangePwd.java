package com.heizi.pointsuser.block.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;
import com.heizi.pointsuser.activity.MainTabsActivity;
import com.heizi.pointsuser.utils.RefreshUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 修改密码
 *
 * @author admin
 */
public class ActChangePwd extends BaseSwipeBackCompatActivity implements
        OnClickListener, Constants {
    private EditText et_old_pwd, et_new_pwd, et_new_pwd_confrim;
    private Button btn_save;
    private TextView tv_title;
    private IResponseCallback<DataSourceModel<String>> cppcb;
    private ParseStringProtocol cpp;
    private ImageView btn_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.act_change_pwd;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_save:
                if (checkData())
                    submit();
                break;
            case R.id.btn_back:
                ActChangePwd.this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
        et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
        et_new_pwd_confrim = (EditText) findViewById(R.id.et_new_pwd_confrim);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("修改密码");
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        cpp = new ParseStringProtocol(ActChangePwd.this, SERVER_URL_NEW + CHANGEPWD);
        cppcb = new IResponseCallback<DataSourceModel<String>>() {

            @Override
            public void onSuccess(DataSourceModel<String> t) {
                // TODO Auto-generated method stub
                LoadingD.hideDialog();
                userModel.setPassword(et_new_pwd.getText().toString());
                Utils.toastShow(ActChangePwd.this, "修改密码成功");
                application.setUserModel(userModel);
                MyApplication.getInstance().refreshUserModel();
                RefreshUtils.sendBroadcast(ActChangePwd.this, userModel);
                ActChangePwd.this.startActivity(new Intent(ActChangePwd.this, MainTabsActivity.class));
                ActChangePwd.this.finish();
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                LoadingD.showDialog(ActChangePwd.this);
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                // TODO Auto-generated method stub
                LoadingD.hideDialog();
                Utils.toastShow(ActChangePwd.this, "" + errorModel.getMsg());
            }
        };
    }

    // 修改密码
    private void submit() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("old_password", et_old_pwd.getText().toString());
        maps.put("new_password", et_new_pwd.getText().toString());
        maps.put("token", userModel.getToken());
        cpp.postData(maps, cppcb);
    }

    // 验证
    private boolean checkData() {
        String eop = et_old_pwd.getText().toString();
        String enp = et_new_pwd.getText().toString();
        String enpc = et_new_pwd_confrim.getText().toString();
        if (Utils.isNull(eop) || eop.length() < 6 || eop.length() > 20) {
            Utils.toastShow(ActChangePwd.this, "旧密码错误(6-20位)");
            return false;
        } else if (Utils.checkSpecialCharacters(eop)) {
            Utils.toastShow(ActChangePwd.this, "请输入6-20位数字或英文字母或组合的原密码!");
            return false;
        } else if (Utils.isNull(enp) || enp.length() < 6 || enp.length() > 20) {
            Utils.toastShow(ActChangePwd.this, "请输入6-20位数字或英文字母或组合的新密码!");
            return false;
        } else if (Utils.checkSpecialCharacters(enp)) {
            Utils.toastShow(ActChangePwd.this, "请输入6-20位数字或英文字母或组合的新密码!");
            return false;
        } else if (Utils.isNull(enpc) || enpc.length() < 6
                || enpc.length() > 20) {
            Utils.toastShow(ActChangePwd.this, "请输入6-20位数字或英文字母或组合的确认密码!");
            return false;
        } else if (Utils.checkSpecialCharacters(enpc)) {
            Utils.toastShow(ActChangePwd.this, "请输入6-20位数字或英文字母或组合的确认密码!");
            return false;
        } else if (!et_new_pwd.getText().toString().trim()
                .equals(et_new_pwd_confrim.getText().toString().trim())) {
            Utils.toastShow(ActChangePwd.this, "两次密码输入不一致!");
            return false;
        }
        return true;
    }

}
