package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.tarena.fanly.R;
import com.tarena.fanly.adapter.CommentAdapter;
import com.tarena.fanly.bean.BusinessBean;
import com.tarena.fanly.bean.Comment;
import com.tarena.fanly.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends Activity {

    BusinessBean.Business business;
    @BindView(R.id.list_Detail)
    ListView listView;

    List<Comment> datas;
    CommentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        business= (BusinessBean.Business) getIntent().getSerializableExtra("business");
        Log.i("TAG","Business-> "+business.getName());
        ButterKnife.bind(this);
        initListView();
    }

    private void initListView() {
        datas = new ArrayList<>();
        adapter = new CommentAdapter(this,datas);
        LayoutInflater inflater = LayoutInflater.from(this);
        View headerBusiness = inflater.inflate(R.layout.item_business_layout, listView, false);
        initHeaderBusiness(headerBusiness);
        View headerInfo = inflater.inflate(R.layout.detail_layout_01, listView, false);
        initHeaderInfo(headerInfo);
        listView.addHeaderView(headerBusiness);
        listView.addHeaderView(headerInfo);
        TextView tv_arddess = (TextView) headerInfo.findViewById(R.id.tv_detail_arrdess);
        tv_arddess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,FindActivity.class);
                intent.putExtra("business",business);
                intent.putExtra("from","detail");
                startActivity(intent);
            }
        });
        listView.setAdapter(adapter);
    }


    private void initHeaderInfo(View view) {
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_detail_arrdess);
        tvAddress.setText(business.getAddress());
        TextView tvTelphone = (TextView) view.findViewById(R.id.tv_detail_phone);
        tvTelphone.setText(business.getTelephone());
    }

    private void initHeaderBusiness(View view) {
        ImageView ivPic = (ImageView) view.findViewById(R.id.iv_business_item);
        ImageView ivRating = (ImageView) view.findViewById(R.id.iv_business_item_rating);
        TextView tvName = (TextView) view.findViewById(R.id.tv_business_item_name);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_business_item_avg_price);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_business_item_info);
        TextView tvDistance = (TextView) view.findViewById(R.id.tv_business_item_distance);

        HttpUtil.loadImage(business.getPhoto_url(), ivPic);

        String name = business.getName().substring(0, business.getName().indexOf("("));
        if (!TextUtils.isEmpty(business.getBranch_name())) {
            name = name + "(" + business.getBranch_name() + ")";
        }
        tvName.setText(name);

        int[] stars = new int[]{R.drawable.movie_star10,
                R.drawable.movie_star20,
                R.drawable.movie_star30,
                R.drawable.movie_star35,
                R.drawable.movie_star40,
                R.drawable.movie_star45,
                R.drawable.movie_star50};
        Random rand = new Random();
        int idx = rand.nextInt(7);
        ivRating.setImageResource(stars[idx]);

        int price = rand.nextInt(100) + 50;

        tvPrice.setText("￥" + price + "/人");

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < business.getRegions().size(); j++) {

            if (j == 0) {
                sb.append(business.getRegions().get(j));
            } else {
                sb.append("/").append(business.getRegions().get(j));
            }


        }

        sb.append(" ");

        for (int j = 0; j < business.getCategories().size(); j++) {
            if (j == 0) {
                sb.append(business.getCategories().get(j));
            } else {
                sb.append("/").append(business.getCategories().get(j));
            }
        }

        tvInfo.setText(sb.toString());

        tvDistance.setText("");


    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {

        HttpUtil.getCommentByVolley(business.getReview_list_url(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                // 解析
                List<Comment> comments=new ArrayList<Comment>();
                Document document= Jsoup.parse(s);
                Elements elements = document.select("div[class=comment-list] li[data-id]");
                for (Element element:elements){
                    Comment comment=new Comment();

                    Element imgElement = element.select("div[class=pic] img").get(0);
                    comment.setName(imgElement.attr("title"));
                    comment.setAvatar(imgElement.attr("src"));

                    Elements spanElements = element.select("div[class=user-info] span[comm-per]");
                    if (spanElements.size()>0){
                        Element spanElement = spanElements.get(0);
                        comment.setPrice(spanElement.text().split(" ")[1] + "/人");
                    }else {
                        comment.setPrice("");
                    }

                    Element spanElement = element.select("div[class=user-info] span[title]").get(0);
                    String rate = spanElement.attr("class");
                    comment.setRating(rate.split("-")[3]);

                    Element divElement = element.select("div[class=J_brief-cont]").get(0);
                    comment.setContent(divElement.text());

                    Elements imgElelects = element.select("div[class=shop-photo] img");
                    int size=imgElelects.size();
                    if (size>3){
                        size=3;
                    }
                    String[] imgs=new String[size];
                    for (int i=0;i<size;i++){
                        imgs[i]=imgElelects.get(i).attr("src");
                    }
                    comment.setImgs(imgs);

                    Element dateElement = element.select("div[class=misc-info] span[class=time]").get(0);
                    comment.setDate(dateElement.text());

                    comments.add(comment);
                }
                Log.i("TAG","评论内容--> " +comments);
                    datas=comments;
                adapter.addAll(comments,true);

                // 放到ListView呈现

            }
        });

/*     HttpUtil.getComment(business.getReview_list_url(), new HttpUtil.OnResponseListener<Document>() {
        @Override
        public void onResponse(Document document) {
          // 解析
            List<Comment> comments=new ArrayList<Comment>();
            Elements elements = document.select("div[class=comment-list] li[data-id]");
            for (Element element:elements){
                Comment comment=new Comment();

                Element imgElement = element.select("div[class=pic] img").get(0);
                comment.setName(imgElement.attr("title"));
                comment.setAvatar(imgElement.attr("src"));

                Elements spanElements = element.select("div[class=user-info] span[comm-per]");
                if (spanElements.size()>0){
                    Element spanElement = spanElements.get(0);
                    comment.setPrice(spanElement.text().split(" ")[1] + "/人");
                }else {
                    comment.setPrice("");
                }

                Element spanElement = element.select("div[class=user-info] span[title]").get(0);
                String rate = spanElement.attr("class");
                comment.setRating(rate.split("-")[3]);

                Element divElement = element.select("div[class=J_brief-cont]").get(0);
                comment.setContent(divElement.text());

                Elements imgElelects = element.select("div[class=shop-photo] img");
                int size=imgElelects.size();
                if (size>3){
                    size=3;
                }
                String[] imgs=new String[size];
                for (int i=0;i<size;i++){
                    imgs[i]=imgElelects.get(i).attr("src");
                }
                comment.setImgs(imgs);

                Element dateElement = element.select("div[class=misc-info] span[class=time]").get(0);
                comment.setDate(dateElement.text());

                comments.add(comment);
            }
            Log.i("TAG","评论内容--> " +comments);


            // 放到ListView呈现

         }
     });*/

    }
}
