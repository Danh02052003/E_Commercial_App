package vlu.mobileproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.adapter.HomeChildAdapter;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.Products;

public class Favorite extends AppCompatActivity implements GlobalData.Callback {
    RecyclerView rvForExpand;

    HomeChildAdapter adapter;

    ImageButton btnBack;
    List<Products> products = new ArrayList<>();
    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        GlobalData.initData(this, this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            finish();
        });

    }

    @Override
    protected void onRestart() {
        GlobalData.initData(this, this);
        super.onRestart();
    }

    @Override
    public void onCompleted(List<Products> products) {
        List<String> products_key = new ArrayList<>();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user_id = auth.getCurrentUser().getUid();
        DatabaseReference favoriteRef = FirebaseDatabase.getInstance().getReference("Favorite_2").child(user_id);
        favoriteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    products_key.add(dataSnapshot.getKey());
                Favorite.this.products.clear();
                for(Products product : products)
                    if(products_key.contains(product.getProductID()))
                        Favorite.this.products.add(product);
                rvForExpand = findViewById(R.id.rvForFavorite);
                rvForExpand.setLayoutManager(new GridLayoutManager(mContext, 2));

                adapter = new HomeChildAdapter(getApplicationContext(), Favorite.this.products);
                rvForExpand.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}