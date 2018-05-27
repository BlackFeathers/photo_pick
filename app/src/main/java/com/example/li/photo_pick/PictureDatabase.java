package com.example.li.photo_pick;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;

import java.io.ByteArrayOutputStream;

public class PictureDatabase extends SQLiteOpenHelper {

    //数据库名
    private static final String DATABASE_NAME = "picture.db";
    //数据库版本号
    private static final int DATABASE_Version = 1;
    //表名
    private static final String TABLE_NAME = "picture";

    //创建数据库
    public PictureDatabase (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }
    //创建表并初始化表
    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE picture(PID INTEGER PRIMARY KEY AUTOINCREMENT,pic blob not null)");
    }

    //更新数据库
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
