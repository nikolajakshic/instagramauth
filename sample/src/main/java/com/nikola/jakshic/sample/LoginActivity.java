package com.nikola.jakshic.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.nikola.jakshic.instagramauth.AuthManager;
import com.nikola.jakshic.instagramauth.InstagramAuthAccessDeniedException;
import com.nikola.jakshic.instagramauth.InstagramAuthException;
import com.nikola.jakshic.instagramauth.InstagramAuthNetworkOperationException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // If User is already logged in, skip this Activity.
        if (AuthManager.getInstance().isLoggedIn()) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }

        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().login(LoginActivity.this, new AuthManager.LoginCallback() {
                    @Override
                    public void onSuccess() {
                        // User successfully logged in, start ProfileActivity
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(@NonNull InstagramAuthException e) {
                        if (e instanceof InstagramAuthAccessDeniedException) {
                            // User denied access, do something...
                        }

                        if (e instanceof InstagramAuthNetworkOperationException) {
                            // Network problem, do something...
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AuthManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}