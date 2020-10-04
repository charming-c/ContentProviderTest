package com.example.photoprovider;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.net.URI;
import java.util.List;

public class StaggerViewAdapter extends RecyclerView.Adapter<StaggerViewAdapter.InnerHolder> {
    private List<PhotoBean> photos;
    private OnItemClickListener mOnItemClickListener;

    public StaggerViewAdapter(List<PhotoBean> photos) {
        this.photos=photos;
    }

    @NonNull
    @Override
    public StaggerViewAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.photo_item,parent,false);
        Point point= ScreenSizeUtils.getScreenSize(view.getContext());
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(point.x/4,point.x/4);
        view.setLayoutParams(layoutParams);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggerViewAdapter.InnerHolder holder, final int position) {
        if(photos!=null){
            ImageView imageView = holder.itemView.findViewById(R.id.photo);
            Glide.with(imageView.getContext()).load(photos.get(position).getImgPath()).into(imageView);
            holder.setData(photos.get(position));
            Log.d("photo","x:"+photos.get(position).getImgPath());
        }
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        //设置一个监听器，其实就是设置一个回调的接口
        this.mOnItemClickListener=listener;

    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView path;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            icon=itemView.findViewById(R.id.photo);
            path=itemView.findViewById(R.id.path);
        }

        public void setData(PhotoBean photoBean) {
            Log.d("photo","y:"+photoBean.getImgPath());
            File file = new File(photoBean.getImgPath());
            if(file!=null){

//                Uri uri=Uri.fromFile(new File(photoBean.getImgPath()));
//                Bitmap bm = BitmapFactory.decodeFile(photoBean.getImgPath());
//                icon.setImageBitmap(bm);
            }
//            String ImagePath = photoBean.getImgPath();
//            Uri uri = Uri.parse(ImagePath);
//            icon.setImageURI(uri);
//            path.setText(photoBean.getImgPath());
        }
    }
}
