package com.example.day06_03_zhihu.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.day06_03_zhihu.app.MyApp;
import com.example.day06_03_zhihu.entity.Classify;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public void getCliassify(final Response.Listener<List<Classify.ResultBean>> listener){
        final List<Classify.ResultBean> classifys=new ArrayList<>();
        final String url=HttpUtil.getURL("http://apis.juhe.cn/cook/category","parentid=&dtype=&key=bc90c61cecbb5c2ca08fca947c7a16eb");
        StringRequest request= new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // 利用Jsonlib（JSONObject）提取团购id
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("result");

                    for (int i=0;i<jsonArray.length();i++){
                        Classify.ResultBean bean;
                        bean= (Classify.ResultBean) jsonArray.get(i);
                        classifys.add(bean);
                    }
                    listener.onResponse(classifys);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },null);
        queue.add(request);

    }



}
