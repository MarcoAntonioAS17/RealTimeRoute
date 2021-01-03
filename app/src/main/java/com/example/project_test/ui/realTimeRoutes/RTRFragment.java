package com.example.project_test.ui.realTimeRoutes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.MainActivity;
import com.example.project_test.R;
import com.example.project_test.models.RutaViewModel;
import com.example.project_test.ui.routes.RutaAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RTRFragment extends Fragment {

    RecyclerView recyclerView;
    List<RutaViewModel> ListRutas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_real_time_routes, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //((MainActivity) getActivity()).hideFloatingActionButton();

        Activity activity = getActivity ();
        if (activity == null) return;

        recyclerView = view.findViewById(R.id.rv_rtr_rutas);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(view.getContext(),2);
        recyclerView.setLayoutManager(mGridLayoutManager);

        ListRutas = new ArrayList<>();
        readFile();

        RtrAdapter myAdapter = new RtrAdapter(ListRutas,activity);
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void readFile() {

        File fileEvents = new File(getContext().getFilesDir()+"/Rutas");

        if (fileEvents.exists()) {
            File[] files = fileEvents.listFiles();
            for (int i = 0; i < files.length; ++i) {

                File file = files[i];
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append("\n");
                    }
                    br.close();
                } catch (IOException e) { }
                try {
                    JSONObject obj = new JSONObject(text.toString());
                    RutaViewModel druta = new RutaViewModel(obj.getString("Nombre"),obj.getString("Foto"));
                    ListRutas.add(druta);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}