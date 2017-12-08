package com.heizi.pointsuser.block.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mycommon.utils.SystemUtil;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseObjectProtocol;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;
import com.heizi.pointsuser.block.login.ActLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 店铺详情
 * Created by leo on 17/9/20.
 */

public class ActivityStoreDetail extends BaseSwipeBackCompatActivity implements View.OnClickListener {

    @InjectView(R.id.iv_store)
    ImageView iv_store;
    @InjectView(R.id.iv_store_des1)
    ImageView iv_store_des1;
    @InjectView(R.id.iv_store_des2)
    ImageView iv_store_des2;
    @InjectView(R.id.iv_store_des3)
    ImageView iv_store_des3;
    @InjectView(R.id.iv_yingyezhihzao)
    ImageView iv_yingyezhihzao;
    @InjectView(R.id.iv_xukezheng)
    ImageView iv_xukezheng;

    @InjectView(R.id.tv_store_name)
    TextView tv_store_name;
    @InjectView(R.id.tv_store_des)
    TextView tv_store_des;
    @InjectView(R.id.tv_point_percent)
    TextView tv_point_percent;
    @InjectView(R.id.rb_star)
    RatingBar rb_star;
    @InjectView(R.id.tv_score)
    TextView tv_score;
    @InjectView(R.id.tv_commit)
    TextView tv_commit;
    @InjectView(R.id.tv_store_des1)
    TextView tv_store_des1;
    @InjectView(R.id.tv_store_des2)
    TextView tv_store_des2;
    @InjectView(R.id.tv_store_des3)
    TextView tv_store_des3;
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_fanwei)
    TextView tv_fanwei;

    @InjectView(R.id.tv_address)
    TextView tv_address;


    @InjectView(R.id.ll_phone)
    LinearLayout ll_phone;
    @InjectView(R.id.ll_address)
    LinearLayout ll_address;
    @InjectView(R.id.ll_fanwei)
    LinearLayout ll_fanwei;

    private String storeId;

    ModelStoreDetail detail;

    boolean isCollected;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_store_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storeId = getIntent().getExtras().getString("id");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("店铺详情");
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageDrawable(getResources().getDrawable(R.mipmap.iv_collect_off));
    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }


    private void getData() {
        ParseObjectProtocol<ModelStoreDetail> protocol = new ParseObjectProtocol(this, SERVER_URL_NEW + STORE_DETAIL, ModelStoreDetail.class);
        Map<String, String> map = new HashMap<>();
        map.put("seller_id", storeId);
        protocol.postData(map, new IResponseCallback<DataSourceModel<ModelStoreDetail>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelStoreDetail> data) {

                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    detail = data.temp;

                    tv_store_name.setText(detail.getName());
                    tv_store_des.setText(detail.getIntroduction());
                    if (!TextUtils.isEmpty(detail.getDesc()))
                        tv_store_des1.setText(detail.getDesc());
                    else
                        tv_store_des1.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(detail.getDesc2()))
                        tv_store_des2.setText(detail.getDesc2());
                    else
                        tv_store_des2.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(detail.getDesc3()))
                        tv_store_des3.setText(detail.getDesc3());
                    else
                        tv_store_des3.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(detail.getUse_range()))
                        tv_fanwei.setText(detail.getUse_range());
                    else
                        ll_fanwei.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(detail.getBusiness_hours()))
                        tv_time.setText(detail.getBusiness_hours());

                    tv_store_name.setText(detail.getName());
                    tv_point_percent.setText("积分比例:" + detail.getIntegral_ratio() + "%");
                    tv_commit.setText(detail.getComment_num() + "评论");
                    tv_address.setText(detail.getAddress());
                    tv_score.setText(detail.getAve_score() + "分");
                    rb_star.setProgress((int) Double.parseDouble(detail.getAve_score()));

                    ImageFactory.displayImage(detail.getHead_img(), iv_store, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);
                    if (!TextUtils.isEmpty(detail.getImage())) {
                        ImageFactory.displayImage(detail.getImage(), iv_store_des1, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);
                    } else {
                        iv_store_des1.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(detail.getImage2())) {
                        ImageFactory.displayImage(detail.getImage2(), iv_store_des2, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);
                    } else {
                        iv_store_des2.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(detail.getImage3())) {
                        ImageFactory.displayImage(detail.getImage3(), iv_store_des3, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);
                    } else {
                        iv_store_des3.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(detail.getBusiness_permit()))
                        ImageFactory.displayImage(detail.getBusiness_permit(), iv_yingyezhihzao, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);

                    if (!TextUtils.isEmpty(detail.getBusiness_license()))
                        ImageFactory.displayImage(detail.getBusiness_license(), iv_xukezheng, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);

                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
                mDialog.showDialog();
            }
        });

        //判断是否收藏
        if (userModel != null) {
            ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + STORE_ISCOLLECTED);
            Map<String, String> map1 = new HashMap<>();
            map1.put("token", userModel.getToken());
            map1.put("seller_id", storeId);
            parseStringProtocol.postData(map1, new IResponseCallback<DataSourceModel<String>>() {
                @Override
                public void onSuccess(DataSourceModel<String> data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data.json);
                        isCollected = jsonObject.getInt("is_collection") == 0 ? false : true;
                        iv_right.setImageDrawable(getResources().getDrawable(isCollected == true ?
                                R.mipmap.iv_collect_on : R.mipmap.iv_collect_off));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(ErrorModel errorModel) {

                }

                @Override
                public void onStart() {

                }
            });
        }
    }


    @OnClick({R.id.ll_phone, R.id.ll_address, R.id.tv_commit, R.id.btn_back, R.id.iv_right})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.ll_phone:
                if (detail != null) {
                    SystemUtil.call(ActivityStoreDetail.this, detail.getMobile());
                } else {
                    Utils.toastShow(ActivityStoreDetail.this, "未获取到店铺信息");
                }
                break;
            case R.id.ll_address:
                if (detail != null) {
                    SystemUtil.map(ActivityStoreDetail.this, detail.getLatitude(), detail.getLongitude(), detail.getAddress());
                } else {
                    Utils.toastShow(ActivityStoreDetail.this, "未获取到店铺信息");
                }

                break;
            case R.id.tv_commit:
                Bundle bundle = new Bundle();
                bundle.putString("storeId", storeId);
                bundle.putString("commit", tv_commit.getText().toString());
                bundle.putString("storename", tv_store_name.getText().toString());
                startActivity(ActivityStoreDetail.this, ActivityCommitList.class, bundle);
                break;

            case R.id.iv_right:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(ActivityStoreDetail.this, ActLogin.class);
                    startActivity(intent);
                } else {
                    if (isCollected) {
                        ParseStringProtocol protocol = new ParseStringProtocol(ActivityStoreDetail.this, SERVER_URL_NEW + STORE_UNCOLLECT);
                        Map<String, String> map = new HashMap<>();
                        map.put("token", userModel.getToken());
                        map.put("seller_id", storeId);
                        protocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
                            @Override
                            public void onSuccess(DataSourceModel<String> data) {
                                isCollected = false;
                                iv_right.setImageDrawable(getResources().getDrawable(isCollected == true ?
                                        R.mipmap.iv_collect_on : R.mipmap.iv_collect_off));
                                Utils.toastShow(ActivityStoreDetail.this, "取消收藏店铺成功");
                            }

                            @Override
                            public void onFailure(ErrorModel errorModel) {
                                Utils.toastShow(ActivityStoreDetail.this, "取消收藏店铺失败");
                            }

                            @Override
                            public void onStart() {

                            }
                        });
                    } else {
                        ParseStringProtocol protocol = new ParseStringProtocol(ActivityStoreDetail.this, SERVER_URL_NEW + STORE_COLLECT);
                        Map<String, String> map = new HashMap<>();
                        map.put("token", userModel.getToken());
                        map.put("seller_id", storeId);
                        protocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
                            @Override
                            public void onSuccess(DataSourceModel<String> data) {
                                isCollected = true;
                                iv_right.setImageDrawable(getResources().getDrawable(isCollected == true ?
                                        R.mipmap.iv_collect_on : R.mipmap.iv_collect_off));
                                Utils.toastShow(ActivityStoreDetail.this, "收藏店铺成功");
                            }

                            @Override
                            public void onFailure(ErrorModel errorModel) {
                                Utils.toastShow(ActivityStoreDetail.this, "收藏店铺失败");
                            }

                            @Override
                            public void onStart() {

                            }
                        });

                    }
                }
                break;
        }
    }
}
