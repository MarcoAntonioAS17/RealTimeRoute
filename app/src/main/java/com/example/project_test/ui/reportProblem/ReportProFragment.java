package com.example.project_test.ui.reportProblem;

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

public class ReportProFragment extends Fragment {

    private ReportProViewModel reportProViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        reportProViewModel =
                new ViewModelProvider(this).get(ReportProViewModel.class);
        View root = inflater.inflate(R.layout.fragment_report_problem, container,false);
        final TextView textView = root.findViewById(R.id.text_report_problem);
        reportProViewModel.getText().observe(getViewLifecycleOwner(),s -> textView.setText(s));
        return root;

    }
}
