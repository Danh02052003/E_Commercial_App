package vlu.mobileproject.activity.view.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.paperdb.Paper;
import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.ProductInCartItem;
import vlu.mobileproject.R;
import vlu.mobileproject.ShoppingCart;
import vlu.mobileproject.activity.view.order.OrderActivity;
import vlu.mobileproject.activity.view.order.PaymentActivity;
import vlu.mobileproject.adapter.ProductInCartAdapter;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;
import vlu.mobileproject.modle.Discount;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderItem;
import vlu.mobileproject.modle.Products;

public class Cart extends AppCompatActivity implements ProductInCartAdapter.OnCheckedChangeListener {
    private static final String CART_REFERENCE_KEY = "Cart";
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";
    private static final String PRODUCTS_REFERENCE_KEY = "Products_2";
    private static final String DISCOUNT_REFERENCE_KEY = "Discount";

    DeliveryStatus deliveryStatus;
    RecyclerView rvProductAdded;
    List<ProductInCartItem> inCartItemList = new ArrayList<>();
    ProductInCartAdapter adapter;
    CheckBox cbCartCheck;
    TextView tvCart_state, tvCart_totalPrice, tvCart_discount;
    Button btnPay, btnApplyDiscount;
    ImageButton btnBack;
    EditText edtDiscount;
    List<ProductInCartItem> inCartSelectedList = new ArrayList<>();
    double totalPrice = 0;
    double discountPercent = 0;
    String formattedValue;
    DatabaseReference cartReference, orderReference, orderItemReference, discountReference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvCart_totalPrice = findViewById(R.id.tvCart_totalPrice);
        btnPay = findViewById(R.id.btnPay);
        btnApplyDiscount = findViewById(R.id.btnApplyDiscount);
        edtDiscount = findViewById(R.id.edtDiscount);
        tvCart_discount = findViewById(R.id.tvCart_discount);
        btnBack = findViewById(R.id.btnBack);

        Paper.init(this);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(view -> {
            finish();
        });

        cartReference = FirebaseDatabase.getInstance().getReference(CART_REFERENCE_KEY);
        orderReference = FirebaseDatabase.getInstance().getReference(ORDER_REFERENCE_KEY);
        orderItemReference = FirebaseDatabase.getInstance().getReference(ORDER_ITEM_REFERENCE_KEY);
        discountReference = FirebaseDatabase.getInstance().getReference(DISCOUNT_REFERENCE_KEY);
        GetListFromShoppingCart();

        btnApplyDiscount.setOnClickListener(view -> {
            applyDiscount(edtDiscount.getText().toString());
        });
    }

    private void setupUI() {
        rvProductAdded = findViewById(R.id.rvProductAdded);
        rvProductAdded.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductInCartAdapter(inCartItemList);
        adapter.setOnCheckedChangeListener(this);
        rvProductAdded.setAdapter(adapter);
        rvProductAdded.setVisibility(View.VISIBLE);
    }

    void GetListFromShoppingCart() {
        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    setupUI();
                    View cartItemView = getLayoutInflater().inflate(R.layout.cart_item, null);
                    cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);
                    PayControl();
                } else {
                    tvCart_state = findViewById(R.id.tvCart_state);
                    tvCart_state.setVisibility(View.VISIBLE);
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ShoppingCart cartItem = dataSnapshot.getValue(ShoppingCart.class);
                    String productId = cartItem.getProductID();

                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference(PRODUCTS_REFERENCE_KEY);
                    productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                            if (productSnapshot.exists()) {
                                Products productDetails = productSnapshot.getValue(Products.class);
                                productDetails.setProductID(productSnapshot.getKey());
                                Products.MemoryOption MemoOptPackage = productDetails.GetMemoOptPackage(cartItem.getMemoryOptID());

                                inCartItemList.add(new ProductInCartItem(dataSnapshot.getKey(), productDetails.getProduct_name(), MemoOptPackage.getProduct_price(), cartItem.quantity, productDetails.getProduct_img(), productDetails.getProductID(), cartItem.getMemoryOptID()));
                                LoadCartItem2View();
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

    void LoadCartItem2View() {
        if (inCartItemList.size() != 0) {

            rvProductAdded = findViewById(R.id.rvProductAdded);
            rvProductAdded.setLayoutManager(new LinearLayoutManager(this));

            adapter = new ProductInCartAdapter(inCartItemList);
            adapter.setOnCheckedChangeListener(this);
            adapter.notifyDataSetChanged();
            rvProductAdded.setAdapter(adapter);
            rvProductAdded.setVisibility(View.VISIBLE);

            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item, null);

            cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);

            PayControl();
        } else {
            tvCart_state = findViewById(R.id.tvCart_state);
            tvCart_state.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemCheckedChanged(int position, boolean isChecked) {
        ProductInCartItem SelectedProduct = inCartItemList.get(position);
        double totalPriceCheckedChanged = 0;
        if (isChecked) {
            SelectedProduct.setChecked(true);
            inCartSelectedList.add(SelectedProduct);
        } else {
            SelectedProduct.setChecked(false);
            inCartSelectedList.remove(SelectedProduct);
        }

        for (ProductInCartItem x : inCartSelectedList) {
            if (x.isChecked()) {
                totalPriceCheckedChanged += x.getProductPrice() * x.getProductQuantity();
                totalPrice = totalPriceCheckedChanged;
            }
        }
        if (inCartSelectedList.size() == 0) {
            totalPrice = 0;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        formattedValue = decimalFormat.format(totalPrice - totalPrice * (discountPercent == 0 ? 1 : (discountPercent / 100)));
        tvCart_totalPrice.setText("$" + (formattedValue));
    }

    private void PayControl() {
        btnPay.setOnClickListener(view -> {
            Set<ProductInCartItem> setInCart = new HashSet<>(inCartItemList);
            Set<ProductInCartItem> setInCartSelected = new HashSet<>(inCartSelectedList);

            for (int i = 0; i < inCartSelectedList.size(); i++) {
                cartReference.child(inCartSelectedList.get(i).getCartItemID()).removeValue();
            }

            Intent intent = new Intent(Cart.this, PaymentActivity.class);
            Paper.book().write("inCartSelectedList", inCartSelectedList);
            Paper.book().write("totalPrice", totalPrice);
            startActivity(intent);

            setInCart.removeAll(setInCartSelected);
            inCartItemList = new ArrayList<>(setInCart);
            setInCartSelected.removeAll(setInCartSelected);
            inCartSelectedList = new ArrayList<>(setInCartSelected);

            adapter = new ProductInCartAdapter(inCartItemList);
            adapter.setOnCheckedChangeListener(this);
            rvProductAdded.setAdapter(adapter);

            tvCart_totalPrice.setText("$00");
            totalPrice = 0;
        });
    }

    private void applyDiscount(String discountCode) {
        discountReference.child(discountCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Discount discount = snapshot.getValue(Discount.class);
                    discountPercent = discount.getDiscountValue() * 100;
                    tvCart_discount.setText(String.valueOf(discountPercent) + " %");
                    tvCart_totalPrice.setText("$ " + String.valueOf(tvCart_totalPrice.getText().length() == 0 ? 0 : discountPercent * Double.valueOf(tvCart_totalPrice.getText().toString().replaceAll("\\$", ""))));
                    Paper.book().write("discount", discount.getDiscountValue());
                } else {
                    Toast.makeText(Cart.this, "Không thấy mã giảm giá", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cart.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}