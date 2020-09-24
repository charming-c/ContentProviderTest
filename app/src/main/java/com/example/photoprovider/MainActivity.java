package com.example.photoprovider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.Adapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * 设置一个瀑布流的RecyclerView来呈现相片内容,每一排四个
        * */
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        StaggerViewAdapter adapter=new StaggerViewAdapter();
        recyclerView.setAdapter(adapter);
    }
}