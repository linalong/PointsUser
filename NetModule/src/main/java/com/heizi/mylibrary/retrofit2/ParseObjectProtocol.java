package com.heizi.mylibrary.retrofit2;

import android.content.Context;

import com.google.gson.Gson;
import com.heizi.mylibrary.model.DataSourceModel;

import org.json.JSONObject;


/**
 * 解析 jsonobject 或者 与jsonarray 混合的数据
 *
 * @param <T>
 * @author leo
 */
public class ParseObjectProtocol<T> extends BaseProtocol<DataSourceModel<T>> {
    private Class<T> mClass;
    private DataSourceModel<T> dataSourceModel;

    public ParseObjectProtocol(Context context,String baseUrl, Class<T> mClass) {
        super(context,baseUrl);
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
            dataSourceModel.temp = gson.fromJson(jsonObject.getString("result"),
                    mClass);
            dataSourceModel.status = jsonObject.getInt("code");
//             dataSourceModel.info = jsonObject.getString("msg");
        } catch (org.json.JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataSourceModel;
    }
}
