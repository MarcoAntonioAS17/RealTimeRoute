package com.example.project_test.ui.Configuration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_test.R;

public class ConfigFragment extends Fragment {

    private ConfigViewModel configViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        configViewModel =
                new ViewModelProvider(this).get(ConfigViewModel.class);
        View root = inflater.inflate(R.layout.fragment_configuration, container, false);
        final TextView textView= root.findViewById(R.id.text_configuration);
        configViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));

        return root;
    }
}
