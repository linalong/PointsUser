package com.heizi.pointsuser.block.my;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mycommon.utils.LoadingD;
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
 * Created by leo on 17/12/6.
 */

public class ActivityReward extends BaseScrollListActivity {

    TextView tv_nun, tv_points;

    private LinearLayout ll_notice;
    private List<ModelReward> listData = new ArrayList<>();
    private CommonAdapter adapter;

    //获取列表
    private ParseListProtocol<ModelReward> parsePanoList;
    private IResponseCallback<DataSourceModel<ModelReward>> callbackPanoList;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_reward;
    }

    @Override
    protected int getLayoutTop() {
        return R.layout.activity_list_scroll_top;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_nun = (TextView) view.findViewById(R.id.tv_num);
        tv_points = (TextView) view.findViewById(R.id.tv_points);
        ll_notice = (LinearLayout) view.findViewById(R.id.ll_notice);
        //listview
        adapter = new CommonAdapter(this, listData, R.layout.item_reward) {
            @Override
            public void getView(int position, ViewHolderHelper holder) {
                TextView tv_name = holder.findViewById(R.id.tv_name);
                TextView tv_time = holder.findViewById(R.id.tv_time);
                TextView tv_type = holder.findViewById(R.id.tv_type);
                TextView tv_points = holder.findViewById(R.id.tv_points);
                ImageView iv_photo = holder.findViewById(R.id.iv_photo);

                tv_name.setText(listData.get(position).getTransaction_name());
                tv_points.setText("+" + listData.get(position).getNum());
                tv_type.setText(listData.get(position).getRemark());
                tv_time.setText(listData.get(position).getCreate_time());
                ImageFactory.displayImage(listData.get(position).getTransaction_head_img(), iv_photo, 0, 0);
            }
        };
        mListView.setAdapter(adapter);
        measureListHeight();

        getText();
    }

    @Override
    protected void initViewTop() {
        super.initViewTop();
        tv_title.setText("奖励信息");
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
        parsePanoList = new ParseListProtocol<>(this, SERVER_URL_NEW + inviteList, ModelReward.class);
        callbackPanoList = new IResponseCallback<DataSourceModel<ModelReward>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelReward> data) {
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
        maps.put("p", pageIndex + "");
        maps.put("pagesize", pageSize + "");
        if (parsePanoList != null)
            parsePanoList.postData(maps, callbackPanoList);
    }

    private void getText() {
        ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + inviteStatistics);
        Map<String, String> map = new HashMap<>();
        map.put("token", userModel.getToken());
        map.put("type", "recharge");
        parseStringProtocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                if (!isFinishing()) {
                    try {
                        JSONObject jsonObject = new JSONObject(data.json);
                        tv_nun.setText(jsonObject.getString("invite_number"));
                        tv_points.setText(jsonObject.getString("invite_income"));
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
