package com.nikola.jakshic.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nikola.jakshic.instagramauth.AuthManager;
import com.nikola.jakshic.instagramauth.InstagramAuthException;
import com.nikola.jakshic.instagramauth.InstagramAuthNetworkOperationException;
import com.nikola.jakshic.instagramauth.InstagramAuthTokenException;
import com.nikola.jakshic.instagramauth.UserInfo;

public class ProfileActivity extends AppCompatActivity {

    private ImageView userPhoto;
    private TextView userName;
    private TextView userFullName;
    private TextView userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = findViewById(R.id.user_name);
        userFullName = findViewById(R.id.user_full_name);
        userId = findViewById(R.id.user_id);
        userPhoto = findViewById(R.id.user_photo);

        AuthManager.getInstance().getUserInfoAsync(new AuthManager.Callback<UserInfo>() {
            @Override
            public void onSuccess(@NonNull UserInfo item) {
                Glide.with(ProfileActivity.this).load(item.getPhotoUrl()).into(userPhoto);
                userName.setText(getString(R.string.user_name, item.getUserName()));
                userFullName.setText(getString(R.string.user_full_name, item.getFullName()));
                userId.setText(getString(R.string.user_id, item.getId()));
            }

            @Override
            public void onError(@NonNull InstagramAuthException e) {
                if (e instanceof InstagramAuthTokenException) {
                    // Instagram has expired the token, we need to re-authenticate the User.
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    finish();
                }

                if (e instanceof InstagramAuthNetworkOperationException) {
                    // Network problem, do something...
                }
            }
        });

        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.getInstance().logout();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}