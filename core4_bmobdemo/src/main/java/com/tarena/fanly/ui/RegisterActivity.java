package com.tarena.fanly.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tarena.fanly.R;
import com.tarena.fanly.app.MyApp;
import com.tarena.fanly.bean.MyUser;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class RegisterActivity extends Activity {

    @BindView(R.id.img_rg_photo)
    ImageView imageView;

    @BindView(R.id.ed_rg_1)
    EditText ed1;
    @BindView(R.id.ed_rg_2)
    EditText ed2;
    @BindView(R.id.radio_rg)
    RadioGroup group;

    String photo;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_rg_1)
    public void regist(View v){
        String username = ed1.getText().toString();
        String password = ed2.getText().toString();
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else{
            MyUser myUser=new MyUser();
            myUser.setUsername(username);
            String md5=new String(Hex.encodeHex(DigestUtils.sha(password)));
            myUser.setPassword(md5);
            if (group.getCheckedRadioButtonId()==R.id.radiobtn_1) {
                myUser.setGender(true);
            }else {
                myUser.setGender(false);
            }
            myUser.setAvatar(photo);

            // 将数据存到Bmob
            myUser.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    imageView=null;
                    ed1.setText("");
                    ed2.setText("");
                    group.check(R.id.radiobtn_1);
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(RegisterActivity.this,"注册失败："+"错误代码："+i+",错误原因："+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 利用Intent选择器实现多种途径设置用户头像
     * （图库，相机）
     * @param v
     */
    @OnClick(R.id.img_rg_photo)
    public void setAvatar(View v){
        // 开启系统图库
        Intent intent1=new Intent(Intent.ACTION_PICK);
        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        // 打开系统相机
        Intent intent2=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),System.currentTimeMillis()+".jpg");

        path = file.getAbsolutePath();
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, path);

        Intent chooser = Intent.createChooser(intent1, "选择头像...");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{intent2});

        startActivityForResult(chooser,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK && requestCode==100){
            // 处理用户选择或拍摄的图片
            final String filePath;
            if (data!=null){
                // 头像从图库选择
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                cursor.moveToNext();
                filePath = cursor.getString(0);

            }else {
                // 头像是拍摄的
                filePath=path;
            }

            // 上传到bmob服务器
            final BmobFile bf=new BmobFile(new File(filePath));
            bf.uploadblock(MyApp.CONTEXT, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    photo = bf.getFileUrl(MyApp.CONTEXT);// 头像在服务器的地址
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(RegisterActivity.this,"头像上传失败："+"i:"+s,Toast.LENGTH_SHORT).show();
                }
            });

        }


    }
}
