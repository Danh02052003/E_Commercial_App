package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
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
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.FirebaseReferenceKey;
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
    TextView textAllOrders, textDelivering, textDelivered, textCanceled, textPending, textInprogress;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history2);

        recOrderHis = findViewById(R.id.recOrderHis);
        layoutManager = new LinearLayoutManager(this);
        recOrderHis.setLayoutManager(layoutManager);
        textAllOrders = findViewById(R.id.textAllOrders);
        textDelivering = findViewById(R.id.textDelivering);
        textDelivered = findViewById(R.id.textDelivered);
        textCanceled = findViewById(R.id.textCanceled);
        textPending = findViewById(R.id.textPending);
        textInprogress = findViewById(R.id.textInprogress);
        btnBack = findViewById(R.id.btnBack);

        auth = FirebaseAuth.getInstance();

        LoadOrderHistory(DeliveryStatus.ALL);

        btnBack.setOnClickListener(v -> finish());

        textAllOrders.setOnClickListener(v -> {
            LoadOrderHistory(DeliveryStatus.ALL);
        });
        textPending.setOnClickListener(v -> {
            LoadOrderHistory(DeliveryStatus.PENDING);
        });
        textDelivering.setOnClickListener(v -> {
            LoadOrderHistory(DeliveryStatus.DELIVERING_TO_YOU);
        });
        textInprogress.setOnClickListener(v -> {
            LoadOrderHistory(DeliveryStatus.IN_PROGRESS);
        });
        textDelivered.setOnClickListener(v -> {
            LoadOrderHistory(DeliveryStatus.DELIVERED);
        });
        textCanceled.setOnClickListener(v -> {
            LoadOrderHistory(DeliveryStatus.CANCELED);
        });
    }

    void LoadOrderHistory(DeliveryStatus deliveryStatus) {
        orderHistories.clear();
        String userID = auth.getCurrentUser().getUid();
        orderReference = FirebaseDatabase.getInstance().getReference(FirebaseReferenceKey.ORDER.getReferenceKey());
        orderItemReference = FirebaseDatabase.getInstance().getReference(FirebaseReferenceKey.ORDER_ITEM.getReferenceKey());
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
                                    List<OrderItem> orderItemList = new ArrayList<>();
                                    for (DataSnapshot orderItemSnapshot : snapshot.getChildren()) {
                                        OrderItem orderItem = orderItemSnapshot.getValue(OrderItem.class);
                                        if (orderItem.getOrder_id().equals(orderHistory.getOrder_id())) {
                                            orderItemList.add(orderItem);
                                        }
                                    }
                                    orderHistory.setOrderItemList(orderItemList);
                                    if (DeliveryStatus.ALL.equals(deliveryStatus)) {
                                        orderHistories.add(orderHistory);
                                    } else {
                                        if (orderHistory.getStatus().equals(deliveryStatus)) {
                                            orderHistories.add(orderHistory);
                                        }
                                    }
                                    orderHistoryAdapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderHistories);
                                    recOrderHis.setAdapter(orderHistoryAdapter);

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