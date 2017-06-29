package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.tarena.fanly.R;
import com.tarena.fanly.adapter.CityAdapter;
import com.tarena.fanly.app.MyApp;
import com.tarena.fanly.bean.CitynameBean;
import com.tarena.fanly.util.DBUtil;
import com.tarena.fanly.util.HttpUtil;
import com.tarena.fanly.util.PinYinUtil;
import com.tarena.fanly.view.MyLetterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CityActivity extends Activity {
    @BindView(R.id.recyclerview_city)
    RecyclerView recyclerView;
    CityAdapter adapter;
    @BindView(R.id.myletterViewId)
    MyLetterView myLetterView;

    DBUtil dbUtil;

    List<CitynameBean> citys=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        dbUtil = new DBUtil(MyApp.CONTEXT);
        initRecyclerView();
        myLetterView.setOnTouchLetterListener(new MyLetterView.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!letter.equals("热门")) {
                    int position = adapter.getPositionForSection(letter.charAt(0));
                    if (adapter.getHeaderView()!=null){
                        position+=1;
                    }
                    manager.scrollToPositionWithOffset(position,0);

                }else {
                    recyclerView.scrollToPosition(0);
                }

            }
        });
    }

    private void initRecyclerView() {
        // 初始化数据源
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        adapter=new CityAdapter(CityActivity.this,citys);
        recyclerView.setAdapter(adapter);
        View headerView = LayoutInflater.from(this).inflate(R.layout.recycerview_top_gps,recyclerView,false);
        adapter.addHeaderView(headerView);

        adapter.setOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View itemView, int position) {
                CitynameBean cityNameBean =citys.get(position);
                String city = cityNameBean.getCityName();

                Intent data = new Intent();
                data.putExtra("city",city);
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        // 从内存缓存中读取数据
        if (MyApp.cityNameBeanList!=null&&MyApp.cityNameBeanList.size()>0){
            adapter.addAll(MyApp.cityNameBeanList,true);
            Log.d("TAG", "城市数据从内存缓存中加载 ");
            return;
        }

        //从数据库中读取城市数据
        List<CitynameBean> list = dbUtil.query();
        if(list!=null && list.size()>0){
            adapter.addAll(list,true);

            MyApp.cityNameBeanList = list;
            Log.d("TAG", "城市数据从数据库中加载 ");
            return;

        }


        HttpUtil.getCitys(new Response.Listener<List<String>>() {
            @Override
            public void onResponse(List<String> strings) {
                Log.i("TAG",strings.toString());
                // 根据List<String> 创建一个List<CitynameBean>
                // 将List<CitynameBean>放在布局中显示
                final List<CitynameBean> ctNames=new ArrayList<>();
                for (String name:strings){
                    if (!name.equals("全国")&&!name.equals("其他")&&!name.equals("点评实验室")){
                        CitynameBean ctnamebean=new CitynameBean();
                        ctnamebean.setCityName(name);
                        ctnamebean.setPyName(PinYinUtil.getPinYin(name));
                        ctnamebean.setLetter(ctnamebean.getPyName().charAt(0));
                        ctNames.add(ctnamebean);
                    }
                }

                Collections.sort(ctNames, new Comparator<CitynameBean>() {
                    @Override
                    public int compare(CitynameBean t1, CitynameBean t2) {
                        return t1.getPyName().compareTo(t2.getPyName());
                    }
                });
                adapter.addAll(ctNames,true);
                // 缓存数据
                MyApp.cityNameBeanList=ctNames;
                //向数据库中写入城市数据
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        long start = System.currentTimeMillis();
                        dbUtil.insertBatch(ctNames);
                        Log.d("TAG", "写入数据库完毕，耗时："+(System.currentTimeMillis()-start));

                    }
                }.start();
            }
        });
    }

    @OnClick(R.id.tv_city_edit)
    public void toSearch(View v){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent,101);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==101){
            setResult(RESULT_OK,data);
            finish();
        }
    }

}
