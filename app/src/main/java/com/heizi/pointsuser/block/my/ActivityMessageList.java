package com.heizi.pointsuser.block.my;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseListProtocol;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseListActivity;
import com.heizi.pointsuser.block.home.ActivityStoreDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by leo on 17/6/8.
 */
public class ActivityMessageList extends BaseListActivity implements View.OnClickListener {

    private LinearLayout ll_notice;
    private TextView tv_notice;
    //listview
    private List<ModelMessage> listData = new ArrayList<>();
    private CommonAdapter adapter;

    //获取列表
    private ParseListProtocol<ModelMessage> parsePanoList;
    private IResponseCallback<DataSourceModel<ModelMessage>> callbackPanoList;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_list;
    }


    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("消息中心");
        btn_back.setOnClickListener(this);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setAutoLoadEnable(false);
        ll_notice = (LinearLayout) findViewById(R.id.ll_notice);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_notice.setText("暂无消息");
        //listview
        adapter = new CommonAdapter(this, listData, R.layout.item_message) {
            @Override
            public void getView(final int position, ViewHolderHelper holder) {
                RelativeLayout rl = holder.findViewById(R.id.rl);
                TextView tv_title = holder.findViewById(R.id.tv_title);
                final TextView tv_des = holder.findViewById(R.id.tv_des);
                final TextView tv_content = holder.findViewById(R.id.tv_content);
                final ImageView iv_show_flag = holder.findViewById(R.id.iv_show_flag);

                tv_title.setText(listData.get(position).getTitle());
                tv_content.setText(listData.get(position).getMessage());
                tv_des.setText(listData.get(position).getCreate_time());

                if (listData.get(position).getIs_read().equals("0")) {
                    tv_title.setTextColor(context.getResources().getColor(R.color.gray10));
                    tv_content.setTextColor(context.getResources().getColor(R.color.gray10));
                    tv_des.setTextColor(context.getResources().getColor(R.color.gray10));
                } else {
                    tv_title.setTextColor(context.getResources().getColor(R.color.black2));
                    tv_content.setTextColor(context.getResources().getColor(R.color.black2));
                    tv_des.setTextColor(context.getResources().getColor(R.color.black2));
                }


                final ModelMessage itemData = listData.get(position);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemData.getIs_read().equals("0"))
                            readMsg(position);


                        itemData.setIs_read("1");
                        notifyDataSetChanged();

                        Layout l = tv_content.getLayout();
                        if (l != null) {
                            int lines = l.getLineCount();
                            if (lines > 0) {
                                if (l.getEllipsisCount(lines - 1) > 0) {
                                    tv_content.setMaxLines(Integer.MAX_VALUE);
                                    iv_show_flag.setImageResource(R.mipmap.iv_up);
                                } else {
                                    iv_show_flag.setImageResource(R.mipmap.iv_down_black);
                                    tv_content.setMaxLines(1);
                                }
                            }
                        }
                    }
                });
            }
        };
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("id", listData.get(position).getId());
                startActivity(ActivityMessageList.this, ActivityStoreDetail.class, bundle);
            }
        });
    }


    private void readMsg(final int position) {
        ParseStringProtocol protocol = new ParseStringProtocol(this, SERVER_URL_NEW + readMessage);
        Map<String, String> map = new HashMap<>();
        map.put("token", userModel.getToken());
        map.put("message_id", listData.get(position).getId());
        protocol.getData(map, new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> response) {

            }

            @Override
            public void onFailure(ErrorModel errorModel) {

            }

            @Override
            public void onStart() {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        parsePanoList = new ParseListProtocol<>(this, SERVER_URL_NEW + messages, ModelMessage.class);
        callbackPanoList = new IResponseCallback<DataSourceModel<ModelMessage>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelMessage> data) {
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
