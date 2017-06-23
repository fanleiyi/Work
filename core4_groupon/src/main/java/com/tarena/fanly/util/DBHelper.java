package com.tarena.fanly.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tarena.fanly.bean.CitynameBean;

import java.sql.SQLException;

/**
 * ORM： Object Relation Mapper
 * Object JAVA
 * Relation DB
 * Mappter 映射
 *
 * 1.一个JAVA类对应数据库中的一种数据表
 * 2.该类中的属性对应数据表中的字段
 * 3.每个类型的对象对应数据表中的一条记录
 *
 * Created by tarena on 2017/6/22.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper{
    private static DBHelper INSTANCE;

    public static DBHelper getInstance(Context  context){

        if(INSTANCE==null){
            synchronized (DBHelper.class){
                if(INSTANCE==null){
                    INSTANCE = new DBHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    private DBHelper(Context context) {
        super(context, "city.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        // 在第一次创建city.db 数据库时，该方法会被调用
        // 创建存数据库的数据表
        try {
            TableUtils.createTableIfNotExists(connectionSource, CitynameBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        // 当数据库结构发生变化的时候
        try {
            TableUtils.dropTable(connectionSource,CitynameBean.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
