package com.tarena.fanly.app;

import android.app.Application;

import com.tarena.fanly.bean.CitynameBean;

import java.util.List;

/**
 * Created by tarena on 2017/6/19.
 */

public class MyApp extends Application {

    public static MyApp CONTEXT;
    // 城市名称缓存
    public static List<CitynameBean> cityNameBeanList;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=this;
    }
}
