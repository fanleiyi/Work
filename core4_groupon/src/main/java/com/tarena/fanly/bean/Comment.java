package com.tarena.fanly.bean;

import java.util.Arrays;

/**
 * Created by tarena on 2017/6/27.
 */

public class Comment {

    private String avatar;// 头像
    private String name; // 名字
    private String date; // 日期
    private String rating; // 打分
    private String price; // 价格
    private String content; // 评论正文
    private String[] imgs; // 配图 最多3张

    @Override
    public String toString() {
        return "Comment{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", rating='" + rating + '\'' +
                ", price='" + price + '\'' +
                ", content='" + content + '\'' +
                ", imgs=" + Arrays.toString(imgs) +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getImgs() {
        return imgs;
    }

    public void setImgs(String[] imgs) {
        this.imgs = imgs;
    }
}
