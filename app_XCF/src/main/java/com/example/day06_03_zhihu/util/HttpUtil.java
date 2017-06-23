package com.example.day06_03_zhihu.util;

import com.android.volley.Response;
import com.example.day06_03_zhihu.entity.Classify;

import java.util.List;

/**
 * Created by tarena on 2017/6/22.
 */

public class HttpUtil {
    /**
     * 获得满足大众点评服务器要求的请求路径
     */

    public static String getURL(String url, String canshu) {
        String result = url + "?" + canshu;

        return result;
    }

    //
    public static void getClassify(Response.Listener<List<Classify.ResultBean>> listener){
        VolleyClient.getInstance().getCliassify(listener);
    }

}
