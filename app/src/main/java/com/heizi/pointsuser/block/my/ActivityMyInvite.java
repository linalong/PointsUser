package com.heizi.pointsuser.block.my;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.heizi.mycommon.utils.Utils;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;

import butterknife.InjectView;

/**
 * Created by leo on 17/9/28.
 */

public class ActivityMyInvite extends BaseSwipeBackCompatActivity {

    @InjectView(R.id.tv_invite)
    TextView tv_invite;

    @InjectView(R.id.btn_copy)
    Button btn_copy;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_myinvite;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("我的邀请码");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tv_invite.getText().toString());
                Utils.toastShow(ActivityMyInvite.this, "复制成功，可以发给朋友们了。").show();
            }
        });
        tv_invite.setText(userModel.getInvitation_code());
    }

    @Override
    protected void initData() {
        super.initData();
    }

}
