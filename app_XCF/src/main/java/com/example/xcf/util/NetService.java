package com.example.xcf.util;

import com.example.xcf.entity.Classify;
import com.example.xcf.entity.Menu;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by tarena on 2017/6/19.
 */

public interface NetService {

    @GET("category")
    public Call<Classify> getClassifys(@QueryMap Map<String,String> params);

    @GET("query.php")
    public Call<Menu> getMenu(@QueryMap Map<String,String> params);


}
