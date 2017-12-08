package com.heizi.pointsuser.block.maidan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.pointsuser.R;

import java.util.LinkedList;
import java.util.List;

import static com.heizi.pointsuser.Constants.SERVER_URL;


public class DeviceSignedActionAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private boolean isShow;

    private List<DeviceSignedActionImage> data = new LinkedList<DeviceSignedActionImage>();
    private DeviceSignedActionImage lastImge = new DeviceSignedActionImage("last_image", "last_image", 1, 0, null);

    public DeviceSignedActionAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        setDataList(data);
    }

    public void setData(List<DeviceSignedActionImage> dataList) {
        setDataList(dataList);
        notifyDataSetChanged();
    }

    private void setDataList(List<DeviceSignedActionImage> dataList) {
        if (dataList != null) {
            data.clear();
            data.addAll(dataList);
        }
        if (!isShow) {
            if (data.size() < 3) {
                data.add(lastImge);
            }

        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public DeviceSignedActionImage getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_device_signed_action_image, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setBuild(data, position);
        // TODO Auto-generated method stub
        return convertView;
    }

    class ViewHolder {
        private ImageView mIv;

        public ViewHolder(View v) {
            // TODO Auto-generated constructor stub
            mIv = (ImageView) v.findViewById(R.id.iv_select_pic3);
        }

        public void setBuild(List<DeviceSignedActionImage> images, int position) {
            // TODO Auto-generated method stub
            System.out.println("图片地址==" + images.toString());
            String imaLocal = images.get(position).getLocalUrl();
            String imaHttp = images.get(position).getHttpUrl();
            images.get(position).setProcessImageView(mIv);
            if (imaLocal != null) {
                if (imaLocal.equals("last_image") && !isShow) {//解决了图片乱跳的问题（本地图片和网络图片的判断的问题，imageloader会解决）
                    //最后一张图片
                    ImageFactory.displayImage("drawable://" + R.mipmap.image_add_icon, mIv, R.mipmap.image_add_icon, R.mipmap.image_add_icon);
                } else {
//                    mIv.setImageBitmap(ImageFactory.getBitmap(imaLocal));
                    //加载本地
                    ImageFactory.displayImage("file://" + imaLocal, mIv, R.mipmap.default_img_fail, R.mipmap.default_img_fail);

                }
            } else if (imaHttp != null) {
                if (imaHttp.equals("last_image") && !isShow) {
                    //最后一张图片
                    ImageFactory.displayImage("drawable://" + R.mipmap.image_add_icon, mIv, R.mipmap.image_add_icon, R.mipmap.image_add_icon);
                } else {
                    //加载网络
                    ImageFactory.displayImage(SERVER_URL + imaHttp, mIv, R.mipmap.default_img_fail, R.mipmap.default_img_fail);
                }
            }
        }

    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

}
