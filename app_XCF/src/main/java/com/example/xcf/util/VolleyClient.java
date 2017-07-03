package com.example.xcf.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.xcf.app.MyApp;

/**
 * Created by tarena on 2017/6/22.
 */

public class VolleyClient {
    //1)声明一个私有的静态的属性
    private static VolleyClient INSTANCE;

    //2)声明一个公有的静态的获取1)属性的方法
    public static VolleyClient getInstance() {
        if (INSTANCE == null) {
            synchronized (VolleyClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VolleyClient();
                }
            }
        }
        return INSTANCE;
    }

    RequestQueue queue;
    ImageLoader imageLoader;
    //3)构造器私有化
    private VolleyClient() {
        queue = Volley.newRequestQueue(MyApp.CONTEXT);
        imageLoader=new ImageLoader(queue, new ImageLoader.ImageCache() {

            LruCache<String,Bitmap> cache= new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getHeight()*value.getRowBytes();
                }
            };
            @Override
            public Bitmap getBitmap(String s) {
                return cache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                cache.put(s,bitmap);
            }
        });
    }

}
