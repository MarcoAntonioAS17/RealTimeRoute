package com.example.project_test.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_test.MainActivity;
import com.example.project_test.R;
import com.example.project_test.models.DetallesRutaObject;
import com.example.project_test.models.RutaAdapter;
import com.example.project_test.models.RutaViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoutesFragment extends Fragment {

    private RoutesViewModel routesViewModel;
    RecyclerView recyclerView;
    List<RutaViewModel> ListRutas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routesViewModel =
                new ViewModelProvider(this).get(RoutesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_see_routes, container, false);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).hideFloatingActionButton();

        recyclerView = view.findViewById(R.id.rv_rutas);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(view.getContext(),2);
        recyclerView.setLayoutManager(mGridLayoutManager);

        ListRutas = new ArrayList<>();
        readFile();

        RutaAdapter myAdapter = new RutaAdapter(getContext(),ListRutas);
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