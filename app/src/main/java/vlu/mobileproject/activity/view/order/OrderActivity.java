package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import vlu.mobileproject.data.DeliveryMethod;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderItem;
import vlu.mobileproject.modle.Products;

public class OrderActivity extends AppCompatActivity {
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference orderReference, orderItemReference, productItemReference;

    LinearLayoutManager layoutManager;

    RecyclerView cardTitle;

    TextView orderStatus, orderTime, orderItemCount, paymentMethod, totalPrice, DestinationEnd, btnDetail2, deliveryMethod, progressDetail;
    ProgressBar pendingBar, inprogressBar, Delivering2YouBar;
    ImageView btnBack;
    Button btnDetail;
    private int progressStatus = 0;
    private ValueAnimator progressAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        orderReference = FirebaseDatabase.getInstance().getReference(ORDER_REFERENCE_KEY);
        orderItemReference = FirebaseDatabase.getInstance().getReference(ORDER_ITEM_REFERENCE_KEY);
        productItemReference = FirebaseDatabase.getInstance().getReference("Products_2");

        pendingBar = findViewById(R.id.pendingBar);
        inprogressBar = findViewById(R.id.inprogressBer);
        Delivering2YouBar = findViewById(R.id.Delivering2You);
        btnBack = findViewById(R.id.btnBack);
        btnDetail = findViewById(R.id.btnDetail);
        btnDetail2 = findViewById(R.id.btnDetail2);
        DestinationEnd = findViewById(R.id.DestinationEnd);
        deliveryMethod = findViewById(R.id.deliveryMethod);
        progressDetail = findViewById(R.id.progressDetail);

        orderStatus = findViewById(R.id.orderStatus);
        orderTime = findViewById(R.id.orderTime);
        orderItemCount = findViewById(R.id.orderItemCount);
        paymentMethod = findViewById(R.id.paymentMethod);
        totalPrice = findViewById(R.id.totalPrice);

