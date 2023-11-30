package com.example.bluejackpharmacy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bluejackpharmacy.Model.Obat;
import com.example.bluejackpharmacy.databinding.ActivityObatDescBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ObatDescActivity extends AppCompatActivity {

    ActivityObatDescBinding binding;
    String obatId;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityObatDescBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        obatId = (String) getIntent().getStringExtra("obatId");

        readProd();
        binding.backBtn.setOnClickListener(v -> finish());
        binding.addToCartBtn.setOnClickListener(view -> {

            if (!binding.valueEdt.getText().toString().equals("")){
                if (!binding.valueEdt.getText().toString().equals("0")){
                    String date = String.valueOf(System.currentTimeMillis());
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart");
                    String id =  reference.push().getKey();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id", id);
                    hashMap.put("obatId", obatId);
                    hashMap.put("user", firebaseUser.getUid());
                    hashMap.put("date", date);
                    hashMap.put("quantity", Integer.parseInt(binding.valueEdt.getText().toString()));

                    if (reference.child("obatId").toString().equals(obatId)){
                        return;
                    }else {
                        reference.child(id).setValue(hashMap);
                        Toast.makeText(this, binding.valueEdt.getText().toString()+" item berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Quantity must be more than 0!", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Quantity must be filled!", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void readProd() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Obat").child(obatId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Obat obat = snapshot.getValue(Obat.class);
                Glide.with(getApplicationContext()).load(obat.getImage()).into(binding.obatImg);
                binding.obatName.setText(obat.getName());
                binding.obatManufacture.setText("Manufacture: "+obat.getManufacture());
                binding.obatPrice.setText("Rp. "+ obat.getPrice());
                binding.obatDesc.setText(obat.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//
    }
}