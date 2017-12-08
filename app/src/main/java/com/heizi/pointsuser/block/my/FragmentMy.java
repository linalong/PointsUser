package com.heizi.pointsuser.block.my;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mycommon.utils.SystemUtil;
import com.heizi.mycommon.view.NoticeTextView;
import com.heizi.mycommon.view.RoundImageView;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.UserModel;
import com.heizi.pointsuser.activity.MainTabsActivity;
import com.heizi.pointsuser.block.login.ActChangePwd;
import com.heizi.pointsuser.block.login.ActLogin;
import com.heizi.pointsuser.fragment.BaseFragment;
import com.heizi.pointsuser.utils.RefreshUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by leo on 17/5/29.
 */

public class FragmentMy extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.ll_1)
    LinearLayout ll_1;//我的邀请码
    @InjectView(R.id.ll_2)
    LinearLayout ll_2;//分享软件
    @InjectView(R.id.ll_3)
    LinearLayout ll_3;//收藏店铺
    @InjectView(R.id.ll_4)
    LinearLayout ll_4;//修改密码
    @InjectView(R.id.ll_5)
    LinearLayout ll_5;//个人信息
    @InjectView(R.id.ll_6)
    LinearLayout ll_6;//联系我们
    @InjectView(R.id.ll_7)
    LinearLayout ll_7;//注销登录
    @InjectView(R.id.ll_8)
    LinearLayout ll_8;//意见反馈
    @InjectView(R.id.ll_9)
    LinearLayout ll_9;//奖励信息


    @InjectView(R.id.ll_mypoint)
    LinearLayout ll_mypoint;//我的积分
    @InjectView(R.id.ll_7)
    LinearLayout ll_givepoint;//积分转赠
    @InjectView(R.id.ll_7)
    LinearLayout ll_rule;//积分规则

    @InjectView(R.id.iv_avatar)
    RoundImageView iv_avatar;
    @InjectView(R.id.nv_msg)
    NoticeTextView nv_msg;
    @InjectView(R.id.tv_name)
    TextView tv_name;

    RefreshUtils refreshUtils;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View v) {
        super.initView(v);

        if (userModel != null)
            setData(userModel);
        refreshUtils = new RefreshUtils();
        refreshUtils.registerBoradcastReceiver(mActivity, new RefreshUtils.refreshCallback() {
            @Override
            public void call(UserModel userModel) {
                setData(userModel);
            }
        });
    }

    private void setData(UserModel userModel) {
        tv_name.setText(userModel.getNickname());
        if (!TextUtils.isEmpty(userModel.getHead_img())) {
            ImageFactory.displayImage(userModel.getHead_img(), iv_avatar, R.mipmap.photo, R.mipmap.photo);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshUtils.unregisterBoradcastReceiver(getActivity());
    }

    @OnClick({R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6, R.id.ll_7, R.id.ll_8, R.id.ll_9, R.id.ll_mypoint, R.id.ll_givepoint, R.id.ll_rule,R.id.nv_msg})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ll_1:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityMyInvite.class, null);
                }

                break;
            case R.id.ll_2:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    new ShareAction(getActivity())
                            .withText("hello")
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                            .setCallback(new UMShareListener() {
                                @Override
                                public void onStart(SHARE_MEDIA share_media) {

                                }

                                @Override
                                public void onResult(SHARE_MEDIA share_media) {
                                    Toast.makeText(getActivity(), "成功了", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    Toast.makeText(getActivity(), "失败" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {
                                    Toast.makeText(getActivity(), "取消了", Toast.LENGTH_LONG).show();
                                }
                            })
                            .open();
                }

                break;

            case R.id.ll_3:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityMyCollect.class, null);
                }
                break;

            case R.id.ll_4:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActChangePwd.class, null);
                }
                break;

            case R.id.ll_5:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityUserInfo.class, null);
                }

                break;

            case R.id.ll_6:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    SystemUtil.call(getActivity(), userModel.getService_phone());
                }

                break;

            case R.id.ll_7:
                application.logout();
                Toast.makeText(getActivity(), "注销成功", Toast.LENGTH_SHORT).show();
                MainTabsActivity.getInstance().changeToMain();

                break;
            case R.id.ll_8:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityFeedback.class, null);
                }
                break;
            case R.id.ll_9:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityReward.class, null);
                }

                break;

            case R.id.ll_mypoint:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityMyPoint.class, null);
                }
                break;

            case R.id.ll_givepoint:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityGivePoint.class, null);
                }
                break;
            case R.id.ll_rule:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityPointsExplain.class, null);
                }
                break;
            case R.id.nv_msg:
                if (application.getUserModel() == null) {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, ActLogin.class);
                    startActivity(intent);
                } else {
                    startActivity(mActivity, ActivityMessageList.class, null);
                }
                break;

        }
    }
}
