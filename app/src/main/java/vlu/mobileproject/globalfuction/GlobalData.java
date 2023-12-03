package vlu.mobileproject.globalfuction;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.AllFrag;
import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.HomeParentItem;
import vlu.mobileproject.R;
import vlu.mobileproject.adapter.HomeParentAdapter;
import vlu.mobileproject.modle.Product;
import vlu.mobileproject.modle.Products;

public class GlobalData {
    public interface Callback{
        void onCompleted(List<Products> foryou_list, List<Products> highlight_list);

    }
    public static List<Products> forYou_list;
    public static List<Products> highlight_list;
    static List<Products> products = new ArrayList<>();

    public static void initData(Context context, Callback callback) {

        forYou_list = new ArrayList<>();
        highlight_list = new ArrayList<>();

        FirebaseApp.initializeApp(context);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference productRef = database.getReference("Products_2");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products product = dataSnapshot.getValue(Products.class);
                    product.setProductID(dataSnapshot.getKey());
                    Log.d("Product debug: ", "Product " + String.valueOf(product.getProduct_id()));
                    i++;
                    String imageCategory_path = product.getProduct_categoryId() == 1 ? "samsung" : "iphone";
//                    String imgUrl = dataSnapshot.child("product_img").getValue().toString();
                    product.setProduct_img("product_image/"+imageCategory_path+"/"+product.getProduct_img());
                    products.add(product);
                }
                separateData();
                callback.onCompleted(forYou_list, highlight_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    static void separateData() {
        for (int i = 0; i < products.size(); i++) {
            if (i < products.size() / 2)
                forYou_list.add(products.get(i));
            else highlight_list.add(products.get(i));
        }
    };




}
