package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tarena.fanly.R;
import com.tarena.fanly.app.MyApp;
import com.tarena.fanly.bean.MyUser;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;

public class MainActivity extends Activity {

    @BindView(R.id.ed_main_1)
    EditText ed1;
    @BindView(R.id.ed_main_2)
    EditText ed2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.btn_main_1)
    public void regist(View v){
        startActivity(new Intent(this,RegisterActivity.class));
    }
     @OnClick(R.id.btn_main_2)
    public void login(View v){
         final String username = ed1.getText().toString();
         final String password = ed2.getText().toString();
         if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
             Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
             return;
         }else {
             // 以用户输入的用户名作为查询条件查询bmob服务器MyUser数据表中是否有相同的数据
             BmobQuery<MyUser> query=new BmobQuery<>();

             query.addWhereEqualTo(
                     "username",// 数据表的列名
                     username);// 用户输入的用户名
             // 发起查询
             query.findObjects(this, new FindListener<MyUser>() {
                 @Override
                 public void onSuccess(List<MyUser> list) {
                     // 服务器给了正常响应
                     // 根据list中是否有元素来判定结果
                     if (list.size()>0){
                         // 数据表中有一条数据记录  其username字段为用户输入的用户名
                         MyUser myUser = list.get(0);
                         String md5=new String(Hex.encodeHex(DigestUtils.sha(password)));
                         if (myUser.getUsername().equals(username)&&myUser.getPassword().equals(md5)){
                             // 将user登录成功的消息告诉所有客户端
                             BmobPushManager<BmobInstallation> manager=new BmobPushManager<BmobInstallation>(MainActivity.this);
                             manager.pushMessageAll(username + "已登录！", new PushListener() {
                                 @Override
                                 public void onSuccess() {
                                     Log.d("TAG", "登录成功 ");
                                 }

                                 @Override
                                 public void onFailure(int i, String s) {
                                     Log.d("TAG", "登录失败: "+i+":"+s);
                                 }
                             });

                             Toast.makeText(MainActivity.this,"登录成功!",Toast.LENGTH_SHORT).show();
                             Intent intent=new Intent(MainActivity.this,ShowActivity.class);
                             MyApp.loginUser=myUser;
                             startActivity(intent);
                             finish();
                         }else {
                             Toast.makeText(MainActivity.this,"用户名或密码错误!",Toast.LENGTH_SHORT).show();
                         }
                     }else {
                         Toast.makeText(MainActivity.this,"用户名或密码错误!",Toast.LENGTH_SHORT).show();
                     }
                 }

                 @Override
                 public void onError(int i, String s) {
                     Toast.makeText(MainActivity.this,"服务器繁忙，请稍后重试!",Toast.LENGTH_SHORT).show();
                 }
             });
         }
     }

}
