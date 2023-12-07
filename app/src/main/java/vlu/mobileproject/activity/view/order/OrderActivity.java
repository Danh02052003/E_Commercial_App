package vlu.mobileproject.activity.view.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
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
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderItem;
import vlu.mobileproject.modle.Product;
import vlu.mobileproject.modle.Products;

public class OrderActivity extends AppCompatActivity {
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;
    DatabaseReference orderReference, orderItemReference, productItemReference;

    LinearLayoutManager layoutManager;

    RecyclerView cardTitle;

    TextView orderStatus, orderTime, orderItemCount, paymentMethod, totalPrice;
    ProgressBar pendingBar, inprogressBar, Delivering2YouBar;
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

        orderStatus = findViewById(R.id.orderStatus);
        orderTime = findViewById(R.id.orderTime);
        orderItemCount = findViewById(R.id.orderItemCount);
        paymentMethod = findViewById(R.id.paymentMethod);
        totalPrice = findViewById(R.id.totalPrice);

        layoutManager = new LinearLayoutManager(this);
        cardTitle = findViewById(R.id.RecOrder);
        cardTitle.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            String value = receivedBundle.getString("OrderID");
            LoadInitOrder(value);
        }

        // Run a sample animation (you can adjust the duration and steps as needed)
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
                        orderStatus.setText(order.getStatus().getStatus());
                        orderTime.setText(order.getOrder_date());
                        paymentMethod.setText(order.getPaymentMethod().getPaymentMethod());
                        totalPrice.setText("$ " + String.valueOf(order.getTotal_amount()));

                        ProgressBarAnimation(order);

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
                                            Product product = snapshot.getValue(Product.class);
                                            orderItem.setProductName( product.getProduct_name());

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

    void ProgressBarAnimation(Order order) {
        if (order.getStatus().getStatus().equals(DeliveryStatus.PENDING.getStatus())) {
            inprogressBar.setProgress(0);
            Delivering2YouBar.setProgress(0);
            startSmoothAnimation(pendingBar);
        } else if (order.getStatus().getStatus().equals(DeliveryStatus.IN_PROGRESS.getStatus())) {
            startSmoothAnimation(inprogressBar);
            pendingBar.setProgress(100);
            Delivering2YouBar.setProgress(0);
        } else if (order.getStatus().getStatus().equals(DeliveryStatus.DELIVERING_TO_YOU.getStatus())) {
            pendingBar.setProgress(100);
            inprogressBar.setProgress(100);
            startSmoothAnimation(Delivering2YouBar);
        }
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