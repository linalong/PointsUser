package com.heizi.pointsuser.block.maidan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.heizi.mycommon.utils.StringUtils;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by leo on 17/9/18.
 */

public class ActivityMaidan extends BaseSwipeBackCompatActivity implements View.OnClickListener {
    //店铺id,店铺名
    String storeId, storeName;
    //消费金额
    double money;
    //转换比例
    int trans;

    public static String spilt = "jfnmuhgpbv";

    //本单最多可用积分
    @InjectView(R.id.tv_point_canuse)
    TextView tv_point_canuse;
    //本单获得积分
    @InjectView(R.id.tv_point_get)
    TextView tv_point_get;
    //实际支付金额
    @InjectView(R.id.tv_real_money)
    TextView tv_real_money;
    //订单金额
    @InjectView(R.id.tv_money)
    TextView tv_money;
    //使用积分
    @InjectView(R.id.et_point)
    EditText et_point;
    @InjectView(R.id.btn_ok)
    Button btn_ok;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_maidan;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String content = getIntent().getStringExtra("content");
        String[] str = content.split(spilt);
        storeId = str[0];
        storeName = str[1];
        trans = Integer.parseInt(str[2]);
        money = StringUtils.getDouble(str[3]);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText(storeName);
        final int canuse = (int) (money * trans * 0.01);
        tv_point_canuse.setText(canuse + "");
        tv_point_get.setText(canuse + "");
        tv_real_money.setText(money + "");
        tv_money.setText(money + "");

        et_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!et_point.getText().toString().equals("")) {
                    if (Integer.parseInt(et_point.getText().toString()) > canuse) {
                        et_point.setText(canuse + "");
                        Utils.toastShow(ActivityMaidan.this, "本单最多能使用" + canuse + "积分");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_point.getText().toString().equals("")) {
                    tv_point_get.setText((int) (money * trans * 0.01) + "");
                    tv_real_money.setText(money + "");
                } else {
                    tv_point_get.setText(canuse - Integer.parseInt(et_point.getText().toString()) + "");
                    tv_real_money.setText(StringUtils.getDouble((money - Integer.parseInt(et_point.getText().toString()) + "")) + "");
                }
            }
        });
    }

    @OnClick({R.id.btn_ok, R.id.btn_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_ok:
                if (et_point.getText().toString().equals("")) {
                    Utils.toastShow(ActivityMaidan.this, "请输入要使用的积分数量");
                } else {
                    ParseStringProtocol protocol = new ParseStringProtocol(ActivityMaidan.this, SERVER_URL_NEW + MAIDAN);
                    Map<String, String> map = new HashMap<>();
                    map.put("total_fee", money + "");
                    map.put("store_id", storeId);
                    map.put("pay_points", et_point.getText().toString());
                    map.put("token", userModel.getToken());
                    protocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
                        @Override
                        public void onSuccess(DataSourceModel<String> data) {
                            if (!isFinishing()) {
                                isBusy = false;
                                mDialog.hideDialog();
                                Utils.toastShow(ActivityMaidan.this, "积分抵扣成功");
                                Bundle bundle = new Bundle();
                                bundle.putString("seller_id", storeId);
                                startActivity(ActivityMaidan.this, ActivityCommit.class, bundle);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(ErrorModel errorModel) {
                            if (!isFinishing()) {
                                isBusy = false;
                                mDialog.hideDialog();
                                Utils.toastShow(ActivityMaidan.this, errorModel.getMsg());
                            }
                        }

                        @Override
                        public void onStart() {
                            isBusy = true;
                            mDialog.showDialog();
                        }
                    });

                }
                break;
        }
    }
}
