package com.example.xcf.util;

import android.util.Log;

import com.example.xcf.constant.IURL;
import com.example.xcf.entity.Classify;
import com.example.xcf.entity.Menu;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by pjy on 2017/6/19.
 */

public class RetrofitClient {

    private static RetrofitClient INSTANCE;

    public static RetrofitClient getInstance(){
        if(INSTANCE==null){

            synchronized (RetrofitClient.class){
                if(INSTANCE==null){
                    INSTANCE = new RetrofitClient();
                }
            }

        }

        return INSTANCE;
    }

    private Retrofit retrofit;

    private OkHttpClient okhttpClient;

    private NetService netService;

    private RetrofitClient(){
        okhttpClient = new OkHttpClient.Builder().addInterceptor(new MyOkHttpInterceptor()).build();
        retrofit = new Retrofit.Builder().client(okhttpClient).baseUrl(IURL.BASEURL).
                addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
        netService = retrofit.create(NetService.class);
    }



    /**
     * OKHTTP的拦截器
     */
    public class MyOkHttpInterceptor implements Interceptor{

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //获得请求对象
            Request request =  chain.request();
            //请求路径
            //比如：http://baseurl/deal/get_daily_new_id_list?city=xxx&date=xxx
            HttpUrl url = request.url();
            //取出原有请求路径中的参数
            HashMap<String ,String> params = new HashMap<String,String>();
            //原有请求路径中,请求参数的名称
            //例如{city,date}
            Set<String> set = url.queryParameterNames();

            for(String key:set){
                params.put(key,url.queryParameter(key));
            }

            //字符串形式的http://baseurl/deal/get_daily_new_id_list?city=xxx&date=xxx
            String urlString = url.toString();
            Log.d("TAG", "原始请求路径------> "+urlString);

            StringBuilder sb = new StringBuilder(urlString);
            if(set.size()==0){
                //意味着原有请求路径中没有参数
                sb.append("?");
            }else{
                sb.append("&");
            }
            sb.append("key=").append(HttpUtil.APPKEY);
            Log.d("TAG", "新的请求路径------>: "+sb.toString());
            Request newRequest = new Request.Builder().url(sb.toString()).build();
            return chain.proceed(newRequest);
        }
    }


    public void getClassifys(Callback<Classify> callback){
        Map<String,String> params = new HashMap<>();
        Call<Classify> call = netService.getClassifys(params);
        call.enqueue(callback);
    }
    public void getMenu(String menu, Callback<Menu> callback){
        Map<String,String> params = new HashMap<>();
        params.put("menu",menu);
        Call<Menu> call = netService.getMenu(params);
        call.enqueue(callback);
    }




}
