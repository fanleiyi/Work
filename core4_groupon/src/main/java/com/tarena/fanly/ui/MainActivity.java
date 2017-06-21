package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tarena.fanly.R;
import com.tarena.fanly.adapter.DealAdapter;
import com.tarena.fanly.bean.TuanBean;
import com.tarena.fanly.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends Activity {
    // 头部
    @BindView(R.id.actionbar_text_DM)
    TextView textView_title_DM;
    @BindView(R.id.actionbar_line)
    LinearLayout linearLayout_action;
    @BindView(R.id.actionbar_line_DM)
    LinearLayout linearLayout_action_DM;
    @BindView(R.id.actionbar_image_add)
    ImageView imageView_add;
    @BindView(R.id.include_main_menu)
    LinearLayout linearLayout_menu;

    // 中段
    @BindView(R.id.ptrlv_main)
    PullToRefreshListView pullToRefreshListView;

    ListView listView;
    List<TuanBean.Deal> datas=new ArrayList<>();
    DealAdapter adapter;


    // 脚部
    @BindView(R.id.radio_bottom)
    RadioGroup radioGroup_bottom;


    private View view_vp;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListView();
    }

    @OnClick(R.id.actionbar_line_DM)
    public void jumpToCity(View v){
        startActivity(new Intent(this,CityActivity.class));
    }

    @OnClick(R.id.actionbar_image_add)
    public void toggleMenu(View v){
        if (linearLayout_menu.getVisibility()==View.INVISIBLE){
            linearLayout_menu.setVisibility(View.VISIBLE);
        }else if (linearLayout_menu.getVisibility()==View.VISIBLE){
            linearLayout_menu.setVisibility(View.INVISIBLE);
        }
    }



    private void initListView() {
        listView=pullToRefreshListView.getRefreshableView();
        adapter=new DealAdapter(this,datas);
        LayoutInflater inflater=LayoutInflater.from(this);
        view_vp = inflater.inflate(R.layout.viewpager_layout_main,listView,false);
        View view_square= inflater.inflate(R.layout.header_list_square,listView,false);
        View view_ads= inflater.inflate(R.layout.header_list_ads,listView,false);
        View view_categories= inflater.inflate(R.layout.header_list_categories,listView,false);
        View view_recommend= inflater.inflate(R.layout.header_list_recommend,listView,false);

        listView.addHeaderView(view_vp);
        listView.addHeaderView(view_square);
        listView.addHeaderView(view_ads);
        listView.addHeaderView(view_categories);
        listView.addHeaderView(view_recommend);

        listView.setAdapter(adapter);
        initViewPager(view_vp);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem==0){
                    linearLayout_action_DM.setVisibility(View.VISIBLE);
                    imageView_add.setVisibility(View.VISIBLE);
                }else {
                    linearLayout_action_DM.setVisibility(View.GONE);
                    imageView_add.setVisibility(View.GONE);
                }
            }
        });

        // 添加下拉松手后的刷新
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    refresh();
            }
        });
    }

    private void initViewPager(View view_vp) {
        viewPager = (ViewPager) view_vp.findViewById(R.id.viewpager_main);
        final RadioGroup radioGroup_vp= (RadioGroup) view_vp.findViewById(R.id.radio__vp);
        final RadioButton radioButton1 = (RadioButton) view_vp.findViewById(R.id.radip_vp_btn1);
        final RadioButton radioButton2 = (RadioButton) view_vp.findViewById(R.id.radip_vp_btn2);
        final RadioButton radioButton3 = (RadioButton) view_vp.findViewById(R.id.radip_vp_btn3);
        pagerAdapter = new PagerAdapter() {

            int[] resIDs = new int[]{
                    R.layout.vp_main_item_01,
                    R.layout.vp_main_item_02,
                    R.layout.vp_main_item_03
            };

            @Override
            public int getCount() {
                return 30000;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                int layoutId = resIDs[position%3];
                View view = LayoutInflater.from(MainActivity.this).inflate(layoutId, viewPager,false);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(15000);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position%3){
                    case 0:
                        radioGroup_vp.check(radioButton1.getId());
                        break;
                    case 1:
                        radioGroup_vp.check(radioButton2.getId());
                        break;
                    case 2:
                        radioGroup_vp.check(radioButton3.getId());
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

   @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {


        //1)发起一个请求，服务器响应
        //以GET的方式发起请求
        //请求格式：http://xxx.xxxx.com/xxx？key=14xxxxxxx&city=%e8%f8%c6%xx%xx%xx
        //利用HttpClient(apache)
        //HttpURLConnection

        //Volley
/*
        HttpUtil.getDailyDealsByVolley(textView_title_DM.getText().toString(), new Response.Listener<TuanBean>() {
            @Override
            public void onResponse(TuanBean tuanBean) {
                if (tuanBean!=null) {
                    List<TuanBean.Deal> deals = tuanBean.getDeals();
                    //将deals放到ListView中呈现
                    adapter.addAll(deals, true);
                }else {
                    // 今日无新增团购内容
                    Toast.makeText(MainActivity.this,"今日无新增团购！",Toast.LENGTH_SHORT).show();
                }
                pullToRefreshListView.onRefreshComplete();

            }
        });*/

        //Retrofit+OKHttp

        HttpUtil.getDailyDealsByRetrofit(textView_title_DM.getText().toString(), new Callback<TuanBean>() {
            @Override
            public void onResponse(Call<TuanBean> call, retrofit2.Response<TuanBean> response) {
                if(response!=null){
                    TuanBean tuanBean= response.body();
                    List<TuanBean.Deal> deals = tuanBean.getDeals();
                    adapter.addAll(deals,true);

                }else{
                    Toast.makeText(MainActivity.this, "今日无新增团购内容", Toast.LENGTH_SHORT).show();
                }
                pullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onFailure(Call<TuanBean> call, Throwable throwable) {
                Log.d("TAG", "onFailure: "+throwable.getMessage());
                pullToRefreshListView.onRefreshComplete();
            }
        });

        //2)根据服务器响应的内容进行解析
        // JSON字符串 / XML文档
        // 解析JSON字符串：
        // JSONLib(JsonObject)
        // GSON
        // fastJson
        // jackson
        // 解析XML文档
        // XMLPull
        // SAX

        //3)将解析结果放到View中显示
        //放到ListView中显示需要适配器、条目布局

        //HttpUtil.testHttpURLConnection();
        //HttpUtil.testVolley();
        //HttpUtil.testRetrofit();


    }
}
