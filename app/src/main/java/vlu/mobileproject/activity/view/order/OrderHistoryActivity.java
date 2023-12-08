package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.R;
import vlu.mobileproject.adapter.OrderHistoryAdapter;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.OrderItem;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";
    DatabaseReference orderReference, orderItemReference, productItemReference;
    FirebaseAuth auth;
    ArrayList<OrderHistory> orderHistories = new ArrayList<>();
    ListView recOrderHis;
    LinearLayoutManager layoutManager;
    OrderHistoryAdapter orderHistoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history2);

//        layoutManager = new LinearLayoutManager(this);
//        recOrderHis.setLayoutManager(layoutManager);
        recOrderHis = findViewById(R.id.recOrderHis);

        orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderHistories);
        orderHistoryAdapter.updateData(orderHistories);
        recOrderHis.setAdapter(orderHistoryAdapter);

        auth = FirebaseAuth.getInstance();

        LoadOrderHistory();
    }

    void LoadOrderHistory() {
        String userID = auth.getCurrentUser().getUid();
        orderReference = FirebaseDatabase.getInstance().getReference(ORDER_REFERENCE_KEY);
        orderItemReference = FirebaseDatabase.getInstance().getReference(ORDER_ITEM_REFERENCE_KEY);
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        OrderHistory orderHistory = orderSnapshot.getValue(OrderHistory.class);
                        if (orderHistory.getBuyer_id().equals(userID)) {
                            orderItemReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int count = 0;
                                    OrderItem orderItem;
                                    for (DataSnapshot orderItemSnapshot : snapshot.getChildren()) {
                                        orderItem = orderItemSnapshot.getValue(OrderItem.class);
                                        if (orderItem.getOrder_id().equals(orderHistory.getOrder_id())) {
                                            count++;
                                        }
                                    }
                                    orderHistory.setOrderItemCount(count);
                                    orderHistories.add(orderHistory);
                                    orderHistoryAdapter.updateData(orderHistories);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}