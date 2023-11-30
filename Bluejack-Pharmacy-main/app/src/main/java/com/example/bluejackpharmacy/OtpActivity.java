package com.example.bluejackpharmacy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bluejackpharmacy.databinding.ActivityOtpBinding;

import java.util.ArrayList;

public class OtpActivity extends AppCompatActivity {

    ActivityOtpBinding binding;

    String otp = "12969";
    String message = "Your verification code for Bluejack Pharmacy are: " + otp;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phone = (String) getIntent().getStringExtra("phoneNum");
//        phone = "15551234567";
        checkPermission();

        binding.resendText.setOnClickListener(view -> {
            checkPermission();
        });

        binding.verifOtpBtn.setOnClickListener(view -> {
            if (binding.edtOtpNum.getText().toString().equals(otp)) {
                Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Wrong OTP code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(OtpActivity.this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendOtp();
        } else {
            ActivityCompat.requestPermissions(OtpActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            sendOtp();
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOtp() {

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(phone, null, parts, null, null);

    }
}