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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.modle.Products;

public class GlobalData {
    public interface Callback{
        void onCompleted(List<Products> products);

    }
    public final static List<Products> products = new ArrayList<>();
    public static boolean isDataLoaded = false;

    public static void initData(Context context, Callback callBack) {
        if(isDataLoaded == false) {
            isDataLoaded = true;
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
                        products.add(product);
                    }
                    callBack.onCompleted(products);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else callBack.onCompleted(products);

    }

}
