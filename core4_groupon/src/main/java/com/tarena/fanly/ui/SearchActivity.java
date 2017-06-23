package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.tarena.fanly.R;
import com.tarena.fanly.app.MyApp;
import com.tarena.fanly.bean.CitynameBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;

public class SearchActivity extends Activity {
    @BindView(R.id.lv_search_listview)
    ListView listView;
    List<String> cities;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initListView();
    }

    @OnTextChanged(R.id.et_search)
    public void search(Editable editable){
        if (editable.length()==0){
            cities.clear();
            adapter.notifyDataSetChanged();
        }else {
            searchCities(editable.toString().toUpperCase());
        }
    }

    @OnItemClick(R.id.lv_search_listview)
    public void selectCity(AdapterView<?> adapterView, View view,int i,long y){
        Intent intent=new Intent();
        String city=adapter.getItem(i);
        intent.putExtra("city",city);
        setResult(RESULT_OK,intent);
        finish();

    }

    /**
     * 根据输入的内容筛选符合的城市名称
     * @param s
     */
    private void searchCities(String s) {
        List<String> temps=new ArrayList<>();

        if (s.matches("[\\u4e00-\\u9fff]+")){
            // 中文
            for (CitynameBean bean: MyApp.cityNameBeanList){
                if (bean.getCityName().contains(s)){
                    temps.add(bean.getCityName());
                }
            }

        }else {
            // 拼音
            for (CitynameBean bean: MyApp.cityNameBeanList) {
                if (bean.getPyName().contains(s)) {
                    temps.add(bean.getCityName());
                }
            }
        }
        if (temps.size()>0){
            cities.clear();
            cities.addAll(temps);
            adapter.notifyDataSetChanged();
        }

    }

    private void initListView() {
        cities = new ArrayList<>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cities);
        listView.setAdapter(adapter);
    }
}
