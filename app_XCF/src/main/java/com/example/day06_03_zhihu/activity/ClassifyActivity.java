package com.example.day06_03_zhihu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.android.volley.Response;
import com.example.day06_03_zhihu.R;
import com.example.day06_03_zhihu.adapter.ClassifyAdapter;
import com.example.day06_03_zhihu.app.MyApp;
import com.example.day06_03_zhihu.entity.Classify;
import com.example.day06_03_zhihu.util.DBUtil;
import com.example.day06_03_zhihu.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassifyActivity extends Activity {

    @BindView(R.id.gridView_Classify)
    GridView gridView;
    DBUtil dbUtil;
    ClassifyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
       dbUtil=new DBUtil(this);
        UI();
    }

    private void UI() {

        adapter=new ClassifyAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        // 从内存缓存中读取数据
        if (MyApp.resultBeenList!=null&&MyApp.resultBeenList.size()>0){
            // adapter.addDatas(MyApp.resultBeenList,true);
            Log.d("TAG", "菜谱数据从内存缓存中加载 ");
            return;
        }

        //从数据库中读取城市数据
        List<Classify.ResultBean> list = dbUtil.query();
        if(list!=null && list.size()>0){
           //  adapter.addDatas(list,true);

            MyApp.resultBeenList = list;
            Log.d("TAG", "菜谱数据从数据库中加载 ");
            return;

        }


        HttpUtil.getClassify(new Response.Listener<List<Classify.ResultBean>>() {
            @Override
            public void onResponse(List<Classify.ResultBean> strings) {
                Log.i("TAG",strings.toString());
                // 根据List<String> 创建一个List<CitynameBean>
                // 将List<CitynameBean>放在布局中显示
                final List<Classify.ResultBean> resultBeanList=new ArrayList<>();
                    for (Classify.ResultBean bean:strings){
                        Classify.ResultBean bean1=new Classify.ResultBean();
                        bean1.setParentId(bean.getParentId());
                        bean1.setName(bean.getName());
                        bean1.setList(bean.getList());
                        resultBeanList.add(bean1);
                    }


             //    adapter.addAll(ctNames,true);
                // 缓存数据
                MyApp.resultBeenList=resultBeanList;
                //向数据库中写入菜谱数据
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        long start = System.currentTimeMillis();
                        dbUtil.insertBatch(resultBeanList);
                        Log.d("TAG", "写入数据库完毕，耗时："+(System.currentTimeMillis()-start));

                    }
                }.start();
            }
        });
    }

    public void onBack(View v){
        finish();
    }
}
