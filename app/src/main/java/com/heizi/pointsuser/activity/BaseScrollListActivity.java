package com.heizi.pointsuser.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.heizi.mycommon.utils.LoadingE;
import com.heizi.pointsuser.R;
import com.markmao.pulltorefresh.widget.XScrollView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leo on 17/9/19.
 */

public abstract class BaseScrollListActivity extends BaseSwipeBackCompatActivity implements XScrollView.IXScrollViewListener {


    protected XScrollView mScrollView;
    protected ListView mListView;
    protected View view;

    protected int pageIndex = 1, pageSize = 10;
    protected boolean isRefresh = false;// 当前是否在加载数据


    @Override
    protected void initView() {
        super.initView();
        //用于最外层就是scrollview的简单布局
        if (getLayoutTop() == 0) {
            mScrollView = new XScrollView(this);
        }
        //用于scrollview还有同级的更复杂的布局
        else {
            view = LayoutInflater.from(this).inflate(getLayoutTop(), null);
            mScrollView = (XScrollView) view.findViewById(R.id.scroll_view);
        }
        mScrollView.setFillViewport(true);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(getTime());
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setFocusable(false);
        mListView.setFocusableInTouchMode(false);
        ViewGroup viewGroup = (ViewGroup) contentView.getParent();
        viewGroup.removeAllViews();
        mScrollView.setView(contentView);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (view == null) {
            setContentView(mScrollView);
        } else {
            setContentView(view);
        }
        initViewTop();

    }

    //在initview后调用
    protected void initViewTop() {
        super.initView();
    }


    protected int getLayoutTop() {
        return 0;
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageIndex = 1;
        mScrollView.setPullLoadEnable(true);
        getData();
    }

    @Override
    public void onLoadMore() {
        if (!isBusy) {
            pageIndex++;
            getData();
        }
    }


    protected void onLoad() {
        mScrollView.stopRefresh();
        mScrollView.stopLoadMore();
        mScrollView.setRefreshTime(getTime());
    }

    protected void getData() {

    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    protected int measureListHeight() {
        // get ListView adapter
        ListAdapter adapter = mListView.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, mListView);
            if (null == item) continue;
            // measure each item width and height
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            item.measure(width, height);

//            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = mListView.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (mListView.getDividerHeight() * (adapter.getCount() - 1));

        mListView.setLayoutParams(params);

        return params.height;
    }
}
