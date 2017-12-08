package com.heizi.pointsuser.block.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.pointsuser.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页分类
 */
public class PagerAdapterCategory extends PagerAdapter {
    private String[] CONTENT;
    private List<ModelCategory> mDatas = new ArrayList<>();
    private Context mContext;
    private List<View> mViews = new ArrayList<>();

    int num = 10;

    public PagerAdapterCategory(Context mContext) {
        this.mContext = mContext;

    }


    /**
     * 设置数据
     *
     * @param datas
     */
    public void setDatas(List<ModelCategory> datas) {
        if (this.mDatas.size() > 0) {
            this.mDatas.removeAll(this.mDatas);
            mViews.removeAll(mViews);
        }
        this.mDatas.addAll(datas);

        if (mDatas.size() % (num / 2) == 1) {
            ModelCategory modelCategory = new ModelCategory();
            mDatas.add(modelCategory);
            mDatas.add(modelCategory);
            mDatas.add(modelCategory);
            mDatas.add(modelCategory);
        } else if (mDatas.size() % (num / 2) == 2) {
            ModelCategory modelCategory = new ModelCategory();
            mDatas.add(modelCategory);
            mDatas.add(modelCategory);
            mDatas.add(modelCategory);
        } else if (mDatas.size() % (num / 2) == 3) {
            ModelCategory modelCategory = new ModelCategory();
            mDatas.add(modelCategory);
            mDatas.add(modelCategory);
        } else if (mDatas.size() % (num / 2) == 4) {
            ModelCategory modelCategory = new ModelCategory();
            mDatas.add(modelCategory);
        }


        //分类 每页8个
        for (int i = 0; i < Math.ceil((float) mDatas.size() / num); i++) {
            View ll = LayoutInflater.from(mContext).inflate(R.layout.item_home_category, null);
            LinearLayout ll_top = (LinearLayout) ll.findViewById(R.id.ll_top);
            LinearLayout ll_bom = (LinearLayout) ll.findViewById(R.id.ll_bom);
            for (int j = 0; j < num; j++) {
                if (i * num + j < mDatas.size()) {
                    View v = LayoutInflater.from(mContext).inflate(R.layout.item_home_category_item, null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.weight = 1;
                    v.setLayoutParams(params);
                    final int finalI = i;
                    final int finalJ = j;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, ActivityStoreList.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("title", mDatas.get(finalI * num + finalJ).getName());
                            bundle.putString("categoryId", mDatas.get(finalI * num + finalJ).getId());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    });

                    TextView tv = (TextView) v.findViewById(R.id.title);
                    tv.setText(mDatas.get(i * num + j).getName());

                    ImageView iv = (ImageView) v.findViewById(R.id.img);
                    ImageFactory.displayImage(mDatas.get(i * num + j).getIcon_url(), iv, 0, 0);

                    if (j < num / 2) {
                        ll_top.addView(v);
                    } else {
                        ll_bom.addView(v);
                    }
                } else {
                    break;
                }

            }
            mViews.add(ll);
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
        return mViews.size();
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
