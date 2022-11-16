package com.bzsdk.bzloginmodule.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bzsdk.bzloginmodule.R;


public class PassordRecoveryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passord_recovery, container, false);
    }

    private void InitView(){
        Button sendOtpBtn = getView().findViewById(R.id.send_opt_btn);
        sendOtpBtn.setOnClickListener(view -> {

        });
    }
}