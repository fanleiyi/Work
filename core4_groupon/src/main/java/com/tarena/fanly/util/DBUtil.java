package com.tarena.fanly.util;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tarena.fanly.bean.CitynameBean;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by tarena on 2017/6/22.
 */

public class DBUtil {

    Dao<CitynameBean,String> dao;
    DBHelper dbHelper;

    public DBUtil(Context context){
        dbHelper = DBHelper.getInstance(context);
        try {
            dao = dbHelper.getDao(CitynameBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insert(CitynameBean cityNameBean){
        try {
            dao.createIfNotExists(cityNameBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBatch(final List<CitynameBean> list){
        // 建立连接后一次性将数据全部写入后再断开连接
        try {
            dao.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (CitynameBean bean:list){
                        insert(bean);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAll(List<CitynameBean> cityNameBeanList){

        for (CitynameBean name:cityNameBeanList){
            insert(name);
        }

    }

    public List<CitynameBean> query(){
        List<CitynameBean> cityNameBeanList = null;

        try {
            cityNameBeanList = dao.queryForAll();
            return cityNameBeanList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        }



    }

}
