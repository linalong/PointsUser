package com.heizi.pointsuser.utils;

import android.util.Log;

import com.heizi.pointsuser.UserModel;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;

/**
 * Created by leo on 16/10/26.
 */

public class DbUtils {

    private static DbManager manager;

    public static DbManager getInstance() {
        if (manager == null) {
            manager = init();
        }
        return manager;
    }

    private static DbManager init() {
        /**
         * 初始化DaoConfig配置
         */
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                //设置数据库名，默认xutils.db
                .setDbName("myapp.db")
                //设置数据库路径，默认存储在app的私有目录
//                .setDbDir(new File("/mnt/sdcard/"))
                //设置数据库的版本号
                .setDbVersion(2)
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                //设置数据库更新的监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                })
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                });
        //设置是否允许事务，默认true
        //.setAllowTransaction(true)

        DbManager db = x.getDb(daoConfig);

        return db;
    }


//    /**
//     * 获取默认模版
//     */
//    public static ModelMoban getDefault() {
//        ModelMoban modelMoban = null;
//        try {
//            manager = getInstance();
//            modelMoban = manager.selector(ModelMoban.class).where("is_default","=", "1").findFirst();
//            if (modelMoban == null) {
//                modelMoban = new ModelMoban();
//                modelMoban.setAd_img("http://cdn.ledianduo.cn/upload/2016/05/d5d4eec1-4794-405b-bce4-1ec17d3db806.jpg");
//                modelMoban.setAd_type(0);
//                modelMoban.setPosition(0);
//                modelMoban.setIs_default(1);
//                DbUtils.getInstance().save(modelMoban);
//
//                ModelMoban modelMoban2 = new ModelMoban();
//                modelMoban2.setAd_img("http://cdn.ledianduo.cn/upload/2016/05/d5d4eec1-4794-405b-bce4-1ec17d3db806.jpg");
//                modelMoban2.setAd_type(0);
//                modelMoban2.setPosition(1);
//                DbUtils.getInstance().save(modelMoban2);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        return modelMoban;
//    }
//
//
//    /**
//     * 删除所有顶部或底部广告
//     *
//     * @param typePosition
//     */
//    public static void deleteByTypePosition(int typePosition) {
//
//        manager = getInstance();
//        WhereBuilder whereBuilder = WhereBuilder.b();
//        whereBuilder.and("position", "=", typePosition);
//        try {
//            manager.delete(ModelMoban.class, whereBuilder);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 查询所有顶部或底部广告
//     *
//     * @param position
//     * @return
//     */
//    public static List<ModelMoban> qureyListByPosition(int position) {
//        List<ModelMoban> list = new ArrayList<>();
//        try {
//            list = DbUtils.getInstance().selector(ModelMoban.class).where("position", "=", position).findAll();
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//        return list;
//    }


    public static void deleteUser() {
        try {
            getInstance().delete(UserModel.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static void sava(UserModel userModel) {
        try {
            getInstance().delete(UserModel.class);
            getInstance().save(userModel);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    public static UserModel get() {
        UserModel userModel = null;
        try {
            userModel = getInstance().selector(UserModel.class).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return userModel;
    }



}
