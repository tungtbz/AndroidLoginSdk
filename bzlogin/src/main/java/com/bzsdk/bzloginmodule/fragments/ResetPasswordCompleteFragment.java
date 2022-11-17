package com.bzsdk.bzloginmodule.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bzsdk.bzloginmodule.R;

public class ResetPasswordCompleteFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password_complete, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Button btn = getView().findViewById(R.id.done_btn);
        btn.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        getActivity().finish();
    }
}