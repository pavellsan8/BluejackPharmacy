package com.example.bluejackpharmacy.Adaptor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bluejackpharmacy.Model.Obat;
import com.example.bluejackpharmacy.Model.Transaction;
import com.example.bluejackpharmacy.Model.User;
import com.example.bluejackpharmacy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private Context context;
    private List<Transaction> transactionList;
    public TransactionAdapter(Context context, List<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    private FirebaseUser firebaseUser;

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Transaction transaction = transactionList.get(position);
        holder.quantity.setText(transaction.getQuantity()+ "");
        long yourmilliseconds = Long.parseLong(transaction.getDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ");
        Date resultdate = new Date(yourmilliseconds);
        holder.date.setText(sdf.format(resultdate));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Obat").child(transaction.getObatId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Obat obat = snapshot.getValue(Obat.class);

                holder.name.setText(obat.getName());
                holder.manufacture.setText(obat.getManufacture());
                holder.price.setText("Rp. "+obat.getPrice());
                Glide.with(context).load(obat.getImage()).into(holder.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.updateBtn.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.update_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            TextInputLayout q = dialog.findViewById(R.id.quantityEdt);
            dialog.findViewById(R.id.no).setOnClickListener(view1 -> dialog.dismiss());
//            dialog.findViewById(R.id.yes).setOnClickListener(view1 -> {
//                Map<String, Object> map = new HashMap<>();
//                int qInt = Integer.parseInt(q.getEditText().getText().toString());
////                Toast.makeText(context, qInt+"", Toast.LENGTH_SHORT).show();
//                map.put("quantity", qInt);
//                FirebaseDatabase.getInstance().getReference("Cart").child(transaction.getId()).updateChildren(map);
//                dialog.dismiss();
//            });
            dialog.findViewById(R.id.yes).setOnClickListener(view1 -> {
                if (!q.getEditText().getText().toString().equals("")){

                    int qInt = Integer.parseInt(q.getEditText().getText().toString());
                    if (qInt<1){
                        Toast.makeText(context, "Quantity must be more than 0!", Toast.LENGTH_SHORT).show();
                    }else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("quantity", qInt);
                        FirebaseDatabase.getInstance().getReference("Cart").child(transaction.getId()).updateChildren(map);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(context, "Quantity must be filled!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.deleteBtn.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference("Cart").child(transaction.getId()).removeValue();
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout main;
        public ImageView image;
        public TextView name, manufacture, price, quantity, date;
        public Button updateBtn, deleteBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.obatImg);
            name = itemView.findViewById(R.id.obatName);
            manufacture = itemView.findViewById(R.id.obatManufacture);
            price = itemView.findViewById(R.id.obatPrice);
            quantity = itemView.findViewById(R.id.quantityTxt);
            date = itemView.findViewById(R.id.dateTxt);
            updateBtn = itemView.findViewById(R.id.updateBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
