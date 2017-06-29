package com.example.day06_03_zhihu.util;

import android.content.Context;

import com.example.day06_03_zhihu.entity.Classify;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by tarena on 2017/6/22.
 */

public class DBUtil {

    Dao<Classify.ResultBean,String> dao;
    Dao<Classify.ResultBean.ListBean,String> dao2;

    DBHelper dbHelper;

    public DBUtil(Context context){
        dbHelper = DBHelper.getInstance(context);
        try {
            dao = dbHelper.getDao(Classify.ResultBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insert(Classify.ResultBean resultBean){
        try {
            dao.createIfNotExists(resultBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void insert(Classify.ResultBean.ListBean bean){
        try {
            dao2.createIfNotExists(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBatch(final List<Classify.ResultBean> list){
        // 建立连接后一次性将数据全部写入后再断开连接
        try {
            dao.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (Classify.ResultBean bean:list){
                        insert(bean);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertBatch2(final List<Classify.ResultBean.ListBean> list){
        // 建立连接后一次性将数据全部写入后再断开连接
        try {
            dao2.callBatchTasks(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (Classify.ResultBean.ListBean bean:list){
                        insert(bean);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAll(List<Classify.ResultBean> resultBeen){

        for (Classify.ResultBean name:resultBeen){
            insert(name);
        }
    }
  public void insertAll2(List<Classify.ResultBean.ListBean> been){

        for (Classify.ResultBean.ListBean name:been){
            insert(name);
        }
    }

    public List<Classify.ResultBean> query(){
        List<Classify.ResultBean> bean;

        try {
            bean = dao.queryForAll();
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        }
    }
  public List<Classify.ResultBean.ListBean> query2(){
        List<Classify.ResultBean.ListBean> bean;

        try {
            bean = dao2.queryForAll();
            return bean;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询异常");
        }
    }

}
