package com.example.project_test.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.R;

import java.util.List;

public class RutaViewHolder extends RecyclerView.ViewHolder {
    ImageView mImage;
    TextView mTitle;

    public RutaViewHolder(@NonNull View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.rv_iv_foto);
        mTitle = itemView.findViewById(R.id.rv_title);

    }

}
