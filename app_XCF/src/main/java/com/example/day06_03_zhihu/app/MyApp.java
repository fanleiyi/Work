package com.example.day06_03_zhihu.app;

import android.app.Application;

import com.example.day06_03_zhihu.entity.Classify;

import java.util.List;

/**
 * Created by tarena on 2017/6/19.
 */

public class MyApp extends Application {

    public static MyApp CONTEXT;
    // 城市名称缓存

    public static List<Classify.ResultBean> resultBeenList;
    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=this;
    }
}
