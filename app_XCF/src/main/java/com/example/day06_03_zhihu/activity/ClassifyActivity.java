package com.example.day06_03_zhihu.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.example.day06_03_zhihu.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassifyActivity extends AppCompatActivity {
    @BindView(R.id.gridLayout_Classify)
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        ButterKnife.bind(this);
        UI();
    }

    private void UI() {

    }

    public void onBack(View v){
        finish();
    }
}
