package com.example.xcf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.example.day06_03_zhihu.R;
import com.example.xcf.adapter.ClassifyAdapter;
import com.example.xcf.app.MyApp;
import com.example.xcf.entity.Classify;
import com.example.xcf.util.DBUtil;
import com.example.xcf.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class ClassifyActivity extends Activity {

    @BindView(R.id.gridView_Classify)
    GridView gridView;
    DBUtil dbUtil;
    ClassifyAdapter adapter;

    List<Classify.ResultBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
        dbUtil=new DBUtil(this);
        UI();
    }

    private void UI() {
        datas = new ArrayList<>();
        adapter=new ClassifyAdapter(this);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        // 从内存缓存中读取数据
        if (MyApp.resultBeenList != null && MyApp.resultBeenList.size() > 0) {
            adapter.addDatas(MyApp.resultBeenList, true);
            Log.d("TAG", "菜谱数据从内存缓存中加载 ");
            return;
        }

        //从数据库中读取菜谱数据
        List<Classify.ResultBean> list = dbUtil.query();
        if (list != null && list.size() > 0) {
            adapter.addDatas(list, true);
            MyApp.resultBeenList = list;
            Log.d("TAG", "菜谱数据从数据库中加载 ");
            return;
        }
        HttpUtil.getClassify(new Callback<Classify>() {
            @Override
            public void onResponse(Call<Classify> call, retrofit2.Response<Classify> response) {
                final Classify classify = response.body();
                datas.addAll(classify.getResult());
                adapter.addDatas(datas, true);
                //向数据库中写入菜单数据
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        long start = System.currentTimeMillis();
                        MyApp.resultBeenList=classify.getResult();
                        dbUtil.insertBatch(classify.getResult());
                        for (Classify.ResultBean bean:MyApp.resultBeenList){
                            dbUtil.insertBatch2(bean.getList());
                        }
                        Log.d("TAG", "写入数据库完毕，耗时：" + (System.currentTimeMillis() - start));

                    }
                }.start();
            }
            @Override
            public void onFailure(Call<Classify> call, Throwable throwable) {
            }
        });

    }
    public void onBack(View v){
        finish();
    }
}
