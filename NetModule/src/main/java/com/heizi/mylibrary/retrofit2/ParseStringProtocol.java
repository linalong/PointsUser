package com.heizi.mylibrary.retrofit2;

import android.content.Context;


import com.heizi.mylibrary.model.DataSourceModel;

import org.json.JSONObject;


/**
 * 解析 data中直接就是String的情况
 *
 * @author leo
 */
public class ParseStringProtocol extends BaseProtocol<DataSourceModel<String>> {
    private DataSourceModel<String> dataSourceModel;

    public ParseStringProtocol(Context context,String baseUrl) {
        super(context,baseUrl);
        // TODO Auto-generated constructor stub
        dataSourceModel = new DataSourceModel<String>();
    }

    @Override
    protected DataSourceModel<String> parseJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            dataSourceModel.status = jsonObject.getInt("code");
            dataSourceModel.msg = jsonObject.getString("msg");
            dataSourceModel.json = jsonObject.getString("result");
        } catch (org.json.JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataSourceModel;
    }
}
