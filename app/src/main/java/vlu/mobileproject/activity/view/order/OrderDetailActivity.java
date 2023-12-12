package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vlu.mobileproject.R;
import vlu.mobileproject.data.DeliveryStatus;
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
    TextView finalTotalPrice, DestinationEnd, orderId, totalTemp, otherFeesText, discountText, progressInfo;
    Map<String, DeliveryStatus> DeliveryStatusMap = new HashMap<>();

    Button cancelOrder;
    String progressInformation;

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

        finalTotalPrice = findViewById(R.id.finalTotalPrice);
        totalTemp = findViewById(R.id.totalTemp);
        otherFeesText = findViewById(R.id.otherFeesText);
        discountText = findViewById(R.id.discountText);
        DestinationEnd = findViewById(R.id.DestinationEnd);
        progressInfo = findViewById(R.id.progressInfo);
        cancelOrder = findViewById(R.id.cancelOrder);

        DeliveryStatusMap.put("Pending", DeliveryStatus.PENDING);
        DeliveryStatusMap.put("In Progress", DeliveryStatus.IN_PROGRESS);
        DeliveryStatusMap.put("Delivering to you", DeliveryStatus.DELIVERING_TO_YOU);
        DeliveryStatusMap.put("Delivered", DeliveryStatus.DELIVERED);
        DeliveryStatusMap.put("Canceled", DeliveryStatus.CANCELED);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(v -> supportFinishAfterTransition());

        Bundle receivedBundle = getIntent().getExtras();
        String OrderID = receivedBundle.containsKey("OrderID") ? receivedBundle.getString("OrderID") : "";
        LoadInitOrder(OrderID);

        cancelOrder.setOnClickListener(v -> {
            showConfirmationDialog(OrderID);
        });
    }

    public void showConfirmationDialog(String OrderID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmation").setMessage("Do you want to proceed?").setPositiveButton("Yes", (dialog, which) -> {
            OnTry2Cancel(OrderID);
        }).setNegativeButton("No", (dialog, which) -> Toast.makeText(OrderDetailActivity.this, "Không hủy nữa", Toast.LENGTH_SHORT).show()).show();
    }

    void OnTry2Cancel(String OrderID) {
        Query query = orderReference.child(OrderID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order.getStatus().equals(DeliveryStatus.PENDING)) {
                        Toast.makeText(OrderDetailActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                        orderReference.child(OrderID).child("status").setValue(DeliveryStatus.CANCELED);
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Không thể hủy được nữa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that may occur
            }
        });
    }

    void SetupCancelBtn(boolean isEnable) {
        int color = isEnable ? R.color.greenVLUS : R.color.greyIcon;
        cancelOrder.setEnabled(isEnable);
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, color);
        cancelOrder.setBackgroundTintList(colorStateList);
    }

    void OnDeliveryProgress(String deliveryMethod) {
        if (deliveryMethod.equals(DeliveryStatus.PENDING.getStatus())) {
            progressInfo.setText(getString(R.string.onPending));
        } else if (deliveryMethod.equals(DeliveryStatus.IN_PROGRESS.getStatus())) {
            progressInfo.setText(getString(R.string.onInProgress));
        } else if (deliveryMethod.equals(DeliveryStatus.DELIVERING_TO_YOU.getStatus())) {
            progressInfo.setText(getString(R.string.onDelivering2You));
        } else if (deliveryMethod.equals(DeliveryStatus.DELIVERED.getStatus())) {
            progressInfo.setText(getString(R.string.onDelivered));
        } else if (deliveryMethod.equals(DeliveryStatus.CANCELED.getStatus())) {
            progressInfo.setText(getString(R.string.onCancel));
            ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.red);
            progressInfo.setTextColor(colorStateList);
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
                        finalTotalPrice.setText("$ " + finTotalAmount);
                        DestinationEnd.setText(order.getShippingAddress());
                        orderId.setText(orderID);

                        progressInformation = order.getStatus().getStatus();
                        if (progressInformation.equals(DeliveryStatus.PENDING.getStatus())) {
                            SetupCancelBtn(true);
                        } else {
                            SetupCancelBtn(false);
                        }
                        OnDeliveryProgress(progressInformation);

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
                                            Products.MemoryOption phoneMemPackage = product.getProductOptPackage(orderItem.getProductMemoryOptKey());
                                            orderItem.setProductName(String.format("%s %s", product.getProduct_name(), phoneMemPackage.getMemory()));
                                            orderItem.setPrice_per_unit(phoneMemPackage.getProduct_price());
                                            orderItemList.add(orderItem);
                                            OrderDetailAdapter orderItemAdapter = new OrderDetailAdapter(OrderDetailActivity.this, orderItemList);
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