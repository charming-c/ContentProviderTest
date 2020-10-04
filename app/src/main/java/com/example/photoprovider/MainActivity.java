package com.example.photoprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int SCAN_OK=1;
    private Dialog mProgressDialog;
    private StaggerViewAdapter adapter;
    private List<PhotoBean> photos = new ArrayList<>();

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //扫描完毕,关闭进度dialog
                mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        }
    };
    private HashMap<String,List<PhotoBean>> mGroupMap= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        Glide.with(this);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                //弹窗解释为何需要该权限，再次请求权限
                Toast.makeText(MainActivity.this, "请授权！", Toast.LENGTH_LONG).show();
                //跳转到应用设置界面
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                initView();
                Log.d("perssion","1");
            }else {
                //不需要解释为何需要授权直接请求授权
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
            initView();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授权成功
                   initView();
                } else {
                    //授权失败
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    private void initView() {
        /*
         * 设置一个瀑布流的RecyclerView来呈现相片内容,每一排四个
         * */
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new StaggerViewAdapter(photos);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new StaggerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("1","dianji");
            }
        });
        if(photos.size()==0){
            getPhotos();
        }
    }
//  利用contentProvider扫描，获取手机中的图片，此方法运行在子线程里，方便快捷
    private void getPhotos() {
        mProgressDialog = ProgressDialog.show(this ,null,"loading...");
//        开启线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = MainActivity.this.getContentResolver();
                Cursor cursor = contentResolver.query(photoUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
                if(cursor==null) return;
                while (cursor.moveToNext()){
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    PhotoBean bean = new PhotoBean(path,false);
                    photos.add(bean);
                    Log.d("photo","x:"+bean.getImgPath());
                    String parentName = new File(path).getParentFile().getName();
                    if (!mGroupMap.containsKey(parentName)) {
                        List<PhotoBean> childList = new ArrayList<PhotoBean>();
                        PhotoBean imageBean = new PhotoBean(path, false);
                        childList.add(imageBean);
                        mGroupMap.put(parentName, childList);
                    } else {
                        mGroupMap.get(parentName).add(new PhotoBean(path, false));
                    }
                }
                mGroupMap.put("全部图片", photos);

                handler.sendEmptyMessage(SCAN_OK);
                cursor.close();
                }
        }).start();

    }
    private List<PhotoGroupBean> subGroupOfImage(HashMap<String, List<PhotoBean>> mGroupMap) {
            if (mGroupMap.size() == 0) {
                return null;
            }
            //遍历
            List<PhotoGroupBean> list = new ArrayList<PhotoGroupBean>();
            Iterator<Map.Entry<String, List<PhotoBean>>> it = mGroupMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<PhotoBean>> entry = it.next();
                PhotoGroupBean mImageBean = new PhotoGroupBean();

                //根据key获取其中图片list
                String key = entry.getKey();
                List<PhotoBean> value = entry.getValue();

                mImageBean.setFolderName(key);//获取该组文件夹名称
                mImageBean.setImageCounts(value.size());//获取该组图片数量
                mImageBean.setTopImagePath(value.get(0).getImgPath());//获取该组的第一张图片
                //将全部图片放在第一位置
                if (mImageBean.getFolderName().equals("全部图片")){
                    list.add(0,mImageBean);
                }else {
                    list.add(mImageBean);
                }
            }
            return list;
    }
}