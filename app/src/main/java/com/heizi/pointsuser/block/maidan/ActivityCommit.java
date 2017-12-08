package com.heizi.pointsuser.block.maidan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.heizi.mycommon.utils.StringUtils;
import com.heizi.mycommon.utils.Utils;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;
import com.heizi.pointsuser.activity.MainTabsActivity;
import com.heizi.pointsuser.block.my.ActivityMyPoint;
import com.heizi.pointsuser.utils.SelectPhotoUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论页
 * Created by leo on 17/9/18.
 */

public class ActivityCommit extends BaseSwipeBackCompatActivity {

    private GridView mGrid;
    private DeviceSignedActionAdapter adapter;
    List<DeviceSignedActionImage> dataList = new ArrayList<>();

    SelectPhotoUtils selectPhotoUtils;

    Button btn_ok;
    TextView tv_num;
    EditText et_remark;
    RatingBar rb_star, rb_star1, rb_star2, rb_star3;

    String seller_id = "";

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_commit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectPhotoUtils = new SelectPhotoUtils(this, null, btn_back,
                PHOTO_PICKED_WITH_DATA, CAMERA_WITH_DATA);
        if (getIntent().getExtras() != null) {
            seller_id = getIntent().getExtras().getString("seller_id");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("发表评论");
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mGrid = (GridView) findViewById(R.id.grid_view_device_signed_action);//图片
        adapter = new DeviceSignedActionAdapter(this);
        adapter.setShow(false);
        mGrid.setAdapter(adapter);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (position < 3) {
                    DeviceSignedActionImage info = (DeviceSignedActionImage) adapter.getItem(position);
                    if (info.getType() == 1) {
                        selectPhotoUtils.ShowPop1(btn_back);
                    } else {
                        if (info.getIsSusses() == 1) {
                            //重新上传图片
                            dataList.remove(position);
                            selectPhotoUtils.ShowPop1(btn_back);
                        }
//                        else if (info.getIsSusses() == 0) {
//                            //查看图片
//                            Intent intent = new Intent(DeviceSignedActionActivity.this, ImageViewShowActivity.class);
//                            //intent.putExtra("url", dataList.get(position));
//                            DeviceSignedActionImage itemData = dataList.get(position);
//                            String url = "";
//                            if (itemData.getLocalUrl() != null) {
//                                url = itemData.getLocalUrl();
//                            } else if (itemData.getHttpUrl() != null) {
//                                url = itemData.getHttpUrl();
//                            }
//                            intent.putExtra("url", url);
//                            startActivity(intent);
//                        }
                    }
                }
//                else {
//                    showShortToast("不好意思，暂时只能上传5张图片");
//                }
            }
        });

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //有图片先上传图片,无图片直接提交数据
                if (dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getIsSusses() != 0) {
                            dataList.get(0).setIsSusses(2);
                            upLoadImg(dataList.get(0).getLocalUrl());
                            return;
                        }
                    }
                    upload();
                } else {
                    upload();
                }
            }
        });

        rb_star = (RatingBar) findViewById(R.id.rb_star);
        rb_star1 = (RatingBar) findViewById(R.id.rb_star1);
        rb_star2 = (RatingBar) findViewById(R.id.rb_star2);
        rb_star3 = (RatingBar) findViewById(R.id.rb_star3);
        tv_num = (TextView) findViewById(R.id.tv_num);
        et_remark = (EditText) findViewById(R.id.et_remark);
        StringUtils.setProhibitEmoji(et_remark);
        et_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_num.setText("还能输入" + (100 - s.length()) + "字符");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 100) {
                    et_remark.setText(s.subSequence(0, 100));
                    tv_num.setText("还能输入" + 0 + "字符");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == PHOTO_PICKED_WITH_DATA) {// 从相册选择图片
                    try {
                        Uri uri = data.getData();
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = managedQuery(uri, proj,
                                null, null, null);
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        String filePath = cursor.getString(column_index);
                        loadPictrue(filePath);

                    } catch (Exception e) {
                    }
                }
            }
            if (requestCode == CAMERA_WITH_DATA) {// 相机
                String filePath = selectPhotoUtils.getFilePath();
                Uri imgUri = selectPhotoUtils.getImgUri();
                try {
                    if (filePath != null && imgUri != null) {
                        loadPictrue(filePath);
                    }
                } catch (Exception e) {
                }
            }

        }
    }


    public void loadPictrue(final String file) {
        DeviceSignedActionImage uploadImageNew = new DeviceSignedActionImage();
        uploadImageNew.setLocalUrl(file);
        dataList.add(uploadImageNew);
        adapter.setData(dataList);
    }

    /**
     * 上传图片
     */
    private void upLoadImg(String localFilePath) {

        AjaxParams params = new AjaxParams();
        try {
            params.put("image", new File(localFilePath));
            params.put("token", userModel.getToken());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FinalHttp fh = new FinalHttp();
        fh.configTimeout(20000);
        fh.post(SERVER_URL_NEW + UOLOAD_IMG, params, callBack);
    }


    private AjaxCallBack<String> callBack = new AjaxCallBack<String>() {
        public void onStart() {
            isBusy = true;
            mDialog.showDialog();
        }

        @Override
        public void onSuccess(String json) {
            if (!isFinishing()) {
                isBusy = false;
                String result = "";
                mDialog.hideDialog();
                try {

                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
                    int code = jsonObject.getInt("code");
                    result = jsonObject1.getString("file");
                    if (code == 200) {
                        Log.d("==", "上传图片返回数据" + json);
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getIsSusses() == 2) {
                                DeviceSignedActionImage deviceSignedActionImage =
                                        dataList.get(i);
                                deviceSignedActionImage.setHttpUrl("" + result);
                                deviceSignedActionImage.setIsSusses(0);
                                break;
                            }
                        }
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getIsSusses() != 0) {
                                dataList.get(i).setIsSusses(2);
                                upLoadImg(dataList.get(i).getLocalUrl());
                                return;
                            }
                        }

                        upload();
                    } else {
                        Utils.toastShow(ActivityCommit.this, "上传图片失败");
                        for (int i = 0; i < dataList.size(); i++) {
                            if (dataList.get(i).getIsSusses() == 2) {
                                dataList.get(i).setIsSusses(1);
                                break;
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public void onFailure(Throwable t, int errorNo, String strMsg) {
            if (!isFinishing()) {
                isBusy = false;
                mDialog.hideDialog();
                Utils.toastShow(ActivityCommit.this, "上传图片失败");
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).getIsSusses() == 2) {
                        dataList.get(i).setIsSusses(1);
                        break;
                    }
                }
            }
        }
    };

    private void upload() {
        if (TextUtils.isEmpty(et_remark.getText().toString())) {
            Utils.toastShow(ActivityCommit.this, "请填写您的评价");
            return;
        } else if (rb_star.getRating() == 0) {
            Utils.toastShow(ActivityCommit.this, "请勾选评分");
            return;
        }
        ParseStringProtocol parseStringProtocol = new ParseStringProtocol(this, SERVER_URL_NEW + ADDCOMMENT);
        Map<String, String> map = new HashMap<>();
        map.put("comment", et_remark.getText().toString());
        map.put("score", (int) rb_star.getRating() + "");

        map.put("server_score", rb_star1.getRating() + "");
        map.put("product_score", rb_star2.getRating() + "");
        map.put("milieu_score", rb_star3.getRating() + "");

        if (dataList.size() == 1) {
            map.put("image1", dataList.get(0).getHttpUrl());
            map.put("image2", "");
            map.put("image3", "");
        } else if (dataList.size() == 2) {
            map.put("image1", dataList.get(0).getHttpUrl());
            map.put("image2", dataList.get(1).getHttpUrl());
            map.put("image3", "");
        } else if (dataList.size() == 3) {
            map.put("image1", dataList.get(0).getHttpUrl());
            map.put("image2", dataList.get(1).getHttpUrl());
            map.put("image3", dataList.get(2).getHttpUrl());
        } else {
            map.put("image1", "");
            map.put("image2", "");
            map.put("image3", "");
        }

        map.put("seller_id", seller_id);
        map.put("token", userModel.getToken());
        parseStringProtocol.postData(map, new IResponseCallback<DataSourceModel<String>>() {
                    @Override
                    public void onSuccess(DataSourceModel<String> data) {
                        if (!isFinishing()) {
                            isBusy = false;
                            mDialog.hideDialog();
                            Utils.toastShow(ActivityCommit.this, "感谢您的评价");
                            MainTabsActivity.getInstance().changeToMy();
                            startActivity(ActivityCommit.this, ActivityMyPoint.class, null);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(ErrorModel errorModel) {
                        if (!isFinishing()) {
                            isBusy = false;
                            mDialog.hideDialog();
                            btn_ok.setEnabled(true);
                            Utils.toastShow(ActivityCommit.this, "评价失败");
                        }
                    }

                    @Override
                    public void onStart() {
                        isBusy = true;
                        mDialog.showDialog();
                    }
                }

        );
    }
}
