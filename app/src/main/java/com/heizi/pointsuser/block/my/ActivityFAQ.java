package com.heizi.pointsuser.block.my;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseListProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by leo on 17/12/6.
 */

public class ActivityFAQ extends BaseListActivity {
    private LinearLayout ll_notice;
    private TextView tv_notice;
    private List<ModelFeedback> listData = new ArrayList<>();
    private CommonAdapter adapter;

    ParseListProtocol<ModelFeedback> parseListProtocol;
    IResponseCallback<DataSourceModel<ModelFeedback>> callback;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_list;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("常见问题");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setAutoLoadEnable(false);
        ll_notice = (LinearLayout) findViewById(R.id.ll_notice);
        tv_notice = (TextView) findViewById(R.id.tv_notice);
        tv_notice.setText("暂无数据");
        //listview
        adapter = new CommonAdapter(this, listData, R.layout.item_feedback) {
            @Override
            public void getView(final int position, ViewHolderHelper holder) {
                final ImageView iv_arrow = holder.findViewById(R.id.iv_arrow);
                TextView tv_issue = holder.findViewById(R.id.tv_issue);
                final TextView tv_answer = holder.findViewById(R.id.tv_answer);
                final LinearLayout ll_issue = holder.findViewById(R.id.ll_issue);
                tv_answer.setText(listData.get(position).getAnswer());
                tv_issue.setText(listData.get(position).getIssue());
                if (listData.get(position).isOpen()) {
                    tv_answer.setVisibility(View.VISIBLE);
                    iv_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.iv_up));
                } else {
                    tv_answer.setVisibility(View.GONE);
                    iv_arrow.setImageDrawable(getResources().getDrawable(R.mipmap.iv_down_black));
                }
                ll_issue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listData.get(position).isOpen()) {
                            listData.get(position).setOpen(false);
                        } else {
                            listData.get(position).setOpen(true);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });

            }
        };
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();
        parseListProtocol = new ParseListProtocol<>(this, SERVER_URL_NEW + FAQs, ModelFeedback.class);
        callback = new IResponseCallback<DataSourceModel<ModelFeedback>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelFeedback> data) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    if (data.list.size() > 0) {
                        listData.addAll(data.list);
                        adapter.notifyDataSetChanged();
                    }
                    if (listData.size() == 0) {
                        ll_notice.setVisibility(View.VISIBLE);
                    } else {
                        ll_notice.setVisibility(View.GONE);
                    }
                    onLoad();
                }

            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    Utils.toastShow(ActivityFAQ.this, errorModel.getMsg());
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

    /**
     */
    protected void getData() {
        Map<String, String> maps = new HashMap<>();
        maps.put("token", userModel.getToken());
        if (parseListProtocol != null)
            parseListProtocol.postData(maps, callback);

    }

}
