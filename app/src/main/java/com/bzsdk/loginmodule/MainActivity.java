package com.bzsdk.loginmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.bzsdk.bzloginmodule.LoginActivity;
import com.bzsdk.bzloginmodule.LoginService;
import com.bzsdk.bzloginmodule.network.NetworkService;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkService.getInstance().Init(this);
        NetworkService.getInstance().SetDebug(true);

        LoginService.getInstance().setGoogleWebClientId(getString(R.string.default_web_client_id));
        LoginService.getInstance().setSignInListener(data -> {
            Log.d("MainActivity", "On Login Success: " + data);

            LoginService.getInstance().getCurrentUserInfo(LoginService.getInstance().getCurrentToken(), new NetworkService.GetUserInfoCallback() {
                @Override
                public void onSuccess(String jsonStr) {

                }

                @Override
                public void onError(String message) {

                }
            });
        });
        Button button = findViewById(R.id.btn_showLogin);
        button.setOnClickListener(view -> {

            LoginService.getInstance().getCurrentUserInfo("", new NetworkService.GetUserInfoCallback() {
                @Override
                public void onSuccess(String jsonStr) {

                }

                @Override
                public void onError(String message) {

                }
            });

            //Show Login Activity
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
    }
}