package com.example.photoprovider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StaggerViewAdapter extends RecyclerView.Adapter<StaggerViewAdapter.InnerHolder> {
    @NonNull
    @Override
    public StaggerViewAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.photo_item,parent,false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggerViewAdapter.InnerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
