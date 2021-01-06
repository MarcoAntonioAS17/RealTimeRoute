package com.example.project_test.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.R;

public class RutaViewHolder2 extends RecyclerView.ViewHolder {

    public TextView mTitle;

    public RutaViewHolder2(@NonNull View itemView) {
        super(itemView);

        mTitle = itemView.findViewById(R.id.rv_title);

    }

}