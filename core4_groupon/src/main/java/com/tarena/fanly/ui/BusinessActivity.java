package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tarena.fanly.R;
import com.tarena.fanly.adapter.BusinessAdapter;
import com.tarena.fanly.bean.BusinessBean;
import com.tarena.fanly.bean.DistrictBean;
import com.tarena.fanly.util.HttpUtil;
import com.tarena.fanly.util.SPUtil;
import com.tarena.fanly.view.MyBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessActivity extends Activity {

    String city;

    @BindView(R.id.lv_business_listview)
    ListView listView;
    List<BusinessBean.Business> datas;
    BusinessAdapter adapter;
    SPUtil sputil;
    @BindView(R.id.include_menu_city)
    LinearLayout linearLayout_menu;

    @BindView(R.id.iv_business_loading)
    ImageView ivLoading;

    @BindView(R.id.list_city_business)
    ListView leftListView;
    @BindView(R.id.list_ardess_business)
    ListView rightListView;

    @BindView(R.id.tv_business_textview1)
    TextView tvRegion;

    List<String> leftDatas;
    List<String> rightDatas;

    ArrayAdapter<String> leftAdapter;
    ArrayAdapter<String> rightAdapter;
    List<DistrictBean.CitiesBean.DistrictsBean> districtList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        city = getIntent().getStringExtra("city");
        Log.d("TAG", "onCreate: 城市--->" + city);
        ButterKnife.bind(this);
        sputil = new SPUtil(this);
        initListView();

    }

    @OnClick(R.id.tv_business_textview1)
    public void toggleMenu(View view) {
        if (linearLayout_menu.getVisibility() == View.INVISIBLE) {
            linearLayout_menu.setVisibility(View.VISIBLE);
        } else if (linearLayout_menu.getVisibility() == View.VISIBLE) {
            linearLayout_menu.setVisibility(View.INVISIBLE);
        }

    }

    private void initListView() {

        datas = new ArrayList<BusinessBean.Business>();
        adapter = new BusinessAdapter(this, datas);
        if (!sputil.isCloseBanner()) {
            final MyBanner myBanner = new MyBanner(this, null);
            myBanner.setOnCloseBannerListener(new MyBanner.OnCloseBannerListener() {
                @Override
                public void onClose() {
                    sputil.setCloseBanner(true);
                    listView.removeHeaderView(myBanner);
                }
            });
            listView.addHeaderView(myBanner);
        }

        listView.setAdapter(adapter);

        AnimationDrawable d = (AnimationDrawable) ivLoading.getDrawable();
        d.start();

        listView.setEmptyView(ivLoading);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BusinessBean.Business business;
                if (sputil.isCloseBanner()){
                    business=adapter.getItem(i);
                }else {
                    business=adapter.getItem(i-1);
                }
                Intent intent=new Intent(BusinessActivity.this,DetailActivity.class);
                intent.putExtra("business",business);
                startActivity(intent);
            }
        });

        leftDatas = new ArrayList<String>();
        leftAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, leftDatas);
        leftListView.setAdapter(leftAdapter);

        rightDatas = new ArrayList<String>();
        rightAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rightDatas);
        rightListView.setAdapter(rightAdapter);

        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DistrictBean.CitiesBean.DistrictsBean district = districtList.get(i);
                List<String> neighborhoods = new ArrayList<String>(district.getNeighborhoods());
                neighborhoods.add(0, "全部" + district.getDistrict_name());
                rightDatas.clear();
                rightDatas.addAll(neighborhoods);
                rightAdapter.notifyDataSetChanged();

            }
        });

        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {
                String region = rightAdapter.getItem(i);
                if (i == 0) {//regioin "全部xx区"
                    region = region.substring(2, region.length());
                }

                tvRegion.setText(region);
                linearLayout_menu.setVisibility(View.INVISIBLE);
                adapter.removeAll();

                HttpUtil.getFoodsByRetrofit(city, region, new Callback<BusinessBean>() {
                    @Override
                    public void onResponse(Call<BusinessBean> call, Response<BusinessBean> response) {

                        BusinessBean businessBean = response.body();
                        List<BusinessBean.Business> list = businessBean.getBusinesses();
                        adapter.addAll(list, true);
                    }

                    @Override
                    public void onFailure(Call<BusinessBean> call, Throwable throwable) {

                    }
                });
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {

        HttpUtil.getFoodsByRetrofit(city, null, new Callback<BusinessBean>() {
            @Override
            public void onResponse(Call<BusinessBean> call, Response<BusinessBean> response) {

                BusinessBean businessBean = response.body();
                List<BusinessBean.Business> list = businessBean.getBusinesses();
                adapter.addAll(list, true);
            }

            @Override
            public void onFailure(Call<BusinessBean> call, Throwable throwable) {

            }
        });

        HttpUtil.getDistrictsByRetrofit(city, new Callback<DistrictBean>() {
            @Override
            public void onResponse(Call<DistrictBean> call, Response<DistrictBean> response) {
                DistrictBean districtBean = response.body();
                districtList = districtBean.getCities().get(0).getDistricts();

                List<String> districtNames = new ArrayList<String>();
                for (int i = 0; i < districtList.size(); i++) {
                    DistrictBean.CitiesBean.DistrictsBean district = districtList.get(i);
                    districtNames.add(district.getDistrict_name());
                }

                leftDatas.clear();
                leftDatas.addAll(districtNames);
                leftAdapter.notifyDataSetChanged();


                List<String> neighborhoods = new ArrayList<String>(districtList.get(0).getNeighborhoods());
                String districtName = districtList.get(0).getDistrict_name();
                neighborhoods.add(0, "全部" + districtName);

                rightDatas.clear();
                rightDatas.addAll(neighborhoods);
                rightAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<DistrictBean> call, Throwable throwable) {

            }
        });

    }
}