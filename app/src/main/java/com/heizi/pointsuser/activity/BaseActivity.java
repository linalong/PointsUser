package com.heizi.pointsuser.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heizi.mycommon.utils.LoadingE;
import com.heizi.mycommon.utils.Utils;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.MyApplication;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.UserModel;
import com.heizi.pointsuser.utils.RefreshUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public abstract class BaseActivity extends FragmentActivity implements Constants {

    private final int SDK_PERMISSION_REQUEST = 127;
    protected LinearLayout title_include;
    protected ImageView btn_back, btn_close, iv_right, iv_right1;
    protected TextView tv_title, tv_right, tv_right1;
    protected View v_diviling;

    protected UserModel userModel;
    protected MyApplication application;
    protected View contentView;

    protected LoadingE mDialog;

    protected String TAG = getClass().getSimpleName();

    protected RefreshUtils refreshUtils;

    protected boolean isBusy, isExist = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.right_in_anim, R.anim.right_out_anim);
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setTranslucentStatus(true);
//        setSystemBarTintDrawable(getResources().getDrawable(R.drawable.dwPrimary));
        contentView = LayoutInflater.from(this).inflate(getLayoutResource(), null);
        setContentView(contentView);
        ButterKnife.inject(this);
        application = MyApplication.getInstance();
        refreshUtils = new RefreshUtils();
        refreshUtils.registerBoradcastReceiver(this, new RefreshUtils.refreshCallback() {
            @Override
            public void call(UserModel model) {
                userModel = model;
            }
        });
        mDialog = new LoadingE(this);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isBusy) {
                    mDialog.hideDialog();
                    finish();
                }
            }
        });
        getPersimmions();
        isExist = true;

    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            // 读写权限
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            //摄像头
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授权，则请求授权
                permissions.add(Manifest.permission.CAMERA);
            }
//            /*
//             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
//			 */
//
//            // 读取电话状态权限
//            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
//            }
            // 读写权限
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CALL_PHONE);
            }

//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);


            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            } else {
                userModel = application.getUserModel();
                initView();
                initData();
            }
        } else {
            userModel = application.getUserModel();
            initView();
            initData();
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }


//    /**
//     * 申请读写sd卡权限
//     */
//    public void getDBPermisson() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
//        } else {
//            application.refreshUserModel();
//            application.setModelMoban(DbUtils.getDefault());
//            userModel = application.getUserModel();
//            initView();
//            initData();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == SDK_PERMISSION_REQUEST) {
            userModel = application.getUserModel();
            initView();
            initData();
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission Denied
                Toast.makeText(BaseActivity.this, "缺少权限将会导致部分功能无法正常使用", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    protected abstract int getLayoutResource();

    protected void initView() {
        title_include = (LinearLayout) findViewById(R.id.include);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        btn_close = (ImageView) findViewById(R.id.btn_close);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right1 = (ImageView) findViewById(R.id.iv_right1);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right1 = (TextView) findViewById(R.id.tv_right1);
        v_diviling = (View) findViewById(R.id.v_diviling);
//        btn_back.setOnClickListener(this);
    }

    protected void initData() {

    }


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_back:
//                finish();
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.hideInputMethod(this);
        refreshUtils.unregisterBoradcastReceiver(this);
        isExist = false;
    }

    @Override
    public void finish() {
        super.finish();
        ButterKnife.reset(this);
        if (!getClass().getSimpleName().equals("MainTabsActivity"))
            overridePendingTransition(R.anim.right_in_anim, R.anim.right_out_anim);
    }

    // /////// 点击空白区域消失软键盘开始///////////////////////
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    // /////// 点击空白区域消失软键盘结束///////////////////////


    public void setTitleVisible(boolean visible) {
        if (visible)
            title_include.setVisibility(View.VISIBLE);
        else
            title_include.setVisibility(View.GONE);
    }

    /**
     * set status bar translucency
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    /**
     * use SytemBarTintManager
     *
     * @param tintDrawable
     */
    public void setSystemBarTintDrawable(Drawable tintDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null) {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fm = getSupportFragmentManager();
//        int index = requestCode >> 16;
//        if (index != 0) {
//            index--;
//            if (fm.getFragments() == null || index < 0
//                    || index >= fm.getFragments().size()) {
//                Log.w(TAG, "Activity result fragment index out of range: 0x"
//                        + Integer.toHexString(requestCode));
//                return;
//            }
        List<Fragment> frags = fm.getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null) {
                    f.onActivityResult(requestCode & 0xffff, resultCode, data);
//                    handleResult(f, requestCode, resultCode, data);
                }

            }
        }
//            Fragment frag = fm.getFragments().get(index);
//            if (frag == null) {
//                Log.w(TAG, "Activity result no fragment exists for index: 0x"
//                        + Integer.toHexString(requestCode));
//            } else {
//
//            }
//            return;
//        }

    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode,
                              Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    protected void startActivity(Context packageContext, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }


    public void startActivityForResult(Context packageContext, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(packageContext, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    protected void finishForResult(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null)
            intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

}
