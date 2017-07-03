package com.example.xcf.util;

import android.widget.ImageView;

import com.example.day06_03_zhihu.R;
import com.example.xcf.app.MyApp;
import com.example.xcf.entity.Classify;
import com.example.xcf.entity.Menu;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import retrofit2.Callback;

/**
 * Created by tarena on 2017/6/22.
 */

public class HttpUtil {
    public static final String APPKEY = "bc90c61cecbb5c2ca08fca947c7a16eb";

    /**
     * 获得满足大众点评服务器要求的请求路径
     */

    public static String getURL(String url, Map<String, String> params) {
        String result = "";
        String query = getQuery(APPKEY, params);
        result = url + "?" + query;
        return result;
    }

    /**
     * 获取请求地址中的参数部分
     *
     * @param appkey
     * @param params
     * @return
     */
    public static String getQuery(String appkey, Map<String, String> params) {
        try {
            // 添加签名
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("key=").append(appkey);
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(),"utf8"));
            }
            String queryString = stringBuilder.toString();

            return queryString;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //扔异常
            throw new RuntimeException("使用了不正确的字符集名称");
        }
    }

    //

    public static void getClassify(Callback<Classify> callback){
        RetrofitClient.getInstance().getClassifys(callback);
    }
    public static void getMenu(String menu,Callback<Menu> callback){
        RetrofitClient.getInstance().getMenu(menu,callback);
    }
    public static void displayImage(String url,ImageView iv){
        Picasso.with(MyApp.CONTEXT).load(url).placeholder(R.drawable.bucket_no_picture).error(R.drawable.bucket_no_picture).into(iv);
    }
}