        layoutManager = new LinearLayoutManager(this);
        cardTitle = findViewById(R.id.RecOrder);
        cardTitle.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(v -> supportFinishAfterTransition());

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            String value = receivedBundle.getString("OrderID");
            LoadInitOrder(value);
        }
    }

    void SetupDetailBtn(String orderID) {
        btnDetail.setOnClickListener(v -> {
            OpenOrderDetail(orderID);
        });
        btnDetail2.setOnClickListener(v -> {
            OpenOrderDetail(orderID);
        });
    }

    void OpenOrderDetail(String orderID) {
        Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("OrderID", orderID);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    void LoadInitOrder(String newOrderID) {
        Query query = orderReference.orderByKey().equalTo(newOrderID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot Snapshot) {
                if (Snapshot.exists()) {
                    for (DataSnapshot orderSnapshot : Snapshot.getChildren()) {
                        Order order = orderSnapshot.getValue(Order.class);
                        SetupInfo2View(order);
                        String orderID = order.getOrder_id();

                        SetupDetailBtn(orderID);

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
                                            orderItemList.add(orderItem);
                                            orderItemCount.setText(orderItemList.size() + " kiện hàng.");
                                            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(OrderActivity.this, orderItemList);
                                            cardTitle.setAdapter(orderItemAdapter);
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

    private void showPopupDialog(int targetGif) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imageView = dialog.findViewById(R.id.gifImageView);
        Glide.with(this).asGif().load(targetGif).into(imageView);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 2000);

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);
    }


    void SetupInfo2View(Order order) {
        String DE_Method = (order.getPaymentMethod().getPaymentMethod());
        if (DE_Method.equals(PaymentMethod.COD.getPaymentMethod())) {
            paymentMethod.setText(R.string.cash_on_delivery);
        } else if (DE_Method.equals(PaymentMethod.BANKING.getPaymentMethod())) {
            paymentMethod.setText(R.string.banking);
        } else if (DE_Method.equals(PaymentMethod.CREDIT_CARD.getPaymentMethod())) {
            paymentMethod.setText(R.string.credit_card);
        }
        String DM_Method = (order.getDeliveryMethod().getdeliveryMethod());
        if (DM_Method.equals(DeliveryMethod.NORMAL)) {
            deliveryMethod.setText(R.string.normal_speed);
        } else if (DM_Method.equals(DeliveryMethod.LOW_COST)) {
            deliveryMethod.setText(R.string.low_cost);
        } else if (DM_Method.equals(DeliveryMethod.HIGH_SPEED)) {
            deliveryMethod.setText(R.string.high_speed);
        }
        if (order.getStatus().equals(DeliveryStatus.PENDING)) {
            OnDeliveryStatusChange(R.string.onPending, R.string.Pending, R.color.greenVLUS);
        } else if (order.getStatus().equals(DeliveryStatus.PENDING)) {
            OnDeliveryStatusChange(R.string.onInProgress, R.string.InProgress, R.color.greenVLUS);
        } else if (order.getStatus().equals(DeliveryStatus.DELIVERING_TO_YOU)) {
            OnDeliveryStatusChange(R.string.onDelivering2You, R.string.Delivering2You, R.color.greenVLUS);
        } else if (order.getStatus().equals(DeliveryStatus.DELIVERED)) {
            OnDeliveryStatusChange(R.string.onDelivered, R.string.Delivered, R.color.greenVLUS);
        } else if (order.getStatus().equals(DeliveryStatus.CANCELED)) {
            OnDeliveryStatusChange(R.string.canceled, R.string.canceled, R.color.red);
        }
        orderTime.setText(order.getOrder_date());

        double totalAmount = order.getTotal_amount();
        double otherFees = order.getOtherFees();
        double discount = order.getDiscount();
        double finTotalAmount = totalAmount - discount * totalAmount + otherFees;
        totalPrice.setText("$ " + finTotalAmount);
        DestinationEnd.setText(order.getShippingAddress());
        ProgressBarAnimation(order);
        String orderID = order.getOrder_id();
        SetupDetailBtn(orderID);
    }

    void OnDeliveryStatusChange(int progressDetailString, int orderStatusString, int textColor) {
        progressDetail.setText(progressDetailString);
        orderStatus.setText(orderStatusString);
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, textColor);
        orderStatus.setTextColor(colorStateList);
    }

    void ProgressBarAnimation(Order order) {
        if (order.getStatus().getStatus().equals(DeliveryStatus.PENDING.getStatus())) {
            OnDeliveryStatusChange(0, 0, 0, R.drawable.pending, R.color.greenVLUS, true, pendingBar);
        } else if (order.getStatus().getStatus().equals(DeliveryStatus.IN_PROGRESS.getStatus())) {
            OnDeliveryStatusChange(100, 0, 0, R.drawable.delivering, R.color.greenVLUS, true, inprogressBar);
        } else if (order.getStatus().getStatus().equals(DeliveryStatus.DELIVERING_TO_YOU.getStatus())) {
            OnDeliveryStatusChange(100, 100, 0, R.drawable.deliveringtoyou, R.color.greenVLUS, true, Delivering2YouBar);
        } else if (order.getStatus().getStatus().equals(DeliveryStatus.DELIVERED.getStatus())) {
            OnDeliveryStatusChange(100, 100, 100, R.drawable.deliverycompleted, R.color.blue, false, Delivering2YouBar);
        } else {
            OnDeliveryStatusChange(100, 100, 100, R.drawable.order_canceled, R.color.red, false, Delivering2YouBar);
        }
    }

    void OnDeliveryStatusChange(int pendingBarProgress, int inprogressBarProgress, int Delivering2YouBarProgress, int targetGif, int progressBarColor, boolean shouldPlayAnim, ProgressBar progresBar2Play) {
        stopSmoothAnimation();
        pendingBar.setProgress(pendingBarProgress);
        inprogressBar.setProgress(inprogressBarProgress);
        Delivering2YouBar.setProgress(Delivering2YouBarProgress);
        if (shouldPlayAnim) {
            startSmoothAnimation(progresBar2Play);
        }
        showPopupDialog(targetGif);
        SetProgressBarsColor(progressBarColor);
    }

    void SetProgressBarsColor(int color) {
        ColorStateList colorStateList = ContextCompat.getColorStateList(this, color);
        pendingBar.setProgressTintList(colorStateList);
        Delivering2YouBar.setProgressTintList(colorStateList);
        inprogressBar.setProgressTintList(colorStateList);
    }

    private void startSmoothAnimation(ProgressBar progressBar) {
        // Stop any existing animation before starting a new one
        stopSmoothAnimation();

        // Create a ValueAnimator that animates the progress from 0 to 100
        progressAnimator = ValueAnimator.ofInt(0, 100);
        progressAnimator.setDuration(1000); // Animation duration in milliseconds
        progressAnimator.setRepeatCount(ValueAnimator.INFINITE); // Infinite repeat

        // Update the progress of the ProgressBar during the animation
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int progress = (int) valueAnimator.getAnimatedValue();
                progressBar.setProgress(progress);
            }
        });

        // Start the animation
        progressAnimator.start();
    }

    private void stopSmoothAnimation() {
        // Stop the animation if it's running to avoid memory leaks
        if (progressAnimator != null && progressAnimator.isRunning()) {
            progressAnimator.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the animation when the activity is being destroyed
        stopSmoothAnimation();
    }
}