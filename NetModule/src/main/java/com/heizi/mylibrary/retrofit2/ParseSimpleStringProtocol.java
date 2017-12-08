package com.heizi.mylibrary.retrofit2;

import android.content.Context;

import com.heizi.mylibrary.model.DataSourceModel;


/**
 * 解析 data中直接就是String的情况
 *
 * @author leo
 */
public class ParseSimpleStringProtocol extends SimpleProtocol<DataSourceModel<String>> {
    private DataSourceModel<String> dataSourceModel;

    public ParseSimpleStringProtocol(Context context, String baseUrl) {
        super(context, baseUrl);
        // TODO Auto-generated constructor stub
        dataSourceModel = new DataSourceModel<String>();
    }

    @Override
    protected DataSourceModel<String> parseJson(String jsonString) {
        dataSourceModel.json = jsonString;
        return dataSourceModel;
    }
}
