package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vlu.mobileproject.R;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderItem;

public class OrderActivity extends AppCompatActivity {
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference orderReference, orderItemReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderReference = FirebaseDatabase.getInstance().getReference(ORDER_REFERENCE_KEY);
        orderItemReference = FirebaseDatabase.getInstance().getReference(ORDER_ITEM_REFERENCE_KEY);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            String value = receivedBundle.getString("OrderID");
            LoadInitOrder(value);
        }
    }

    void LoadInitOrder(String newOrderID) {
        Query query = orderReference.child(newOrderID);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Order order = snapshot.getValue(Order.class);
                    String orderID = order.getOrder_id();

                    Query orderItemQuery = orderItemReference.child(orderID);
                    orderItemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot orderItemSnapshot) {
                            if (orderItemSnapshot.exists()) {
                                OrderItem orderItem = orderItemSnapshot.
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}