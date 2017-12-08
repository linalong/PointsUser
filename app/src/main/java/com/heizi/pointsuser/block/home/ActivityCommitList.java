package com.heizi.pointsuser.block.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseListProtocol;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseScrollListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 店铺评论列表
 * Created by leo on 17/9/19.
 */

public class ActivityCommitList extends BaseScrollListActivity { //listview

    TextView tv_score1;
    TextView tv_score2;
    TextView tv_score3;
    RatingBar rb_star1;
    RatingBar rb_star2;
    RatingBar rb_star3;

    private TextView tv_notice;
    private LinearLayout ll_notice;
    private List<ModelCommit> listData = new ArrayList<>();
    private CommonAdapter adapter;

    ParseListProtocol<ModelCommit> parseListProtocol;
    IResponseCallback<DataSourceModel<ModelCommit>> callback;

    String commit = "", storeName = "", storeId = "";


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_commit_list;
    }

    @Override
    protected int getLayoutTop() {
        return R.layout.activity_list_scroll_top;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        storeId = getIntent().getExtras().getString("storeId");
        commit = getIntent().getExtras().getString("commit");
        storeName = getIntent().getExtras().getString("storename");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        rb_star1 = (RatingBar) view.findViewById(R.id.rb_star1);
        rb_star2 = (RatingBar) view.findViewById(R.id.rb_star2);
        rb_star3 = (RatingBar) view.findViewById(R.id.rb_star3);
        tv_score1 = (TextView) view.findViewById(R.id.tv_score1);
        tv_score2 = (TextView) view.findViewById(R.id.tv_score2);
        tv_score3 = (TextView) view.findViewById(R.id.tv_score3);
        ll_notice = (LinearLayout) view.findViewById(R.id.ll_notice);
        tv_notice = (TextView) view.findViewById(R.id.tv_notice);
        tv_notice.setText("店铺暂无评论");
        //listview
        adapter = new CommonAdapter(this, listData, R.layout.item_commit) {
            @Override
            public void getView(int position, ViewHolderHelper holder) {
                ImageView iv_photo = holder.findViewById(R.id.iv_photo);
                TextView tv_name = holder.findViewById(R.id.tv_name);
                TextView tv_score = holder.findViewById(R.id.tv_score);
                TextView tv_time = holder.findViewById(R.id.tv_time);
                TextView tv_des = holder.findViewById(R.id.tv_des);
                RatingBar rb_star = holder.findViewById(R.id.rb_star);
                rb_star.setProgress((int) Double.parseDouble(listData.get(position).getScore()));
                tv_score.setText(listData.get(position).getScore());
                tv_name.setText(listData.get(position).getNickname());
                ImageFactory.displayImage(listData.get(position).getHead_url(), iv_photo, R.mipmap.default_img_fail, R.mipmap.default_img_fail);

                GridView grid = holder.findViewById(R.id.grid);
                final List<String> dataList = new ArrayList<>();
                if (!TextUtils.isEmpty(listData.get(position).getImage1())) {
                    dataList.add(listData.get(position).getImage1());
                }
                if (!TextUtils.isEmpty(listData.get(position).getImage2())) {
                    dataList.add(listData.get(position).getImage2());
                }
                if (!TextUtils.isEmpty(listData.get(position).getImage3())) {
                    dataList.add(listData.get(position).getImage3());
                }
                CommonAdapter adapterGrid = new CommonAdapter(ActivityCommitList.this, dataList, R.layout.item_device_signed_action_image) {
                    @Override
                    public void getView(int position, ViewHolderHelper holder) {
                        ImageView iv_select_pic3 = holder.findViewById(R.id.iv_select_pic3);
                        ImageFactory.displayImage(dataList.get(position), iv_select_pic3, R.mipmap.iv_fail_small, R.mipmap.iv_fail_small);
                    }
                };
                grid.setAdapter(adapterGrid);
                adapterGrid.notifyDataSetChanged();
                tv_des.setText(listData.get(position).getComment());
            }
        };
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initViewTop() {
        super.initViewTop();
        tv_title.setText("评论");
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
        parseListProtocol = new ParseListProtocol<>(this, SERVER_URL_NEW + COMMITLIST, ModelCommit.class);
        callback = new IResponseCallback<DataSourceModel<ModelCommit>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelCommit> data) {
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
                        measureListHeight();
                        //数据小于请求的每页数据
                        if (data.list.size() < pageSize) {
                            mScrollView.setPullLoadEnable(false);
                        }
                    } else {
                        mScrollView.setPullLoadEnable(false);
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
                    Utils.toastShow(ActivityCommitList.this, errorModel.getMsg());
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

        getAverage();

    }

    /**
     */
    protected void getData() {
        Map<String, String> maps = new HashMap<>();
        maps.put("seller_id", storeId);
        maps.put("pagesize", pageSize + "");
        maps.put("p", pageIndex + "");
        if (parseListProtocol != null)
            parseListProtocol.postData(maps, callback);

    }

    /**
     * 获取店铺平均分
     */
    private void getAverage() {
        ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + average);
        Map<String, String> maps = new HashMap<>();
        maps.put("seller_id", storeId);
        parseStringProtocol.postData(maps, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                if (!isFinishing()) {
                    isBusy = false;
                    try {
                        JSONObject jsonObject = new JSONObject(data.json);
                        if (TextUtils.isEmpty(jsonObject.getString("server_average")) || jsonObject.getInt("server_average") == 0) {
                            tv_score1.setText("暂无评分");
                        } else {
                            rb_star1.setRating(jsonObject.getInt("server_average"));
                            tv_score1.setText(jsonObject.getString("server_average") + "分");
                        }

                        if (TextUtils.isEmpty(jsonObject.getString("product_average")) || jsonObject.getInt("product_average") == 0) {
                            tv_score2.setText("暂无评分");
                        } else {
                            rb_star2.setRating(jsonObject.getInt("product_average"));
                            tv_score2.setText(jsonObject.getString("product_average") + "分");
                        }

                        if (TextUtils.isEmpty(jsonObject.getString("milieu_average")) || jsonObject.getInt("milieu_average") == 0) {
                            tv_score3.setText("暂无评分");
                        } else {
                            rb_star3.setRating(jsonObject.getInt("milieu_average"));
                            tv_score3.setText(jsonObject.getString("milieu_average") + "分");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    Utils.toastShow(ActivityCommitList.this, errorModel.getMsg());
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
            }
        });
    }

}
