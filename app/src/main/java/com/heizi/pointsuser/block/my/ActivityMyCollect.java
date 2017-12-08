package com.heizi.pointsuser.block.my;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseListProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseListActivity;
import com.heizi.pointsuser.block.home.ActivityStoreDetail;
import com.heizi.pointsuser.block.home.ModelProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leo on 17/6/8.
 */
public class ActivityMyCollect extends BaseListActivity implements View.OnClickListener {

    private LinearLayout ll_notice;
    private TextView tv_notice;
    //listview
    private List<ModelProduct> listData = new ArrayList<>();
    private CommonAdapter adapter;

    //获取列表
    private ParseListProtocol<ModelProduct> parsePanoList;
    private IResponseCallback<DataSourceModel<ModelProduct>> callbackPanoList;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_list;
    }


    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("我的收藏");
        btn_back.setOnClickListener(this);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setAutoLoadEnable(false);
        ll_notice = (LinearLayout) findViewById(R.id.ll_notice);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_notice.setText("暂无收藏记录");
        //listview
        adapter = new CommonAdapter(this, listData, R.layout.item_home_store) {
            @Override
            public void getView(int position, ViewHolderHelper holder) {
                TextView tv_name = holder.findViewById(R.id.tv_name);
                ImageView iv_store = holder.findViewById(R.id.iv_store);
                RatingBar rb_star = holder.findViewById(R.id.rb_star);
                TextView tv_score = holder.findViewById(R.id.tv_score);
                TextView tv_zhekou = holder.findViewById(R.id.tv_zhekou);
                TextView tv_commit = holder.findViewById(R.id.tv_commit);
                TextView tv_distance = holder.findViewById(R.id.tv_distance);
                tv_score.setText(listData.get(position).getAve_score() + "分");
                rb_star.setProgress((int) Double.parseDouble(listData.get(position).getAve_score()));
                tv_name.setText(listData.get(position).getName());
                tv_zhekou.setText("返利折扣: " + listData.get(position).getIntegral_ratio() + "%");
                tv_commit.setText(listData.get(position).getComment_num() + "评论");
                tv_distance.setVisibility(View.INVISIBLE);
                ImageFactory.displayImage(listData.get(position).getHead_img(), iv_store, R.mipmap.iv_fail_mid, R.mipmap.iv_fail_mid);
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("id", listData.get(position).getId());
                startActivity(ActivityMyCollect.this, ActivityStoreDetail.class, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        parsePanoList = new ParseListProtocol<>(this, SERVER_URL_NEW + MY_COLLECT, ModelProduct.class);
        callbackPanoList = new IResponseCallback<DataSourceModel<ModelProduct>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelProduct> data) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    //先判断是否是刷新,刷新成功则清除数据
                    if (isRefresh) {
                        pageIndex = 1;
                        listData.removeAll(listData);
                    }
                    if (data.list.size() > 0) {
                        listData.addAll(data.list);
                        adapter.notifyDataSetChanged();
                        //数据小于请求的每页数据
                        if (data.list.size() < pageSize) {
                            mListView.setPullLoadEnable(false);
                        }
                    } else {
                        mListView.setPullLoadEnable(false);
                    }

                    if (listData.size() == 0) {
                        ll_notice.setVisibility(View.VISIBLE);
                    } else {
                        ll_notice.setVisibility(View.GONE);
                    }
                    isRefresh = false;
                    onLoad();
                }

            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    isRefresh = false;
                    onLoad();
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
                mDialog.showDialog();
            }
        };


        if (listData.size() == 0)
            getData();
    }

    public void getData() {
        Map<String, String> maps = new HashMap<>();
        maps.put("token", userModel.getToken());

        if (parsePanoList != null)
            parsePanoList.postData(maps, callbackPanoList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;

        }
    }
}
