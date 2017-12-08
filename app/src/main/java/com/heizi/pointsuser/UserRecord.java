package com.heizi.pointsuser;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 登录过的用户
 *
 * @author Lin
 */
public class UserRecord implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6700564129364959895L;
    private static final String K = "ksxsooeAK23CI432597eruossx";
    private static final String fileName = "tuiguang";
    private UserModel user = new UserModel();

    /**
     * 保存文本
     */
    public synchronized void save(Context context) {

        try {
            // 写数据
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE); // 获取输出流
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);// 写入
            fos.close(); // 关闭输出流
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中加载登陆过的用户信息
     */
    public static UserRecord load(Context context) {
        UserRecord config = null;
        try {
            FileInputStream fis = context.openFileInput(fileName); // 获得输入流
            ObjectInputStream ois = new ObjectInputStream(fis);
            config = (UserRecord) ois.readObject();
            ois.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            config = new UserRecord();
        }
        // config=new com.heizi.pointsuser.UserRecord();
        // config.pwd="1111111";
        // config.userName="test";
        // com.heizi.pointsuser.UserModel model=new com.heizi.pointsuser.UserModel();
        // model.setUser_token("f5a019b9b3654b99de454115798a2cc7");
        // model.setAddress("地址");
        // config.user=model;
        return config;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {

        out.writeObject(user);
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {

        user = (UserModel) in.readObject();

    }

    public void clearAll() {
        setUser(new UserModel());
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static String getK() {
        return K;
    }

    public static String getFilename() {
        return fileName;
    }

}
