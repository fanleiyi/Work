package com.example.day06_03_zhihu.util;

import com.example.day06_03_zhihu.entity.Classify;

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


}
