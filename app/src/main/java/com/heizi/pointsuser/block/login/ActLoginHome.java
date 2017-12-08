package com.heizi.pointsuser.block.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.heizi.mycommon.utils.DipPixUtils;
import com.heizi.mycommon.utils.ImageFactory;
import com.heizi.mycommon.utils.SharePreferenceUtil;
import com.heizi.mylibrary.callback.IResponseCallback;
import com.heizi.mylibrary.model.DataSourceModel;
import com.heizi.mylibrary.model.ErrorModel;
import com.heizi.mylibrary.retrofit2.ParseStringProtocol;
import com.heizi.pointsuser.Constants;
import com.heizi.pointsuser.R;
import com.heizi.pointsuser.activity.BaseSwipeBackCompatActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 登陆首页
 * 
 * @author admin
 * 
 */
public class ActLoginHome extends BaseSwipeBackCompatActivity implements OnClickListener,
		Constants {
	
	private SharePreferenceUtil su;
	private ParseStringProtocol pro;
	private IResponseCallback<DataSourceModel<String>> cb;
	private List<LoginImgModel> listData=new ArrayList<LoginImgModel>();
	private ViewPager viewpager;
	private ImageAdapter adapter;
	private List<View> viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
	private List<String> listUrl=new ArrayList<String>();
	private LinearLayout pointlayout;
	private List<ImageView> listImg=new ArrayList<ImageView>();
	private Button btn_login,btn_register;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getData();
	}

	@Override
	protected int getLayoutResource() {
		return R.layout.act_login_home;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btn_login){
			startActivity(new Intent(this,ActLogin.class));
		}else if(v==btn_register){
			Intent intent = new Intent();
			intent.setClass(this, ActRegister.class);
			intent.putExtra("from","loginhome");
			startActivity(intent);
		}
	}

	// 初始化控件
	protected void initView() {
		su = new SharePreferenceUtil(this);
		viewpager=(ViewPager)findViewById(R.id.viewpager);
		pointlayout = (LinearLayout) findViewById(R.id.pointlayout);
		btn_login=(Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_register=(Button)findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		adapter=new ImageAdapter(viewList, listUrl);
		viewpager.setAdapter(adapter);
		showData();
	}
	
	private void setdefault(List<ImageView> list) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setBackgroundResource(R.drawable.point_home_normal);
		}
	}
	
	private void showData(){
		for (int i = 0; i < listData.size(); i++) {
			listUrl.add(listData.get(i).getImg());
			ImageView img = new ImageView(ActLoginHome.this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
			img.setLayoutParams(lp);
			
			if (i == 0)
				img.setBackgroundResource(R.drawable.point_home_normal_select);
			else
				img.setBackgroundResource(R.drawable.point_home_normal);
			
			LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(15,15);
			lp1.setMargins(0, 0, DipPixUtils.dip2px(ActLoginHome.this, 8), 0);
			pointlayout.addView(img, lp1);
			listImg.add(img);
			View vp1 = LayoutInflater.from(ActLoginHome.this).inflate(R.layout.recommendviewpager, null);
			viewList.add(vp1);
		}
		adapter.setList(viewList,listUrl);
		adapter.notifyDataSetChanged();
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				setdefault(listImg);
				if(position<listImg.size())
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

	// 初始化数据
	protected void initData() {
		pro=new ParseStringProtocol(ActLoginHome.this,SERVER_URL);
		cb=new IResponseCallback<DataSourceModel<String>>() {
			
			@Override
			public void onSuccess(DataSourceModel<String> t) {
				// TODO Auto-generated method stub
				LoginImgModel sim=new LoginImgModel();
				sim.setId("");
				sim.setImg("");
				listData.add(sim);
				try {
					JSONObject json=new JSONObject(t.json);
					JSONArray array=json.getJSONArray("info");
					for(int i=0;i<array.length();i++){
						listData.add(new Gson().fromJson(array.getString(i), LoginImgModel.class));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(listData.size()>0)
					showData();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onFailure(ErrorModel errorModel) {
				// TODO Auto-generated method stub
			}
		};
	}

	// 获取数据
	private void getData() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("client", "android");
		maps.put("service", "Theme.Getshuffling");
		pro.getData(maps, cb);
	}
	
	public class ImageAdapter extends PagerAdapter {
		private List<View> mListViews;
		private List<String> urllist;

		public ImageAdapter(List<View> lv, List<String> ls) {
			this.mListViews = lv;// 构造方法，参数是我们的页卡，这样比较方便。
			this.urllist = ls;
		}

		public void setList(List<View> lv, List<String> ls){
			this.mListViews = lv;// 构造方法，参数是我们的页卡，这样比较方便。
			this.urllist = ls;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			if(position>=urllist.size()){
				return mListViews.get(urllist.size()-1);
			}
			final ImageView img = (ImageView) mListViews.get(position).findViewById(R.id.photo);
			if(!urllist.get(position).equals("")) {
				ImageFactory.displayImage(urllist.get(position), img, 0, 0);

			}else
				img.setImageResource(R.mipmap.login_home_bg);
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}

}
