package com.heizi.pointsuser.block.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mycommon.sortlistview.ClearEditText;
import com.heizi.mycommon.utils.Utils;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseListActivity;

import java.util.ArrayList;

import static com.heizi.pointsuser.block.home.ActivityCity.GET_CITY;


/**
 * Created by leo on 17/9/21.
 */

public class ActivitySearchCity extends BaseListActivity implements PoiSearch.OnPoiSearchListener {

    ClearEditText et_search;
    TextView tv_search;

    String city = "";
    String keyWord = "";

    PoiSearch poiSearch;
    PoiSearch.Query query;

    ArrayList<PoiItem> listData = new ArrayList<>();
    CommonAdapter adapter;
    LinearLayout ll_notice;

    ModelAddress modelAddress;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        city = getIntent().getExtras().getString("city");
        super.onCreate(savedInstanceState);
    }


    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        keyWord = et_search.getText().toString().trim();
        query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        if (modelAddress != null && modelAddress.getLatitude() != 0 && modelAddress.getLongitude() != 0) {
            PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(new LatLonPoint(modelAddress.getLatitude(), modelAddress.getLongitude()), 0);
            poiSearch.setBound(searchBound);//
        }
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();// 异步搜索
        isBusy = true;
        mDialog.showDialog();
        getData();
    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (!isFinishing()) {
            isBusy = false;
            mDialog.hideDialog();
            if (rCode == 1000) {
                if (result.getPois().size() > 0) {
                    ll_notice.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    //解析result获取POI信息
                    if (listData.size() > 0)
                        listData.clear();
                    listData.addAll(result.getPois());
                    adapter.notifyDataSetChanged();
                    onLoad();
                } else {
                    ll_notice.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

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
        et_search = (ClearEditText) findViewById(R.id.et_search);
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
                doSearchQuery();
                Utils.hideInputMethod(ActivitySearchCity.this);
                return true;

            }
        });
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchQuery();

            }
        });
        ll_notice = (LinearLayout) findViewById(R.id.ll_notice);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        adapter = new CommonAdapter(this, listData, R.layout.item_poi) {
            @Override
            public void getView(int position, ViewHolderHelper holder) {
                TextView tv_name = holder.findViewById(R.id.tv_name);
                TextView tv_local = holder.findViewById(R.id.tv_local);
                TextView tv_distance = holder.findViewById(R.id.tv_distance);
                tv_name.setText(listData.get(position).getTitle());
                tv_local.setText(listData.get(position).getAdName() + listData.get(position).getSnippet());
                if (listData.get(position).getDistance() > 0) {
                    tv_distance.setVisibility(View.VISIBLE);
                    if (listData.get(position).getDistance() > 999) {
                        tv_distance.setText((float) Math.round((float) listData.get(position).getDistance() / 1000 * 10) / 10 + "千米")
                        ;
                    } else {
                        tv_distance.setText(listData.get(position).getDistance() + "米");
                    }

                } else {
                    tv_distance.setVisibility(View.INVISIBLE);
                }
            }
        };

        mListView.setAdapter(adapter);
        modelAddress = MyApplication.getInstance().modelAddress;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelAddress modelAddress = new ModelAddress();
                modelAddress.setCity(listData.get(position - 1).getCityName());
                modelAddress.setLatitude(listData.get(position - 1).getLatLonPoint().getLatitude());
                modelAddress.setLongitude(listData.get(position - 1).getLatLonPoint().getLongitude());
                modelAddress.setAddress(listData.get(position - 1).getTitle());
                MyApplication.getInstance().modelAddress = modelAddress;
                setResult(GET_CITY);
                finish();
            }
        });
    }

}
