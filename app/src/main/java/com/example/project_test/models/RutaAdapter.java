package com.example.project_test.models;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class RutaAdapter extends RecyclerView.Adapter<RutaViewHolder> {
    private Context context;
    private List<RutaViewModel> RutaList;

    public RutaAdapter(Context mContext, List<RutaViewModel> rList){
        this.context = mContext;
        this.RutaList = rList;
    }

    @NonNull
    @Override
    public RutaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_bus,parent,false);
        return new RutaViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RutaViewHolder holder, int position) {
        Picasso.get().load("file:"+RutaList.get(position).getImg()).into(holder.mImage);
        holder.mTitle.setText(RutaList.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return RutaList.size();
    }
}
