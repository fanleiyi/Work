package com.example.xcf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.day06_03_zhihu.R;
import com.example.xcf.adapter.MenuAdapter;
import com.example.xcf.entity.Menu;
import com.example.xcf.util.HttpUtil;
import com.hp.hpl.sparta.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends Activity {

    private String menu;
    @BindView(R.id.tv_menu_title)
    TextView tv_title;
    @BindView(R.id.list_menu)
    ListView listView;

    List<Menu.ResultBean.DataBean> datas;
    MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        menu = getIntent().getStringExtra("menu");
        tv_title.setText(menu);
        initListView();
    }

    private void initListView() {
        datas=new ArrayList<>();
        adapter=new MenuAdapter(this);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        String menu = this.menu.replaceAll(" ", "");
        HttpUtil.getMenu(menu, new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                Menu menu = response.body();
                Log.i("TAG",menu.toString());
               adapter.addDatas(menu.getResult().getData(),true);
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable throwable) {

            }
        });

    }

    @OnClick(R.id.img_menu_esc)
    public void back(View v){
        finish();
    }
}
