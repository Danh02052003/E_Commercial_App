package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.R;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderItem;
import vlu.mobileproject.modle.Products;

public class OrderDetailActivity extends AppCompatActivity {
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference orderReference, orderItemReference, productItemReference;
    LinearLayoutManager layoutManager;
    RecyclerView cartRec;
    ImageView btnBack;
    TextView  totalPrice, DestinationEnd, orderId, totalTemp, otherFeesText, discountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderReference = FirebaseDatabase.getInstance().getReference(ORDER_REFERENCE_KEY);
        orderItemReference = FirebaseDatabase.getInstance().getReference(ORDER_ITEM_REFERENCE_KEY);
        productItemReference = FirebaseDatabase.getInstance().getReference("Products_2");
        btnBack = findViewById(R.id.btnBack);

        layoutManager = new LinearLayoutManager(this);
        cartRec = findViewById(R.id.RecOrder);
        orderId = findViewById(R.id.orderId);
        cartRec.setLayoutManager(new LinearLayoutManager(this));

        totalPrice = findViewById(R.id.totalTemp);
        totalTemp = findViewById(R.id.totalPrice);
        otherFeesText = findViewById(R.id.otherFeesText);
        discountText = findViewById(R.id.discountText);
        DestinationEnd = findViewById(R.id.DestinationEnd);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(v -> supportFinishAfterTransition());

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            String value = receivedBundle.getString("OrderID");
            LoadInitOrder(value);
        }
    }

    void LoadInitOrder(String newOrderID) {
        Query query = orderReference.orderByKey().equalTo(newOrderID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                if (Snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : Snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        String orderID = order.getOrder_id();
                        double totalAmount = order.getTotal_amount();
                        double otherFees = order.getOtherFees();
                        double discount = order.getDiscount();
                        double finTotalAmount = totalAmount - discount * totalAmount + otherFees;
                        totalTemp.setText("$ " + totalAmount);
                        discountText.setText(discount + " %");
                        otherFeesText.setText("$ " + otherFees);
                        totalPrice.setText("$ " + finTotalAmount);
                        DestinationEnd.setText(order.getShippingAddress());
                        orderId.setText(orderID);

                        Query orderItemQuery = orderItemReference.orderByChild("order_id").equalTo(orderID);
                        orderItemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot orderItemSnapshots) {
                                List<OrderItem> orderItemList = new ArrayList<>();
                                for (DataSnapshot orderItemSnapshot : orderItemSnapshots.getChildren()) {
                                    OrderItem orderItem = orderItemSnapshot.getValue(OrderItem.class);


                                    Query productItemQuery = productItemReference.child(orderItem.getProduct_id());
                                    productItemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Products product = snapshot.getValue(Products.class);
                                            product.setProductID(orderItem.getProduct_id());
                                            Products.MemoryOption phoneMemPackage = product.GetMemoOptPackage(orderItem.getProductMemoryOption());
                                            orderItem.setProductName(String.format("%s %s", product.getProduct_name(), phoneMemPackage.getMemory()));
                                            orderItem.setPrice_per_unit(phoneMemPackage.getProduct_price());
                                            orderItemList.add(orderItem);
                                            OrderDetailAdapter orderItemAdapter = new OrderDetailAdapter(orderItemList);
                                            cartRec.setAdapter(orderItemAdapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled if needed
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });
    }

}