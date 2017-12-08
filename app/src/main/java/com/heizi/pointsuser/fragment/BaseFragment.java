package com.heizi.pointsuser.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heizi.mycommon.callback.FragmentCallback;
import com.heizi.mycommon.utils.LoadingE;
import com.heizi.mycommon.utils.Utils;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.UserModel;
import com.heizi.pointsuser.UserRecord;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements Constants {
    protected LayoutInflater inflater;
    private View contentView;
    private Context context;
    private ViewGroup container;
    public Activity mActivity;

    protected LinearLayout title_include;
    protected ImageView btn_back, iv_right;
    protected TextView tv_title, tv_right, tv_right1;
    protected View v_diviling;
    protected LoadingE mDialog;

    protected UserRecord userRecord;
    protected UserModel userModel;
    protected FragmentCallback fragmentCallback;

    protected MyApplication application;

    protected String TAG = getClass().getSimpleName();

    public void setFragmentCallback(FragmentCallback callback) {
        this.fragmentCallback = callback;
    }

    protected boolean isBusy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        mActivity = getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        application = MyApplication.getInstance();
        userModel = application.getUserModel();
//        mTabsActivity = (MainTabsActivity) activity;
//        mTabsActivity.setBottomVisible(false);
//        // 取消隐藏通知栏
//        mTabsActivity.getWindow().clearFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        userRecord = MyApplication.getInstance().getUserRecord();
//        bitmapUtilsBase = new BitmapUtils(mTabsActivity, IMAGE_CACHE);
        // bitmapUtilsBase.configDefaultLoadingImage(R.drawable.banner_temp);
        // bitmapUtilsBase.configDefaultLoadFailedImage(R.drawable.banner_temp);
    }

    @Override
    @Nullable
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.inflater = inflater;
        this.container = container;
        return onCreateView(savedInstanceState);
    }

    protected View onCreateView(Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutResource(), null);
        ButterKnife.inject(this, contentView);
        initView(contentView);
        initData();
        return contentView;
    }


    protected abstract int getLayoutResource();

    protected void initView(View v) {
        title_include = (LinearLayout) v.findViewById(R.id.include);
        btn_back = (ImageView) v.findViewById(R.id.btn_back);
        iv_right = (ImageView) v.findViewById(R.id.iv_right);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_right = (TextView) v.findViewById(R.id.tv_right);
        tv_right1 = (TextView) v.findViewById(R.id.tv_right1);
        v_diviling = (View) v.findViewById(R.id.v_diviling);
        mDialog = new LoadingE(getActivity());
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isBusy && !getClass().getSimpleName().equals("MainTabsActivity")) {
                    mDialog.hideDialog();
                    getActivity().finish();
                }
            }
        });
    }

    protected void initData() {

    }

    ;

//    @OnClick(R.id.btn_back)
//    public void backClick() {
//        mTabsActivity.backFragment();
//    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        Utils.hideInputMethod(mActivity);
        ButterKnife.reset(this);
        contentView = null;
        container = null;
        inflater = null;
    }

    public Context getApplicationContext() {
        return context;
    }

    public void setContentView(int layoutResID) {
        setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
    }

    public void setContentView(View view) {
        contentView = view;
    }

    public View getContentView() {
        return contentView;
    }


    protected void startActivityForResult(Context packageContext, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivity(Context packageContext, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setTitleVisible(boolean visible) {
        if (visible)
            title_include.setVisibility(View.VISIBLE);
        else
            title_include.setVisibility(View.GONE);
    }


}
