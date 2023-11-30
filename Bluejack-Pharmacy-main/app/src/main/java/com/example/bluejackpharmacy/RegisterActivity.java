package com.example.bluejackpharmacy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluejackpharmacy.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference reference;
    ActivityRegisterBinding binding;
    Dialog progressDialog;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        auth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
        initDialog();
        binding.registerBtn.setOnClickListener(view -> {
            String str_username = binding.nameEdt.getEditText().getText().toString();
            String str_email = binding.emailEdt.getEditText().getText().toString();
            String str_password = binding.passEdt.getEditText().getText().toString();
            String str_confirmPassword = binding.confPassEdt.getEditText().getText().toString();
            String strNumber = binding.numberEdt.getEditText().getText().toString();

            if (!str_username.equals("") && !str_email.equals("") && !str_password.equals("") && !str_confirmPassword.equals("") && !strNumber.equals("")) {
                if (str_username.length() < 5) {
                    Toast.makeText(this, "Name must be more than 5 character!", Toast.LENGTH_SHORT).show();
                } else if (!str_email.endsWith(".com")) {
                    Toast.makeText(this, "Email must end with .com!", Toast.LENGTH_SHORT).show();
                } else if (!str_password.matches("[A-Za-z0-9]+")) {
                    Toast.makeText(this, "Password must be alphanumeric!", Toast.LENGTH_SHORT).show();
                } else if (!str_password.endsWith(str_confirmPassword)) {
                    Toast.makeText(this, "Please enter the same password", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

                    register(str_username, str_email, str_password, strNumber);


                }
            } else {
                Toast.makeText(this, "All Field must be filled!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void register(String name, String email, String password,
                          String number) {
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                            String timestamp = String.valueOf(System.currentTimeMillis());

//                            int num = Integer.parseInt(number);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("name", name.toLowerCase());
                            hashMap.put("email", email.toLowerCase());
                            hashMap.put("password", password);
                            hashMap.put("number", number);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    try {
//                                        Toast.makeText(RegisterActivity.this, strNumber, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, OtpActivity.class).putExtra("phoneNum", number);
                                        Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(RegisterActivity.this, "Please try again!", Toast.LENGTH_SHORT).show();
                                    }

//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error or email already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void initDialog(){
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);

    }

}