package com.example.bluejackpharmacy;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bluejackpharmacy.Adaptor.ObatAdaptor;
import com.example.bluejackpharmacy.Adaptor.TransactionAdapter;
import com.example.bluejackpharmacy.Model.Obat;
import com.example.bluejackpharmacy.Model.Transaction;
import com.example.bluejackpharmacy.Model.User;
import com.example.bluejackpharmacy.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Obat> obatList;
    private List<Transaction> transactionList;
    private ObatAdaptor obatAdaptor;
    private TransactionAdapter transactionAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        obatList = new ArrayList<>();
        transactionList = new ArrayList<>();


        obatAdaptor = new ObatAdaptor(this, obatList);
        transactionAdapter = new TransactionAdapter(this, transactionList);
        checkUser();
        RadioGroup();

        binding.obatRecView.setHasFixedSize(true);
        binding.obatRecView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        reachProd();
        reachTransaction();
        binding.obatRecView.setAdapter(obatAdaptor);

        binding.aboutBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));
        binding.profileImg.setOnClickListener(view -> logoutDialog());
    }

    private void logoutDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.yes_no_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.show();
        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        TextView title = dialog.findViewById(R.id.title);
        TextView descript = dialog.findViewById(R.id.description);
        title.setText("Logout");
        descript.setText("Apakah anda ingin keluar?");

        yes.setText("Keluar");
        no.setText("Batal");

        no.setOnClickListener(v1 -> dialog.dismiss());

//        yes.setOnClickListener(v1 -> {
//            logOut(dialog);
//        });

        logOut(dialog);
    }
    private void logOut(Dialog yesNoDialog) {


        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        };

        auth.addAuthStateListener(authStateListener);

        yesNoDialog.findViewById(R.id.yes).setOnClickListener(v -> {
            yesNoDialog.dismiss();
            auth.signOut();
        });
    }

    private void RadioGroup() {

        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radioGroup.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.homeBtn:
                        binding.obatRecView.setHasFixedSize(true);
                        binding.obatRecView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        reachProd();
                        binding.obatRecView.setAdapter(obatAdaptor);
                        break;
                    case R.id.transactionBtn:
                        binding.obatRecView.setHasFixedSize(true);
                        binding.obatRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        reachTransaction();
//                        myTransaction();
                        binding.obatRecView.setAdapter(transactionAdapter);
                        break;
                    default:
                        break;
                }

            }
        });
    }
    private void reachProd(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Obat");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    obatList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Obat obat = dataSnapshot.getValue(Obat.class);
                        obatList.add(obat);
                    }

                    obatAdaptor.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void reachTransaction(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    transactionList.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Transaction transaction = dataSnapshot.getValue(Transaction.class);
//                        transactionList.add(transaction);
                        if (transaction.getUser().equals(firebaseUser.getUid())) {
                            transactionList.add(transaction);
                        }
                    }

                    transactionAdapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                binding.textView4.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
