package com.heizi.pointsuser.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.heizi.pointsuser.R;
import com.markmao.pulltorefresh.widget.XListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leo on 17/9/19.
 */

public abstract class BaseListFragment extends BaseFragment implements XListView.IXListViewListener {


    protected XListView mListView;

    protected int pageIndex = 1, pageSize = 10;
    protected boolean isBusy = false, isRefresh = false, hasMore = true;// 当前是否在加载数据


    @Override
    protected void initView(View v) {
        super.initView(v);
        mListView = (XListView) v.findViewById(R.id.list_view);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());
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
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    protected void getData() {

    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    protected int measureListHeight(ListView listView) {
        // get ListView adapter
        ListAdapter adapter = listView.getAdapter();
        if (null == adapter) {
            return 0;
        }

        int totalHeight = 0;

        for (int i = 0, len = adapter.getCount(); i < len; i++) {
            View item = adapter.getView(i, null, listView);
            if (null == item) continue;
            // measure each item width and height
            item.measure(0, 0);
            // calculate all height
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // calculate ListView height
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));

        listView.setLayoutParams(params);

        return params.height;
    }

}
