package com.example.project_test.ui.reportProblem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_test.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportProFragment extends Fragment {

    EditText etName, etCorreo, etReporte;
    Button send;
    String repuesta;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_report_problem, container,false);
        etName = root.findViewById(R.id.edit_report_problem_nombre);
        etCorreo = root.findViewById(R.id.edit_report_problem_contacto);
        etReporte = root.findViewById(R.id.edit_report_problema);

        send = root.findViewById(R.id.button_enviar_report);

        send.setOnClickListener(v -> {
            guardar_reporte();
        });

        return root;

    }

    private void guardar_reporte() {
        Snackbar snackbar = Snackbar.make (getView(), "Enviando reporte...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup layer = (ViewGroup) snackbar.getView ().findViewById (com.google.android.material.R.id.snackbar_text).getParent ();
        ProgressBar bar = new ProgressBar (getContext ());
        layer.addView (bar);
        snackbar.show ();

        String nombre = etName.getText().toString();
        String correo = etCorreo.getText().toString();
        String reporte = etReporte.getText().toString();
        Date fecha = new Date();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> reportehash = new HashMap<>();
        reportehash.put("Nombre", nombre);
        reportehash.put("Correo", correo);
        reportehash.put("Reporte",reporte);
        reportehash.put("Fecha",fecha.toString());

        db.collection("Reportes")
                .add(reportehash)
                .addOnSuccessListener(documentReference -> {
                    Log.d("TYAM", "DocumentSnapshot added with ID: " + documentReference.getId());
                    snackbar.dismiss ();
                    Toast.makeText(getContext(),"Enviado con exito",Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Log.w("TYAM", "Error adding document", e);
                    snackbar.dismiss ();
                    Toast.makeText(getContext(),"Error al guardar",Toast.LENGTH_LONG).show();
                });

        Log.w("TYAM",fecha+"=>"+nombre+"=>"+correo+"=>"+reporte);
        etName.setText("");
        etCorreo.setText("");
        etReporte.setText("");

    }
}
