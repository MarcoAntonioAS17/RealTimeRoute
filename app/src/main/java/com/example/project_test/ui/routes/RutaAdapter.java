package com.example.project_test.ui.routes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.R;
import com.example.project_test.models.RutaViewHolder;
import com.example.project_test.models.RutaViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RutaAdapter extends RecyclerView.Adapter<RutaViewHolder> {

    private List<RutaViewModel> RutaList;
    private Activity activity;

    public RutaAdapter(List<RutaViewModel> rList, Activity activity){
        this.RutaList = rList;
        this.activity = activity;
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

        holder.itemView.setOnClickListener(view ->{
            Bundle bundle = new Bundle();
            bundle.putString("Ruta",RutaList.get(position).getNombre());

            Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.mapa_rutas,bundle);
        } );
    }

    @Override
    public int getItemCount() {
        return RutaList.size();
    }


}
