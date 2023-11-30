package com.example.bluejackpharmacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bluejackpharmacy.databinding.ActivitySplashBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    FirebaseUser firebaseUser;
    ActivitySplashBinding binding;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        sharedPreferences = getSharedPreferences("FirstTimePrefs", MODE_PRIVATE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }

        binding.registerBtn.setOnClickListener(view -> {
            startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
        });

        binding.loginBtn.setOnClickListener(view -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        });
    }
}