package com.example.project_test.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.R;

import java.util.List;

public class RutaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView mImage;
    TextView mTitle;
    OnBusListener onBusListener;

    public RutaViewHolder(@NonNull View itemView, OnBusListener onBusListener) {
        super(itemView);

        mImage = itemView.findViewById(R.id.rv_iv_foto);
        mTitle = itemView.findViewById(R.id.rv_title);
        this.onBusListener = onBusListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onBusListener.onBusClick(getAdapterPosition());
    }
}
