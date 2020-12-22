package com.example.project_test.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_test.R;
import com.example.project_test.models.DetallesRutaObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class RoutesFragment extends Fragment {

    private RoutesViewModel routesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        routesViewModel =
                new ViewModelProvider(this).get(RoutesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_see_routes, container, false);
        final TextView textView = root.findViewById(R.id.text_see_routes);
        routesViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore db  = FirebaseFirestore.getInstance();


        db.collection("Rutas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DetallesRutaObject druta = document.toObject(DetallesRutaObject.class);
                                Log.d("TYAM", druta.getNombre() + " IDA=> " + druta.getRuta_Ida().toString());
                                Log.d("TYAM", druta.getNombre() + " VUELTA=> " + druta.getRuta_Vuelta().toString());
                                /*
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("Nombre",druta.getNombre());
                                    jsonObject.put("Horario",druta.getHorario());
                                    jsonObject.put("Foto",druta.getPhoto());
                                    jsonObject.put("RutaI",druta.getRuta());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("TYAM",  "JSON => " + jsonObject.toString());
                                */
                            }
                        } else {
                            Log.d("TYAM", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}