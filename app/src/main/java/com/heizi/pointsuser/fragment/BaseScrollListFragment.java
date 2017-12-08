package com.heizi.pointsuser.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.heizi.pointsuser.R;
import com.markmao.pulltorefresh.widget.XScrollView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leo on 17/9/19.
 */

public abstract class BaseScrollListFragment extends BaseFragment implements XScrollView.IXScrollViewListener {


    protected XScrollView mScrollView;

    protected ListView mListView;
    protected View view;

    protected int pageIndex = 1, pageSize = 10;
    protected boolean isBusy = false, isRefresh = false,hasMore=true;// 当前是否在加载数据


    @Override
    protected View onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        if (view == null) {
            return mScrollView;
        } else {
            initViewTop(view);
            return view;
        }


    }

    /**
     * 先执行initview 后执行initviewtop
     * @param v
     */
    protected void initViewTop(View v) {
        super.initView(v);
    }

    @Override
    protected void initView(View v) {
        super.initView(v);


        //用于最外层就是scrollview的简单布局
        if (getLayoutTop() == 0) {
            mScrollView = new XScrollView(getActivity());
        }
        //用于scrollview还有同级的更复杂的布局
        else {
            view = inflater.inflate(getLayoutTop(), null);
            mScrollView = (XScrollView) view.findViewById(R.id.scroll_view);
        }
        mScrollView.setFillViewport(true);
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(getTime());
        if (null != v) {
            mListView = (ListView) v.findViewById(R.id.listview);
            mListView.setFocusable(false);
            mListView.setFocusableInTouchMode(false);
        }

        mScrollView.setView(v);
    }

    protected int getLayoutTop() {
        return 0;
    }

    ;

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageIndex = 1;
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
            item.measure(0, 0);
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
