package vlu.mobileproject.globalfuction;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedManager {
    String product_id;
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    static String user_id = auth.getCurrentUser().getUid();
    public static DatabaseReference recentlyViewedRef = FirebaseDatabase.getInstance().getReference("RecentlyViewed").child(user_id);
    public RecentlyViewedManager(){
    }

    public static void AddRecentlyViewed(String product_id){
        long time = System.currentTimeMillis();
        recentlyViewedRef.child(product_id).setValue(time);
        recentlyViewedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 5){
                    removeOldestProduct();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void GetRecentlyViewedProducts(ValueEventListener listener) {
        recentlyViewedRef.orderByValue().limitToLast(5).addValueEventListener(listener);
    }

    private static void removeOldestProduct() {
        recentlyViewedRef.orderByValue().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            childSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
