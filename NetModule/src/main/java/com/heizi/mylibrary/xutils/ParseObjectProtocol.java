package com.heizi.mylibrary.xutils;

import android.content.Context;

import com.google.gson.Gson;
import com.heizi.mylibrary.model.DataSourceModel;

import org.json.JSONObject;


/**
 * 解析 jsonobject 或者 与jsonarray 混合的数据
 * 
 * @author leo
 * 
 * @param <T>
 */
public class ParseObjectProtocol<T> extends BaseProtocol<DataSourceModel<T>> {
	private Class<T> mClass;
	private DataSourceModel<T> dataSourceModel;

	public ParseObjectProtocol(Context context, Class<T> mClass) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mClass = mClass;
		dataSourceModel = new DataSourceModel<T>();
	}

	@Override
	protected DataSourceModel<T> parseJson(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			Gson gson = new Gson();
			dataSourceModel.temp = gson.fromJson(jsonObject.getString("data"),
					mClass);
			dataSourceModel.status = jsonObject.getInt("ret");
			// dataSourceModel.info = jsonObject.getString("msg");
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSourceModel;
	}
}
