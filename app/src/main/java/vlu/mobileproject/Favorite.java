package vlu.mobileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.adapter.HomeChildAdapter;

public class Favorite extends AppCompatActivity {
    RecyclerView rvForExpand;

    List<HomeChildItem> childItemList;
    HomeChildAdapter adapter;

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        rvForExpand = findViewById(R.id.rvForFavorite);
        rvForExpand.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new HomeChildAdapter(this, FavoriteProduct.lstProduct);
        rvForExpand.setAdapter(adapter);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            finish();
        });

    }

    @Override
    protected void onRestart() {
        adapter.setItems(FavoriteProduct.lstProduct);
        super.onRestart();
    }
}