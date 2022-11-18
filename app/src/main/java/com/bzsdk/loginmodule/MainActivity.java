package com.bzsdk.loginmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.bzsdk.bzloginmodule.LoginActivity;
import com.bzsdk.bzloginmodule.LoginService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginService.getInstance().setGoogleWebClientId(getString(R.string.default_web_client_id));

        Button button = findViewById(R.id.btn_showLogin);
        button.setOnClickListener(view -> {
            //Show Login Activity
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        });
    }
}