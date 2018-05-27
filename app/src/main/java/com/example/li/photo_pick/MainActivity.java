package com.example.li.photo_pick;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase dbw;
    private SQLiteDatabase dbr;
    private PictureDatabase pictureDatabase;
    private Context mContext;

    //private static final int CHOOSE_PHOTO = 0;
    private ImageView imageView;
    private ImageView imgShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imgView);
        imgShow = findViewById(R.id.imgShow);
        mContext = MainActivity.this;
        pictureDatabase = new PictureDatabase(mContext);
    }

    public void openAlbum(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri uri=data.getData();
            ContentResolver cr=this.getContentResolver();
            Bitmap bitmap=null;
            Bundle extras=null;
            if(requestCode==1){
                String[] proj ={MediaStore.Images.Media.DATA};
                Cursor actualimagecursor=managedQuery(uri,proj,null,null,null);
                int actual_image_column_index=actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String path=actualimagecursor.getString(actual_image_column_index);
                try{
                    bitmap=BitmapFactory.decodeStream(cr.openInputStream(uri));
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    /**
     * 将图片存放入数据库
     * 从image view中获取图片源bitmap，转为二进制数组存储
     * @param view
     */
    public void saveToDatabase(View view){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();
        imageView.setDrawingCacheEnabled(false);
        dbw = pictureDatabase.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pic", getPicture(bitmap));
        dbw.insert("picture", null,cv);
        Toast.makeText(this,"fuck you!",Toast.LENGTH_SHORT).show();
    }

    //将Bitmap转换成可以用来存储的byte[]类型，采用ByteBuffer实现
    private byte[] getPicture(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
        return buffer.array();
    }

    public void imgShow(View view){
        dbr = pictureDatabase.getReadableDatabase();
        Cursor cursor = dbr.query("picture",null,null,null,null,
                null,null);
        byte[] imgByte = null;
        if(cursor.moveToNext()){
           imgByte = cursor.getBlob(cursor.getColumnIndex("pic"));
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte,0,imgByte.length);
        imgShow.setImageBitmap(bitmap);
    }

}
