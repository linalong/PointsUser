package com.heizi.pointsuser.block.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.heizi.mycommon.adapter.CommonAdapter;
import com.heizi.mycommon.adapter.ViewHolderHelper;
import com.heizi.mycommon.utils.DipPixUtils;
import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mycommon.utils.TimerAlarm;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseListProtocol;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.block.login.ActLogin;
import com.heizi.pointsuser.block.maidan.ActivityScanCode;
import com.heizi.pointsuser.fragment.BaseScrollListFragment;
import com.heizi.pointsuser.utils.UtilDialog;
import com.markmao.pulltorefresh.widget.OnScrollChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 首页
 * 1.首页列表默认关联城市id和分类id  百度定位变化后获取城市id,id获取成功重新刷新列表,从activityCity更改城市返回后重新刷新列表
 * Created by leo on 17/5/16.
 */

public class FragmentHome extends BaseScrollListFragment implements
        View.OnClickListener, AdapterView.OnItemClickListener, Constants, OnScrollChangedListener {
    LinearLayout ll_title;
    TextView tv_local;
    ImageView iv_saosao;
    TextView tv_search;

    private LinearLayout ll_notice, ll_bottom, ll_top;
    //listview
    private List<ModelProduct> listData = new ArrayList<>();
    private CommonAdapter adapter;


    //viewpager_top
    private ViewPager vp_top;
    private LinearLayout pointlayout;
    private List<ModelHome> dataPager = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<ImageView>();
    private PagerAdapterHome pagerAdapterHome;

    //viewpager_mid
    private ViewPager vp_mid;
    private LinearLayout pointlayout_mid;
    private List<ModelCategory> dataPagerMid = new ArrayList<>();
    private List<ImageView> listImgMid = new ArrayList<ImageView>();
    private PagerAdapterCategory pagerAdapterCategory;

    private static AMapLocationClient mlocationClient;
    private static AMapLocationClientOption mLocationOption;

    //获取列表
    private ParseListProtocol<ModelProduct> parsePanoList;
    private IResponseCallback<DataSourceModel<ModelProduct>> callbackPanoList;


    ModelAddress modelAddress;
    //轮播图定时器
    TimerAlarm timerAlarm;


    public static int codeGetCity = 0x009888;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }


    @Override
    public void onScrollChanged(int top, int oldTop) {
        int mBuyLayout2ParentTop = Math.max(top, ll_top.getTop() + Utils.typedValueDP(mActivity, 150));
        ll_bottom.layout(0, mBuyLayout2ParentTop, ll_bottom.getWidth(), mBuyLayout2ParentTop + ll_bottom.getHeight());

        if (top > Utils.typedValueDP(mActivity, 150)) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initView(View v) {

        super.initView(v);

        ll_bottom = (LinearLayout) v.findViewById(R.id.ll_bottom);
        ll_bottom.setOnClickListener(this);
        ll_top = (LinearLayout) v.findViewById(R.id.ll_top);

        tv_local = (TextView) v.findViewById(R.id.tv_local);
        tv_local.setOnClickListener(this);
        iv_saosao = (ImageView) v.findViewById(R.id.iv_saosao);
        iv_saosao.setOnClickListener(this);
        tv_search = (TextView) v.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(this);
        ll_title = (LinearLayout) v.findViewById(R.id.ll_title);
        mScrollView.setOnScrollChangedListener(this);
        modelAddress = MyApplication.getInstance().modelAddress;

        ll_notice = (LinearLayout) v.findViewById(R.id.ll_notice);
        //listview
        adapter = new CommonAdapter(getActivity(), listData, R.layout.item_home_store) {
            @Override
            public void getView(int position, ViewHolderHelper holder) {
                TextView tv_name = holder.findViewById(R.id.tv_name);
                ImageView iv_store = holder.findViewById(R.id.iv_store);
                RatingBar rb_star = holder.findViewById(R.id.rb_star);
                TextView tv_score = holder.findViewById(R.id.tv_score);
                TextView tv_zhekou = holder.findViewById(R.id.tv_zhekou);
                TextView tv_commit = holder.findViewById(R.id.tv_commit);
                TextView tv_distance = holder.findViewById(R.id.tv_distance);
                tv_score.setText(listData.get(position).getAve_score() + "分");
                rb_star.setProgress((int) Double.parseDouble(listData.get(position).getAve_score()));
                tv_name.setText(listData.get(position).getName());
                tv_zhekou.setText("返利折扣: " + listData.get(position).getIntegral_ratio() + "%");
                tv_commit.setText(listData.get(position).getComment_num() + "评论");
                tv_distance.setText(listData.get(position).getDistance());
                ImageFactory.displayImage(listData.get(position).getHead_img(), iv_store, R.mipmap.iv_fail_mid, R.mipmap.iv_fail_mid);
            }
        };
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(adapter);
        measureListHeight();


        //轮播图
        vp_top = (ViewPager) v.findViewById(R.id.vp_top);
        pointlayout = (LinearLayout) v.findViewById(R.id.pointlayout);
        pagerAdapterHome = new PagerAdapterHome(mActivity);
        vp_top.setAdapter(pagerAdapterHome);

        //分类
        vp_mid = (ViewPager) v.findViewById(R.id.vp_mid);
        pointlayout_mid = (LinearLayout) v.findViewById(R.id.pointlayout_mid);
        pagerAdapterCategory = new PagerAdapterCategory(mActivity);
        vp_mid.setAdapter(pagerAdapterCategory);

        timerAlarm = new TimerAlarm(new TimerAlarm.CallLisener() {
            @Override
            public void call(int code) {
                if (dataPager.size() > 0) {
                    if (vp_top.getCurrentItem() == dataPager.size() - 1) {
                        vp_top.setCurrentItem(0);
                    } else {
                        vp_top.setCurrentItem(vp_top.getCurrentItem() + 1);
                    }

                    setdefault(listImg);
                    if (vp_top.getCurrentItem() < listImg.size())
                        listImg.get(vp_top.getCurrentItem()).setBackgroundResource(
                                R.drawable.point_home_normal_select);
                }
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        timerAlarm.stop();
    }

    @Override
    public void onStart() {
        super.onStart();
        timerAlarm.start();
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
            } else {
                final AlertDialog myDialog = new AlertDialog.Builder(getActivity()).create();
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
                tv_local.setText("定位失败");
                return;
            }

            MyApplication.getInstance().modelAddress = model;
            modelAddress = model;
            tv_local.setText(modelAddress.getAddress());
            mlocationClient.stopLocation();
            if (listData.size() == 0)
                getData();

        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", listData.get(position).getId());
        startActivity(getActivity(), ActivityStoreDetail.class, bundle);
    }


    @Override
    protected void initData() {
        super.initData();

        getDataPager();

        getDataCateroty();

        parsePanoList = new ParseListProtocol<>(mActivity, SERVER_URL_NEW + STORE_LIST, ModelProduct.class);
        callbackPanoList = new IResponseCallback<DataSourceModel<ModelProduct>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelProduct> data) {
                if (!getActivity().isFinishing()) {
                    isBusy = false;
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
                if (!getActivity().isFinishing()) {
                    isBusy = false;
                    isRefresh = false;
                    onLoad();
                }
            }

            @Override
            public void onStart() {
                isBusy = true;
            }
        };

        if (modelAddress != null) {
            tv_local.setText(modelAddress.getAddress());
            if (listData.size() == 0)
                getData();
        } else {
            mlocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(mapLocationListener);
            mLocationOption.setInterval(2000);
            mlocationClient.setLocationOption(new AMapLocationClientOption());
            mlocationClient.startLocation();
        }

    }

    /**
     */
    protected void getData() {

        Map<String, String> maps = new HashMap<>();
        if (modelAddress != null) {
            maps.put("city_id", modelAddress.getCity());
            maps.put("longitude", modelAddress.getLongitude() + "");
            maps.put("latitude", modelAddress.getLatitude() + "");
        }
        maps.put("pagesize", pageSize + "");
        maps.put("p", pageIndex + "");
        if (parsePanoList != null)
            parsePanoList.postData(maps, callbackPanoList);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_local:
                Bundle bundle = new Bundle();
                if (modelAddress != null)
                    bundle.putString("cityname", modelAddress.getCity());
                startActivityForResult(mActivity, ActivityCity.class, bundle, codeGetCity);
                break;
            case R.id.iv_saosao:
                if (application.getUserModel() == null && application.refreshUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ActLogin.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ActivityScanCode.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_search:
//                startActivity(mActivity, ActivityCommit.class, null);
                startActivity(mActivity, ActivitySearch.class, null);
                break;
            case R.id.ll_bottom:
//                startActivity(mActivity, ActivityCommit.class, null);
                startActivity(mActivity, ActivitySearch.class, null);
                break;
        }
    }

    //获取轮播图数据
    private void getDataPager() {

        ParseListProtocol<ModelHome> parseListProtocol = new ParseListProtocol(getActivity(), SERVER_URL_NEW + HOME_ADS, ModelHome.class);
        parseListProtocol.getData(new HashMap(), new IResponseCallback<DataSourceModel<ModelHome>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelHome> data) {
                if (data.list.size() > 0) {
                    dataPager.addAll(data.list);
                    showDataPager(dataPager);
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                showDataPager(dataPager);
            }

            @Override
            public void onStart() {

            }
        });
    }

    //获取分类
    private void getDataCateroty() {

        ParseListProtocol<ModelCategory> parseListProtocol = new ParseListProtocol(getActivity(), SERVER_URL_NEW + HOME_CATEGORY, ModelCategory.class);
        parseListProtocol.getData(new HashMap(), new IResponseCallback<DataSourceModel<ModelCategory>>() {
            @Override
            public void onSuccess(DataSourceModel<ModelCategory> data) {
                if (data.list.size() > 0) {
                    dataPagerMid.addAll(data.list);
                    showDataMid(dataPagerMid);
                }
            }

            @Override
            public void onFailure(ErrorModel errorModel) {
                showDataPager(dataPager);
            }

            @Override
            public void onStart() {

            }
        });
    }

    //viewpager填充
    private void showDataPager(List<ModelHome> list) {

        if (list.size() != listImg.size()) {
            pointlayout.removeAllViews();
            listImg.removeAll(listImg);
            for (int i = 0; i < list.size(); i++) {
                ImageView img = new ImageView(mActivity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        15, 15);
                img.setLayoutParams(lp);

                if (i == 0)
                    img.setBackgroundResource(R.drawable.point_home_normal_select);
                else
                    img.setBackgroundResource(R.drawable.point_home_normal);

                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                        15, 15);
                lp1.setMargins(0, 0, DipPixUtils.dip2px(mActivity, 5), 0);
                pointlayout.addView(img, lp1);
                listImg.add(img);
            }

        }

        //一个时不显示圆点
        if (list.size() == 1) {
            pointlayout.setVisibility(View.GONE);
        }

        pagerAdapterHome.setDatas(list);
        vp_top.setCurrentItem(0);
        vp_top.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setdefault(listImg);
                if (position < listImg.size())
                    listImg.get(position).setBackgroundResource(
                            R.drawable.point_home_normal_select);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    //分类填充
    private void showDataMid(List<ModelCategory> list) {

        if (list.size() != listImgMid.size()) {
            pointlayout_mid.removeAllViews();
            listImgMid.removeAll(listImgMid);
            for (int i = 0; i < Math.ceil((float) list.size() / 10); i++) {
                ImageView img = new ImageView(mActivity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        15, 15);
                img.setLayoutParams(lp);

                if (i == 0)
                    img.setBackgroundResource(R.drawable.point_home_normal_select);
                else
                    img.setBackgroundResource(R.drawable.point_home_normal);

                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                        15, 15);
                lp1.setMargins(0, 0, DipPixUtils.dip2px(mActivity, 5), 0);
                pointlayout_mid.addView(img, lp1);
                listImgMid.add(img);
            }

        }

        //一个时不显示圆点
        if (list.size() == 1) {
            pointlayout_mid.setVisibility(View.GONE);
        }

        pagerAdapterCategory.setDatas(list);
        vp_mid.setCurrentItem(0);
        vp_mid.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                setdefault(listImgMid);
                if (position < listImgMid.size())
                    listImgMid.get(position).setBackgroundResource(
                            R.drawable.point_home_normal_select);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setdefault(List<ImageView> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setBackgroundResource(R.drawable.point_home_normal);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UtilDialog.dismiss();
        mlocationClient.unRegisterLocationListener(mapLocationListener);
        mlocationClient.stopLocation();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //在城市页选择城市后的处理,和上次不是同一个城市则重新获取
        if (requestCode == codeGetCity) {
            modelAddress = MyApplication.getInstance().modelAddress;
            tv_local.setText(modelAddress.getAddress());
            onRefresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
