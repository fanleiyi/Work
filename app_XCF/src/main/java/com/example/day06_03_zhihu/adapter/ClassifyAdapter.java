package com.example.day06_03_zhihu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.day06_03_zhihu.R;
import com.example.day06_03_zhihu.entity.Classify;
import com.example.day06_03_zhihu.entity.Menu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/6/22.
 */
public class ClassifyAdapter extends MyBaseAdapter<Classify.ResultBean>{



    public ClassifyAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHoder vh;
        if (view==null){
            view=layoutInflater.inflate(R.layout.griditem_classify,viewGroup,false);
            vh=new ViewHoder(view);
            view.setTag(vh);
        }else {
            vh= (ViewHoder) view.getTag();
        }
        Classify.ResultBean bean=getItem(i);
        vh.textView_name.setText(bean.getName());

        return view;
    }

    public class ViewHoder{

        @BindView(R.id.text_classify)
        TextView textView_name;

        public ViewHoder(View view){
            ButterKnife.bind(this,view);
        }
    }
}