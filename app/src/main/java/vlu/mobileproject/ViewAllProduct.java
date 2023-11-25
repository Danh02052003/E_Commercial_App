package vlu.mobileproject;

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
import vlu.mobileproject.modle.Products;

public class ViewAllProduct extends AppCompatActivity {
    RecyclerView rvForExpand;

    List<Products> childItemList;
    HomeChildAdapter adapter;

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_product);

        rvForExpand = findViewById(R.id.rvForExpand);
        rvForExpand.setLayoutManager(new GridLayoutManager(this, 2));

        Intent intent = getIntent();
        childItemList = (List<Products>) intent.getSerializableExtra("dataList");

        adapter = new HomeChildAdapter(this, childItemList);
        rvForExpand.setAdapter(adapter);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> {
            finish();
        });

    }
}