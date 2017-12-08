package com.heizi.mylibrary.xutils;

import android.content.Context;


import com.heizi.mylibrary.model.DataSourceModel;

import org.json.JSONObject;


/**
 * 解析 data中直接就是String的情况
 * 
 * @author leo
 * 
 */
public class ParseStringProtocol extends BaseProtocol<DataSourceModel<String>> {
	private DataSourceModel<String> dataSourceModel;

	public ParseStringProtocol(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		dataSourceModel = new DataSourceModel<String>();
	}

	@Override
	protected DataSourceModel<String> parseJson(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
			dataSourceModel.temp = jsonObject.getString("data");
			dataSourceModel.status = jsonObject.getInt("ret");
			// dataSourceModel.info = jsonObject.getString("msg");
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSourceModel;
	}
}
