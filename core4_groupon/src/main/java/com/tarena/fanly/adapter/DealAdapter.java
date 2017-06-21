package com.tarena.fanly.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.fanly.R;
import com.tarena.fanly.bean.TuanBean;
import com.tarena.fanly.util.HttpUtil;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tarena on 2017/6/20.
 */

public class DealAdapter extends MyBaseAdapter<TuanBean.Deal> {

    public DealAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view==null){
            view=inflater.inflate(R.layout.list_item_main,viewGroup,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }

        TuanBean.Deal deal= getItem(i);
        //TODO 呈现图片
        HttpUtil.displayImage(deal.getS_image_url(),holder.ivPic);
        holder.tvTitle.setText(deal.getTitle());
        holder.tvDeail.setText(deal.getDescription());
        holder.tvPrice.setText(deal.getCurrent_price()+"");
        Random random = new Random();
        int count = random.nextInt(2000)+300;
        holder.tvCount.setText("已售"+count);
        //TODO 距离 vh.tvDistance.setText("xxxx");
        return view;
    }

    public class ViewHolder{
        @BindView(R.id.list_image_photo)
        ImageView ivPic;// 图片
        @BindView(R.id.list_text_name)
        TextView tvTitle; // 菜名
        @BindView(R.id.list_text_intro)
        TextView tvDeail; // 简介
        @BindView(R.id.list_text_distance)
        TextView tvDistance;// 距离
        @BindView(R.id.list_text_cost)
        TextView tvPrice;// 价格
        @BindView(R.id.list_text_sold)
        TextView tvCount;// 数量
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
