package com.example.project_test.ui.realTimeRoutes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.R;
import com.example.project_test.models.RutaViewHolder2;
import com.example.project_test.models.RutaViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RtrAdapter extends RecyclerView.Adapter<RutaViewHolder2> {

    private List<RutaViewModel> RutaList;
    private Activity activity;
    private EditText texto;

    public RtrAdapter(List<RutaViewModel> rList, Activity activity, EditText texto){
        this.RutaList = rList;
        this.activity = activity;
        this.texto = texto;
    }

    @NonNull
    @Override
    public RutaViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item2,parent,false);
        return new RutaViewHolder2(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RutaViewHolder2 holder, int position) {

        holder.mTitle.setText(RutaList.get(position).getNombre());

        holder.itemView.setOnClickListener(view ->{
            Bundle bundle = new Bundle();
            bundle.putString("Ruta",RutaList.get(position).getNombre());
            bundle.putString("Texto",texto.getText().toString());

            Navigation.findNavController(activity,R.id.nav_host_fragment).navigate(R.id.mapa_rtr_rutas,bundle);
        } );
    }

    @Override
    public int getItemCount() {
        return RutaList.size();
    }


}

