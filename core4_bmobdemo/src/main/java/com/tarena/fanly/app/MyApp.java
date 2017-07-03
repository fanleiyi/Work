package com.tarena.fanly.app;

import android.app.Application;
import android.media.MediaPlayer;


import com.tarena.fanly.R;
import com.tarena.fanly.bean.MyUser;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

/**
 * Created by tarena on 2017/6/29.
 */

public class MyApp extends Application{

    public static MyApp CONTEXT;
    public static MyUser loginUser;
    public static MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=this;
        // dd8ad988bde34b92b3e3244922f8700b // 自己的

        // 772f0d5bcb45dfd29579c82dd05510aa// 老师的
        // 初始化BmobSDK
        Bmob.initialize(this, "772f0d5bcb45dfd29579c82dd05510aa");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this);

        player=MediaPlayer.create(this, R.raw.notify);
    }

}
