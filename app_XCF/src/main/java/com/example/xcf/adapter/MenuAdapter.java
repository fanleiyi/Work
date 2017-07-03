package com.example.xcf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.day06_03_zhihu.R;
import com.example.xcf.entity.Menu;
import com.example.xcf.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/7/1.
 */

public class MenuAdapter extends MyBaseAdapter<Menu.ResultBean.DataBean> {

    List<Menu> datas = new ArrayList<>();
    Context context;

    public MenuAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view==null){
            view=layoutInflater.inflate(R.layout.list_item_menu,viewGroup,false);
            vh=new ViewHolder(view);
            view.setTag(vh);
        }else {
            vh= (ViewHolder) view.getTag();
        }
        Menu.ResultBean.DataBean item = getItem(i);
        String photo = item.getAlbums().get(0);
        HttpUtil.displayImage(photo,vh.ivPhoto);
        vh.tvTitle.setText(item.getTitle());
        vh.tvTag.setText(item.getIngredients());
        Random rand = new Random();
        int idx = rand.nextInt(4)+5;
        int idx2 = rand.nextInt(9);
        vh.tvinfo.setText(idx+"."+idx2+"分");
        int idx3=rand.nextInt(3000)+500;

        vh.tvdis.setText(idx3+"人做过");
        return view;
    }

    public class ViewHolder{

        @BindView(R.id.iv_menu_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_menu_item_name)
        TextView tvTitle;
        @BindView(R.id.tv_menu_item_tag)
        TextView tvTag;
        @BindView(R.id.tv_menu_item_info)
        TextView tvinfo;
        @BindView(R.id.tv_menu_item_distance)
        TextView tvdis;


        public ViewHolder(View view){

            ButterKnife.bind(this,view);
        }
    }

}
