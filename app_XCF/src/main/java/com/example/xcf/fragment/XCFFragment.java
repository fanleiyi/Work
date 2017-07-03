package com.example.xcf.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.day06_03_zhihu.R;
import com.example.xcf.activity.ClassifyActivity;
import com.example.xcf.activity.MainActivity;
import com.example.xcf.activity.MenuActivity;
import com.example.xcf.adapter.XCFAdapter;
import com.example.xcf.entity.XCF;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class XCFFragment extends BaseFragment {


    private XCFAdapter adapter;
    private List<XCF> xcfs;

    RadioGroup rg_top;
    RadioGroup rg_menu;
    private RadioButton rbtn1;
    private RadioButton rbtn2;
    private RadioButton rbtn3;

    public XCFFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contentView  = inflater.inflate(R.layout.fragment_xcf, container, false);
        ButterKnife.bind(MainActivity.class,contentView);
        initialUI();
       return contentView;

    }

    @Override
    public void initialUI() {
        actionbar = (LinearLayout) contentView.
                findViewById(R.id.actionbar_XCF);
        //初始化标题栏
        initialActionbar(R.drawable.createrecipeicon, "搜索菜谱、食材", R.drawable.buy_list_button);

        xcfs = new ArrayList<>();

        int[] pngs=new int[]{R.drawable.title01,
                R.drawable.title02,
                R.drawable.title03,
                R.drawable.title04,
                R.drawable.title05,
                R.drawable.title06,
                R.drawable.title07,
                R.drawable.title08,
                R.drawable.title09,
                R.drawable.title10,};
        for (int i =0;i<pngs.length;i++){
            XCF xcf = new XCF();
            xcf.setPrint(pngs[i]);
            xcf.setTitle("Title-"+(i+1));
            xcfs.add(xcf);
        }
        PullToRefreshListView ptrListView = (PullToRefreshListView) contentView.findViewById(R.id.ptrl_xcf);
        ListView listView =ptrListView.getRefreshableView();
        View headview=View.inflate(getActivity(), R.layout.image_btn_xcf_01, null);
        listView.addHeaderView(headview);
        adapter = new XCFAdapter(getActivity());
        adapter.addDatas(xcfs,true);
        listView.setAdapter(adapter);
        rg_top = (RadioGroup) contentView.findViewById(R.id.radio_xcf_top);
        rg_menu = (RadioGroup) contentView.findViewById(R.id.radio_xcf_menu);
        rbtn1 = (RadioButton) contentView.findViewById(R.id.rbtn_xcf_1);
        rbtn2 = (RadioButton) contentView.findViewById(R.id.rbtn_xcf_2);
        rbtn3 = (RadioButton) contentView.findViewById(R.id.rbtn_xcf_3);
        rg_top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioBtn4:
                        startActivity(new Intent(getActivity(),ClassifyActivity.class));
                    break;
                }
            }
        });

        rg_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rbtn_xcf_1:
                        String menu = rbtn1.getText().toString();
                        Intent intent=new Intent(getActivity(), MenuActivity.class);
                        intent.putExtra("menu",menu);
                        startActivity(intent);
                        break;
                    case R.id.rbtn_xcf_2:
                        String menu2 = rbtn2.getText().toString();
                        Intent intent2=new Intent(getActivity(), MenuActivity.class);
                        intent2.putExtra("menu",menu2);
                        startActivity(intent2);
                        break;
                    case R.id.rbtn_xcf_3:
                        String menu3 = rbtn3.getText().toString();
                        Intent intent3=new Intent(getActivity(), MenuActivity.class);
                        intent3.putExtra("menu",menu3);
                        startActivity(intent3);
                        break;
                }

            }
        });

    }


}
