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
//                        if(products.size() == snapshot.getChildrenCount())
//                            callBack.onCompleted(products);
//                        if(product.getProduct_categoryId() == 2){
//                            String imageCategory_path = product.getProduct_categoryId() == 1 ? "samsung" : "iphone";
//                            product.setProduct_img("product_image/"+imageCategory_path+"/"+product.getProduct_img());
//                            StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(product.getProduct_img());
//                            imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                                product.setProduct_img(uri.toString());
//                                dataSnapshot.getRef().child("product_img").setValue(uri.toString());
//                                products.add(product);
//                                if(products.size() == snapshot.getChildrenCount())
//                                    callBack.onCompleted(products);
//
//                            }).addOnFailureListener(e -> {
//                                Log.e(this.toString(), "Error loading image from Firebase: " + e.getMessage());
//                                e.printStackTrace();
//                            });
//                        }
//                        else {
//
//                        }
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
