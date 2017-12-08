package com.heizi.mylibrary.xutils;

import android.content.Context;


import com.heizi.mylibrary.model.DataSourceModel;

import org.json.JSONObject;


/**
 * 解析 data中直接就是String的情况
 *
 * @author leo
 */
public class ParseSimpleStringProtocol extends SimpleProtocol<DataSourceModel<String>> {
    private DataSourceModel<String> dataSourceModel;

    public ParseSimpleStringProtocol(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        dataSourceModel = new DataSourceModel<String>();
    }

    @Override
    protected DataSourceModel<String> parseJson(String jsonString) {
        JSONObject jsonObject;
        try {
            dataSourceModel.json = jsonString;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataSourceModel;
    }
}
