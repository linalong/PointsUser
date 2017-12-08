package com.heizi.pointsuser.block.my;

import android.view.View;
import android.webkit.WebView;

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

/**
 * Created by leo on 17/12/7.
 */

public class ActivityPointsExplain extends BaseSwipeBackCompatActivity {
    @InjectView(R.id.webview)
    WebView webview;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("积分说明");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getText();
    }

    private void getText() {
        ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + integralExplain);
        Map<String, String> map = new HashMap<>();
        parseStringProtocol.getData(map, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                if (!isFinishing()) {
                    try {
                        JSONObject jsonObject = new JSONObject(data.json);
                        jsonObject.getString("content");
                        webview.setBackgroundColor(0);
//                        webview.getBackground().setAlpha(0);
                        webview.loadDataWithBaseURL(null, jsonObject.getString("content"), "text/html", "utf-8", null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                }
            }

            @Override
            public void onStart() {
            }
        });
    }
}
