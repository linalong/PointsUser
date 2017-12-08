package com.heizi.pointsuser.block.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.heizi.mycommon.sortlistview.ClearEditText;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;

import butterknife.InjectView;

/**
 * Created by leo on 17/9/20.
 */

public class ActivitySearch extends BaseSwipeBackCompatActivity {
    @InjectView(R.id.et_search)
    ClearEditText et_search;
    @InjectView(R.id.tv_search)
    TextView tv_search;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }


    @Override
    protected void initView() {
        super.initView();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 监听回车键
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             *
             * @param v 被监听的对象
             * @param actionId  动作标识符,如果值等于EditorInfo.IME_NULL，则回车键被按下。
             * @param event    如果由输入键触发，这是事件；否则，这是空的(比如非输入键触发是空的)。
             * @return 返回你的动作
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", v.getText().toString());
                bundle.putString("title", "搜索结果");
                startActivity(ActivitySearch.this, ActivityStoreList.class, bundle);
                return true;

            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", et_search.getText().toString());
                bundle.putString("title", "搜索结果");
                startActivity(ActivitySearch.this, ActivityStoreList.class, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
