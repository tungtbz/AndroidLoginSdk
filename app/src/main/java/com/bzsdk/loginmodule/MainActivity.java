package com.bzsdk.loginmodule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bzsdk.bzloginmodule.LoginActivity;
import com.bzsdk.bzloginmodule.LoginService;
import com.rofi.core.network.NetworkService;
import com.rofi.referral.RofiReferralHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkService.getInstance().Init(this);
        NetworkService.getInstance().SetDebug(true);

        LoginService.getInstance().Init(getApplicationContext());
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
            //Show Login Activity
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        });

        Button checkInRefCodeBtn = findViewById(R.id.btn_checkin);
        checkInRefCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RofiReferralHelper.getInstance().RequestCheckInRefCode(
                        LoginService.getInstance().getCurrentToken(),
                        getString(R.string.campaignCode),
                        getString(R.string.gameId),
                        getString(R.string.refCode),
                        new NetworkService.BaseCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(String message) {

                            }
                        }
                );
            }
        });

        Button joinRefButton = findViewById(R.id.btn_join);
        joinRefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RofiReferralHelper.getInstance().JoinCampaign(LoginService.getInstance().getCurrentToken(), getString(R.string.campaignCode), new RofiReferralHelper.RefLinkCallback() {
                    @Override
                    public void onSuccess(String data) {

                    }

                    @Override
                    public void onError(String data) {

                    }
                });
            }
        });
    }
}