package com.tarena.fanly.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tarena.fanly.R;
import com.tarena.fanly.bean.MyPost;
import com.tarena.fanly.bean.MyUser;
import com.tarena.fanly.ui.PostActivity;
import com.tarena.fanly.ui.ShowActivity;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by tarena on 2017/6/30.
 */

public class PostAdapter extends BaseAdapter {

    Context context;
    List<MyPost> datas;
    LayoutInflater inflater;

    MyUser currentUser;//当前登录用户

    public PostAdapter(Context context, List<MyPost> datas,MyUser currentUser) {

        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
        this.currentUser = currentUser;

    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public MyPost getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = inflater.inflate(R.layout.item_post_layout, viewGroup, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        final MyPost post = getItem(i);

        MyUser user = post.getUser();//帖子作者

        String avatar = user.getAvatar();

        if (TextUtils.isEmpty(avatar)) {
            //用户没设置头像
            vh.ivAvatar.setImageResource(R.mipmap.ic_launcher);
        } else {

            Picasso.with(context).load(user.getAvatar()).into(vh.ivAvatar);
        }

        vh.tvName.setText(user.getUsername());

        vh.tvTitle.setText(post.getTitle());

        vh.tvTime.setText(post.getCreatedAt());

        vh.tvContent.setText(post.getContent());

        //帖子作者和当前登录用户是不是一个人？
        if(currentUser.getUsername().equals(user.getUsername())){
            vh.tvDelete.setVisibility(View.VISIBLE);
            vh.tvUpdate.setVisibility(View.VISIBLE);

        }else{
            vh.tvDelete.setVisibility(View.INVISIBLE);
            vh.tvUpdate.setVisibility(View.INVISIBLE);
        }

        vh.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 点击删除该帖子
                // 弹框 请用户确认后再删除
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("确认删除")
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .setMessage("您确定要删除吗？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 从服务器删除
                                post.delete(context, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                        // 如果有本地缓存，且本地缓存是被优先加载的情况下，也应该是从缓存清除数据
                                        // 从数据源删除
                                        remove(post);
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(context, "服务器繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("取消",null)
                        .create().show();

            }
        });

        vh.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击更新该帖子

                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("from","update");
                intent.putExtra("post",post);

                context.startActivity(intent);
            }
        });





        return view;
    }

    public void addAll(List<MyPost> list, boolean isClear) {
        if (isClear) {
            datas.clear();

        }

        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(MyPost post) {
        datas.remove(post);
        notifyDataSetChanged();
    }

    public class ViewHolder {

        @BindView(R.id.iv_item_avatar)
        ImageView ivAvatar;
        @BindView(R.id.tv_item_username)
        TextView tvName;
        @BindView(R.id.tv_item_title)
        TextView tvTitle;
        @BindView(R.id.tv_item_posttime)
        TextView tvTime;
        @BindView(R.id.tv_item_content)
        TextView tvContent;
        @BindView(R.id.tv_item_delete)
        TextView tvDelete;
        @BindView(R.id.tv_item_update)
        TextView tvUpdate;


        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }

    }


}
