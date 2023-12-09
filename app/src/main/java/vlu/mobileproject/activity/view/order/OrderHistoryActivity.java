package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.OrderItem;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";
    DatabaseReference orderReference, orderItemReference, productItemReference;
    FirebaseAuth auth;
    ArrayList<OrderHistory> orderHistories = new ArrayList<>();
    RecyclerView recOrderHis;
    LinearLayoutManager layoutManager;
    OrderHistoryAdapter orderHistoryAdapter;
    TextView textAllOrders, textDelivering;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history2);

        recOrderHis = findViewById(R.id.recOrderHis);
        layoutManager = new LinearLayoutManager(this);
        recOrderHis.setLayoutManager(layoutManager);
        textAllOrders = findViewById(R.id.textAllOrders);
        textDelivering = findViewById(R.id.textDelivering);

        auth = FirebaseAuth.getInstance();

        LoadOrderHistory();

        textAllOrders.setOnClickListener(v -> {
            LoadOrderHistory();
        });
        textDelivering.setOnClickListener(v -> {
            ArrayList<OrderHistory> orderHistories2Show = new ArrayList<>();
            LoadOrderHistory();
            for (OrderHistory orderHistory : orderHistories) {
                if (orderHistory.getStatus().equals(DeliveryStatus.CANCELED)) {
                    orderHistories2Show.add(orderHistory);
                }
            }
            orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderHistories2Show);
            recOrderHis.setAdapter(orderHistoryAdapter);
            orderHistoryAdapter.updateData(orderHistories2Show);
        });
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
                                    orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderHistories);
                                    recOrderHis.setAdapter(orderHistoryAdapter);

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