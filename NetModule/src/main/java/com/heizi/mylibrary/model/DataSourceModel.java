package com.heizi.mylibrary.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataSourceModel<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4475492232292606868L;
    public int status = 0; // 状态
    public String msg = ""; // 提示
    public String code = "";//ok  或者其他 失败信息
    public String pageNow;
    public String pageCount;
    public List<T> list = new ArrayList<>();// 常用数据集1
    public List<T> list1; // 常用数据集2
    public List<T> list2; // 常用数据集3
    public List<T> list3; // 常用数据集4
    public List<?> list4; // 常用数据集4
    public T temp;// 临时对象
    public T temp1;// 临时对象
    public String json;//未处理的json对象
    public String name;
    public int id;
}