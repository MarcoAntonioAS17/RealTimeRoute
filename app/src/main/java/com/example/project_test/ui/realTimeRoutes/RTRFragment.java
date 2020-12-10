package com.example.project_test.ui.realTimeRoutes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_test.R;

public class RTRFragment extends Fragment {

    private RTRViewModel RTRViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RTRViewModel =
                new ViewModelProvider(this).get(RTRViewModel.class);
        View root = inflater.inflate(R.layout.fragment_real_time_routes, container, false);
        final TextView textView = root.findViewById(R.id.text_real_time_routes);
        RTRViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }
}