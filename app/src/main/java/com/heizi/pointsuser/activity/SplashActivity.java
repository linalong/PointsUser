package com.heizi.pointsuser.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.heizi.mycommon.adapter.CommonPagerAdapter;
import com.heizi.mycommon.utils.SharePreferenceUtil;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseObjectProtocol;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.UserModel;
import com.heizi.pointsuser.block.home.ModelHome;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplashActivity extends BaseSwipeBackCompatActivity {


    SharePreferenceUtil sharePreferenceUtil;
    CommonPagerAdapter pagerAdapter;
    List<View> viewList = new ArrayList<>();
    RelativeLayout rl_splash;
    ViewPager vp;

    private ParseObjectProtocol loginProtocol;
    private IResponseCallback<DataSourceModel<UserModel>> cb;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    startMainActivity();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }


    @Override
    protected void initView() {
        super.initView();
        LayoutInflater inflater = LayoutInflater.from(this);
        rl_splash = (RelativeLayout) findViewById(R.id.splash);
        vp = (ViewPager) findViewById(R.id.viewpager);
        sharePreferenceUtil = new SharePreferenceUtil(this);
//        if (!sharePreferenceUtil.getBooleanData("hasOpenApp")) {
//            vp.setVisibility(View.VISIBLE);
//            rl_splash.setVisibility(View.GONE);
//            pagerAdapter = new CommonPagerAdapter();
//            viewList.add(inflater.inflate(R.layout.view_guide_1, null));
//            viewList.add(inflater.inflate(R.layout.view_guide_2, null));
//            viewList.add(inflater.inflate(R.layout.view_guide_3, null));
//            pagerAdapter.setViews(viewList);
//            vp.setAdapter(pagerAdapter);
//            viewList.get(viewList.size()-1).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    sharePreferenceUtil.setBooleanData("hasOpenApp", true);
//                    startMainActivity();
//                }
//            });
//        } else {
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    SystemClock.sleep(3000);
//                    handler.sendEmptyMessage(1);
//                }
//            }.start();
//        }


    }

    // 初始化数据
    protected void initData() {
        loginProtocol = new ParseObjectProtocol(this, SERVER_URL_NEW + LOGIN, UserModel.class);
        cb = new IResponseCallback<DataSourceModel<UserModel>>() {

            @Override
            public void onSuccess(DataSourceModel<UserModel> t) {
                try {
                    UserModel u = t.temp;
                    u.setPassword(userModel.getPassword());
                    MyApplication.getInstance().setUserModel(u);
                } catch (Exception e) {

                }
                SystemClock.sleep(2000);
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                // TODO Auto-generated method stub
                MyApplication.getInstance().logout();
                SystemClock.sleep(2000);
                handler.sendEmptyMessage(1);
            }
        };

        prcessLogic();
//        test();
    }

    private void test() {
        try {
//            Map<String, String> mapP = new HashMap<>();
//            mapP.put("name1", "1");
//            mapP.put("name2", "2");
//            mapP.put("name3", "3");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < 10; i++) {
                ModelHome modelHome = new ModelHome();
                modelHome.setImage_url("http://img" + i);
                modelHome.setId(i + "");
                JSONObject jsonObjectModel = new JSONObject();
                jsonObjectModel.put("img", modelHome.getImage_url());
                jsonObjectModel.put("id", modelHome.getId());
                jsonArray.put(jsonObjectModel);
            }


            JSONObject jsonObject = new JSONObject();

            jsonObject.put("parm1", "111111");
            jsonObject.put("parm2", jsonArray);

            ParseStringProtocol protocol = new ParseStringProtocol(this, SERVER_URL_NEW + "/api.php/v1.Index/test/");
            Map<String, String> map = new HashMap<>();
            map.put("json", jsonObject.toString());
            protocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
                @Override
                public void onSuccess(DataSourceModel<String> data) {

                }

                @Override
                public void onFailure(ErrorModel errorModel) {

                }

                @Override
                public void onStart() {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void getData() {
        Map<String, String> maps = new HashMap<>();
        maps.put("mobile", userModel.getMobile());
        maps.put("password", userModel.getPassword());
        loginProtocol.postData(maps, cb);
    }

    private void prcessLogic() {
//        showLoadingDialog();
        if (MyApplication.getInstance().refreshUserModel() != null) {
            userModel = MyApplication.getInstance().getUserModel();
            if (userModel != null) {
                getData();
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        SystemClock.sleep(3000);
                        handler.sendEmptyMessage(1);
                    }
                }.start();
            }
        } else {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    SystemClock.sleep(3000);
                    handler.sendEmptyMessage(1);
                }
            }.start();
        }

    }


    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainTabsActivity.class);
        startActivity(intent);
        finish();
    }


}
