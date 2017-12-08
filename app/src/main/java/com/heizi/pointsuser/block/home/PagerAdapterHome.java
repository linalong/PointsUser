package com.heizi.pointsuser.block.home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.pointsuser.R;

import java.util.ArrayList;
import java.util.List;


public class PagerAdapterHome extends PagerAdapter {
    private String[] CONTENT;
    private List<ModelHome> mDatas = new ArrayList<>();
    private Context mContext;
    private List<View> mViews = new ArrayList<>();

    public PagerAdapterHome(Context mContext) {
        this.mContext = mContext;

    }


    /**
     * 设置数据
     *
     * @param datas
     */
    public void setDatas(List<ModelHome> datas) {
        if (this.mDatas.size() > 0) {
            this.mDatas.removeAll(this.mDatas);
            mViews.removeAll(mViews);
        }
        this.mDatas.addAll(datas);

        for (int i = 0; i < mDatas.size(); i++) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_home_lunbo, null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ImageView iv = (ImageView) v.findViewById(R.id.iv);
            ModelHome model = mDatas.get(i);

            ImageFactory.displayImage(model.getImage_url(), iv, R.mipmap.iv_fail_big, R.mipmap.iv_fail_big);


            mViews.add(v);
        }

        notifyDataSetChanged();


    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return (CONTENT == null) ? "" : CONTENT[position % CONTENT.length];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
