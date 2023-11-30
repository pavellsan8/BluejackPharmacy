package com.example.bluejackpharmacy.Adaptor;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bluejackpharmacy.Model.Obat;
import com.example.bluejackpharmacy.ObatDescActivity;
import com.example.bluejackpharmacy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Vector;

public class ObatAdaptor extends RecyclerView.Adapter<ObatAdaptor.ViewHolder> {

    private Context context;
    private List<Obat> obats;

    private FirebaseUser firebaseUser;

    public ObatAdaptor(Context context, List<Obat> obats) {
        this.context = context;
        this.obats = obats;
    }

    @NonNull
    @Override
    public ObatAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.obat_item, parent, false);
        return new ObatAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObatAdaptor.ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Obat obat = obats.get(position);
        holder.name.setText(obat.getName());
        holder.manufacture.setText(obat.getManufacture());
        holder.price.setText("Rp. " + obat.getPrice());


//        Glide.with(context.getApplicationContext()).load(obat.getImage()).into(holder.image);
        Glide.with(context).load(obat.getImage()).into(holder.image);

        holder.main.setOnClickListener(view -> {
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View,String>(holder.image, "obatImg");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
            context.startActivity(new Intent(context, ObatDescActivity.class).putExtra("obatId", obat.getId()), options.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return obats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout main;
        public ImageView image;
        public TextView name, manufacture, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.mainObat);
            image = itemView.findViewById(R.id.obatImg);
            name = itemView.findViewById(R.id.obatName);
            manufacture = itemView.findViewById(R.id.obatManufacture);
            price = itemView.findViewById(R.id.obatPrice);
        }
    }
}
