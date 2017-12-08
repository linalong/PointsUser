package com.heizi.mylibrary.retrofit2;

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
 * @param <T>
 * @author leo
 */
public class ParseListProtocol<T> extends BaseProtocol<DataSourceModel<T>> {
    private DataSourceModel<T> dataSourceModel;
    private Class<T> cls;
    private Gson mGson;

    public ParseListProtocol(Context context, String baseUrl, Class<T> cls) {
        super(context, baseUrl);
        this.cls = cls;
        dataSourceModel = new DataSourceModel<T>();
        mGson = new Gson();
    }

    @Override
    protected DataSourceModel<T> parseJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);

            String result = jsonObject.getString("result");
            if (result.equals("null") || result.equals("") || result.equals("{}")) {
                dataSourceModel.list = new ArrayList<>();
            } else {
                dataSourceModel.list = fromJsonToList(
                        jsonObject.getString("result"), cls);
            }

            dataSourceModel.status = jsonObject.getInt("code");
            dataSourceModel.msg = jsonObject.getString("msg");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            dataSourceModel.list = new ArrayList<>();
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
