package com.heizi.mylibrary.xutils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.heizi.mylibrary.model.DataSourceModel;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 解析纯jsonarray的数据
 * 
 * @author leo
 * 
 * @param <T>
 */
public class ParseListProtocol<T> extends BaseProtocol<DataSourceModel<T>> {
	private DataSourceModel<T> dataSourceModel;
	private Class<T> cls;
	private Gson mGson;

	public ParseListProtocol(Context context, Class<T> cls) {
		super(context);
		this.cls = cls;
		dataSourceModel = new DataSourceModel<T>();
		mGson = new Gson();
	}

	@Override
	protected DataSourceModel<T> parseJson(String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);

			JSONObject jsonObject2 = new JSONObject(
					jsonObject.getString("data"));

			dataSourceModel.list = fromJsonToList(
					jsonObject2.getString("info"), cls);
			dataSourceModel.status = jsonObject.getInt("ret");
			// dataSourceModel.info = jsonObject.getString("msg");
		} catch (org.json.JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataSourceModel;
	}

	public <T> ArrayList<T> fromJsonToList(String json, Class<T> cls) {
		ArrayList<T> mList = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for (final JsonElement elem : array) {
			mList.add(mGson.fromJson(elem, cls));
		}
		return mList;
	}
}
