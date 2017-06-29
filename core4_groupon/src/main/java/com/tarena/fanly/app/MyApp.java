package com.tarena.fanly.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.tarena.fanly.bean.CitynameBean;
import com.tarena.fanly.util.SPUtil;

import java.util.List;

/**
 * Created by tarena on 2017/6/19.
 */

public class MyApp extends Application {

    public static MyApp CONTEXT;
    // 城市名称缓存
    public static List<CitynameBean> cityNameBeanList;
    public static LatLng myLatLng;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        CONTEXT=this;
        SPUtil spUtil=new SPUtil(this);
        spUtil.setCloseBanner(false);
    }
}
