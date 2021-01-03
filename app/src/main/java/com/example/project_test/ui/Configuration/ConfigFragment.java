package com.example.project_test.ui.Configuration;

import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_test.MainActivity;
import com.example.project_test.R;
import com.example.project_test.models.DetallesRutaObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConfigFragment extends Fragment {

    private ConfigViewModel configViewModel;
    private FirebaseStorage storage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        configViewModel =
                new ViewModelProvider(this).get(ConfigViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuration, container, false);
        final TextView textView= root.findViewById(R.id.text_configuration);
        final Button Update_button = root.findViewById(R.id.button_update);
        configViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));

        Update_button.setOnClickListener(v -> {
            obtener_datos();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //((MainActivity) getActivity()).hideFloatingActionButton();
    }

    private void obtener_datos() {
        Snackbar snackbar = Snackbar.make (getView(), "Obteniendo información...", Snackbar.LENGTH_INDEFINITE);
        ViewGroup layer = (ViewGroup) snackbar.getView ().findViewById (com.google.android.material.R.id.snackbar_text).getParent ();
        ProgressBar bar = new ProgressBar (getContext ());
        layer.addView (bar);
        snackbar.show ();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        ArrayList<DetallesRutaObject> Data = new ArrayList<>();
        db.collection("Rutas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot elem: task.getResult()){
                                DetallesRutaObject druta = elem.toObject(DetallesRutaObject.class);
                                Log.d("TYAM",druta.getNombre() );
                                Data.add(druta);
                                Log.d("PhotoURL","=>"+druta.getPhoto());
                                DownloadImage(druta.getNombre(),druta.getPhoto());
                                druta.setPhoto(getContext().getFilesDir()+"/Fotos/"+druta.getNombre()+".jpg");
                                Log.d("PhotoFile","=>"+druta.getPhoto());
                            }
                            switch (save_data(Data)){
                                case 0:
                                    snackbar.dismiss ();
                                    Toast.makeText(getContext(),"Datos guardados ",Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    snackbar.dismiss ();
                                    Toast.makeText(getContext(),"Espacio insuficiente ",Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    snackbar.dismiss ();
                                    Toast.makeText(getContext(),"Error al guardar datos ",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(),"Error al guardar datos ",Toast.LENGTH_LONG).show();
                        }
                    }


                });

    }

    public void DownloadImage(String fileName,String url) {
        StorageReference photoBusRef = storage.getReferenceFromUrl(url);
        File root = new File(getContext().getFilesDir(), "Fotos");
        if(!root.exists())
            root.mkdirs();
        File archivo = new File(root,fileName+".jpg");
        photoBusRef.getFile(archivo)
                .addOnSuccessListener(
                    taskSnapshot -> Log.d("PHOTO","=>Archivo creado "+archivo.getAbsolutePath()))
                .addOnFailureListener(
                    e -> e.printStackTrace());

    }

    public int save_data(List<DetallesRutaObject> list){
        List<JSONObject> lista_json = new ArrayList<>();

        File root = new File(getContext().getFilesDir(), "Rutas");
        if(!root.exists())
            root.mkdirs();

        for( DetallesRutaObject elem: list) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Nombre", elem.getNombre());
                jsonObject.put("Horario", elem.getHorario());
                jsonObject.put("Foto", elem.getPhoto());
                jsonObject.put("Ruta_Ida", elem.getRuta_Ida());
                jsonObject.put("Ruta_Vuelta", elem.getRuta_Vuelta());
                lista_json.add(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return 2;
            }
        }
        long espacio_requerido = lista_json.toString().getBytes().length;
        //Comprobamos que halla espacio suficiente
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            StorageManager storageManager = getContext().getSystemService(StorageManager.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try {
                    UUID appSepecificInternalDirUuid = storageManager.getUuidForPath(root);
                    long availableBytes =
                            storageManager.getAllocatableBytes(appSepecificInternalDirUuid);
                    if(availableBytes >= espacio_requerido) {
                        storageManager.allocateBytes(
                                appSepecificInternalDirUuid, espacio_requerido);
                    }else{
                        return 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return 2;
                }
            }
        }

        for(JSONObject jsonObject: lista_json){
            try {
                File archivo = new File(root,jsonObject.getString("Nombre")+".json");
                FileWriter fileWriter = new FileWriter(archivo);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonObject.toString());
                bufferedWriter.close();
                fileWriter.close();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return 2;
            }

        }
        return 0;

    }
}
