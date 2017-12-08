package com.heizi.pointsuser.block.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.heizi.mycommon.sortlistview.CharacterParser;
import com.heizi.mycommon.sortlistview.PinyinComparator;
import com.heizi.mycommon.sortlistview.SideBar;
import com.heizi.mycommon.sortlistview.SortAdapter;
import com.heizi.mycommon.sortlistview.SortModel;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

import static com.heizi.pointsuser.block.home.FragmentHome.codeGetCity;


/**
 * Created by Bob on 15/8/18.
 * 会话页面
 */
public class ActivityCity extends BaseSwipeBackCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    public static int GET_CITY = 101;
    @InjectView(R.id.ll_city)
    LinearLayout ll_city;
    @InjectView(R.id.ll_location)
    LinearLayout ll_location;

    @InjectView(R.id.tv_city)
    TextView tv_city;//搜索框内的城市
    @InjectView(R.id.tv_current)
    TextView tv_current;//定位到的当前城市
    @InjectView(R.id.tv_search)
    TextView tv_search;//搜索框

    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;


    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList = new ArrayList<SortModel>();

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    //是否定位成功
    private boolean isLocationSuccess = false;

    ParseStringProtocol parseStringProtocol;
    private IResponseCallback<DataSourceModel<String>> callback;

    //高德定位
    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;

    ModelAddress modelAddress;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_city;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("选择城市");

        if (getIntent().getStringExtra("cityname") != null)
            tv_city.setText(getIntent().getStringExtra("cityname"));

        // TODO Auto-generated method stub
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnScrollListener(this);
        adapter = new SortAdapter(ActivityCity.this, SourceDateList);
        adapter.setImageVisible(false);
        sortListView.setAdapter(adapter);
        sortListView.setOnItemClickListener(this);

        mlocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(mapLocationListener);
        mLocationOption.setInterval(2000);
        mlocationClient.setLocationOption(new AMapLocationClientOption());
        mlocationClient.startLocation();

    }

    AMapLocationListener mapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            ModelAddress model = new ModelAddress();
            model.setCity(aMapLocation.getCity());
            model.setLatitude(aMapLocation.getLatitude());
            model.setLongitude(aMapLocation.getLongitude());
            if (!TextUtils.isEmpty(aMapLocation.getAoiName())) {
                model.setAddress(aMapLocation.getAoiName());
            } else if (!TextUtils.isEmpty(aMapLocation.getPoiName())) {
                model.setAddress(aMapLocation.getPoiName());
            } else if (!TextUtils.isEmpty(aMapLocation.getStreet())) {
                model.setAddress(aMapLocation.getStreet());
            }
            MyApplication.getInstance().modelAddress = model;
            modelAddress = model;
            tv_current.setText(modelAddress.getCity());
            mlocationClient.stopLocation();
        }
    };


    @Override
    protected void initData() {
        super.initData();
        parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + CITYS);
        callback = new IResponseCallback<DataSourceModel<String>>() {
            @Override
            public void onSuccess(DataSourceModel<String> data) {
                if (!isFinishing()) {
                    isBusy = false;
                    mDialog.hideDialog();
                    try {
                        JSONArray jsonArray = new JSONArray(data.json);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            SortModel model = new SortModel();
                            model.setId(jsonArray.getJSONObject(i).getInt("id"));
                            model.setName(jsonArray.getJSONObject(i).getString("name"));
                            SourceDateList.add(model);
                        }
                        setList(SourceDateList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                if (!isFinishing()) {
                    isBusy = false;
                    Utils.toastShow(ActivityCity.this, errorModel.getMsg());
                    mDialog.hideDialog();
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
                mDialog.showDialog();
            }
        };
        Map<String, String> maps = new HashMap<String, String>();
        parseStringProtocol.getData(maps, callback);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        modelAddress.setCity(SourceDateList.get(arg2).getName());
        tv_city.setText(SourceDateList.get(arg2).getName());
        ll_city.setVisibility(View.GONE);
        ll_location.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.tv_city, R.id.tv_current, R.id.btn_back, R.id.ll_location, R.id.tv_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;

            case R.id.tv_city:
                if (ll_city.getVisibility() == View.GONE) {
                    ll_city.setVisibility(View.VISIBLE);
                    ll_location.setVisibility(View.GONE);
                } else {
                    ll_city.setVisibility(View.GONE);
                    ll_location.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_current:
                modelAddress.setCity(tv_current.getText().toString());
                tv_city.setText(tv_current.getText().toString());
                ll_city.setVisibility(View.GONE);
                ll_location.setVisibility(View.VISIBLE);
                break;

            case R.id.ll_location:
                mlocationClient = new AMapLocationClient(getApplicationContext());
                mLocationOption = new AMapLocationClientOption();
                mlocationClient.setLocationListener(new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        ModelAddress model = new ModelAddress();
                        model.setCity(aMapLocation.getCity());
                        model.setLatitude(aMapLocation.getLatitude());
                        model.setLongitude(aMapLocation.getLongitude());
                        if (!TextUtils.isEmpty(aMapLocation.getAoiName())) {
                            model.setAddress(aMapLocation.getAoiName());
                        } else if (!TextUtils.isEmpty(aMapLocation.getPoiName())) {
                            model.setAddress(aMapLocation.getPoiName());
                        } else if (!TextUtils.isEmpty(aMapLocation.getStreet())) {
                            model.setAddress(aMapLocation.getStreet());
                        } else {
                            final AlertDialog myDialog = new AlertDialog.Builder(ActivityCity.this).create();
                            myDialog.setCanceledOnTouchOutside(false);
                            myDialog.show();
                            myDialog.getWindow().setContentView(R.layout.alert_home);
                            TextView tvContent = (TextView) myDialog.getWindow()
                                    .findViewById(R.id.tv_content);
                            tvContent.setText("获取位置信息失败,是否重试?");
                            final Button btn_sure = (Button) myDialog.getWindow().findViewById(R.id.btn_sure);
                            btn_sure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mlocationClient.startLocation();
                                    myDialog.cancel();
                                }
                            });
                            myDialog.getWindow().findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDialog.cancel();
                                }
                            });
                            tv_current.setText("定位失败");
                            return;
                        }
                        MyApplication.getInstance().modelAddress = model;
                        mlocationClient.stopLocation();
                        tv_city.setText(aMapLocation.getAoiName());
                        setResult(codeGetCity);
                        finish();
                    }
                });
                mLocationOption.setInterval(2000);
                mlocationClient.setLocationOption(new AMapLocationClientOption());
                mlocationClient.startLocation();
                break;

            case R.id.tv_search:
                Bundle bundle = new Bundle();
                bundle.putString("city", tv_city.getText().toString());
                startActivityForResult(ActivityCity.this, ActivitySearchCity.class, bundle, GET_CITY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GET_CITY) {
            finish();
        }
    }

    /**
     * 将数据重新转化一下
     *
     * @return
     */
    private List<SortModel> filledData(List<SortModel> arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            SortModel sortModel = arrayList.get(i);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(sortModel
                    .getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }
        }
        return arrayList;

    }

    public void setList(List<SortModel> SourceDateList) {
        this.SourceDateList = filledData(SourceDateList);
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter.updateListView(SourceDateList);
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
        // TODO Auto-generated method stub
        switch (arg1) {
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:// 滚动状态
                Utils.hideInputMethod(ActivityCity.this);
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸后滚动
                Utils.hideInputMethod(ActivityCity.this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mlocationClient.unRegisterLocationListener(mapLocationListener);
        mlocationClient.stopLocation();
        super.onDestroy();
    }


}
