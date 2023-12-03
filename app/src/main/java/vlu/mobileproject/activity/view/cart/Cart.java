package vlu.mobileproject.activity.view.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vlu.mobileproject.ProductInCartItem;
import vlu.mobileproject.R;
import vlu.mobileproject.ShoppingCart;
import vlu.mobileproject.adapter.ProductInCartAdapter;
import vlu.mobileproject.modle.Products;

public class Cart extends AppCompatActivity implements ProductInCartAdapter.OnCheckedChangeListener {
    RecyclerView rvProductAdded;
    List<ProductInCartItem> inCartItemList = new ArrayList<>();
    ProductInCartAdapter adapter;
    Products product;
    CheckBox cbCartCheck;
    TextView tvCart_state, tvCart_totalPrice, tvCart_discount;
    Button btnPay, btnApplyDiscount;
    ImageButton btnBack;
    EditText edtDiscount;
    List<ProductInCartItem> inCartSelectedList = new ArrayList<>();
    double totalPrice = 0;

    String formattedValue;

    DatabaseReference cartReference;

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

        btnBack.setOnClickListener(view -> {
            finish();
        });
        cartReference = FirebaseDatabase.getInstance().getReference("Cart");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        GetListFromShoppingCart();
        if (inCartItemList.size() != 0) {

            rvProductAdded = findViewById(R.id.rvProductAdded);
            rvProductAdded.setLayoutManager(new LinearLayoutManager(this));

            adapter = new ProductInCartAdapter(inCartItemList);
            adapter.setOnCheckedChangeListener(this);
            rvProductAdded.setAdapter(adapter);
            rvProductAdded.setVisibility(View.VISIBLE);

            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item,null);

            cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);

            PayControl();
            addControl();
        }
        else {
            tvCart_state = findViewById(R.id.tvCart_state);
            tvCart_state.setVisibility(View.VISIBLE);
        }
    }


    void GetListFromShoppingCart() {
        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ShoppingCart cartItem = dataSnapshot.getValue(ShoppingCart.class);
                    String productId = cartItem.getProductID();

                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
                    productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
                            if (productSnapshot.exists()) {
                                Products productDetails = productSnapshot.getValue(Products.class);
                                int quantity = cartItem.getQuantity();
                                double totalPrice = quantity * cartItem.getPrice();

                                inCartItemList.add(new ProductInCartItem(productDetails.getProduct_name(), cartItem.price, cartItem.quantity, productDetails.getProduct_img()));

                                // Now you have the total price, you can use it as needed
                                Log.d("TotalPrice", String.valueOf(totalPrice));
                                LoadCartItem2View();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    // Break out of the loop since the product is found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
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

            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item,null);

            cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);

            PayControl();
            addControl();
        }
        else {
            tvCart_state = findViewById(R.id.tvCart_state);
            tvCart_state.setVisibility(View.VISIBLE);
        }
    }

    private List<ProductInCartItem> getListProductAdded(){
        GetListFromShoppingCart();
        if (inCartItemList.size() != 0){
            ProductInCartItem lastProduct = new ProductInCartItem(product.getProduct_name(), ShoppingCart.lstProduct.get(ShoppingCart.lstProduct.size()-1).price, ShoppingCart.lstProduct.get(ShoppingCart.lstProduct.size()-1).quantity, product.getProduct_img());
            inCartItemList.remove(inCartItemList.size()-1);
            inCartItemList.add(0, lastProduct);

            ShoppingCart last_product = ShoppingCart.lstProduct.remove(ShoppingCart.lstProduct.size() -1);
            ShoppingCart.lstProduct.add(0, last_product);

//            int lastQuantity = ShoppingCart.lstQuantity.remove(ShoppingCart.lstQuantity.size() - 1);
//            ShoppingCart.lstQuantity.add(0, lastQuantity);

            SetIdForProduct();
            return inCartItemList;
        }
        else {
            inCartItemList.add(new ProductInCartItem(product.getProduct_name(), product.getPriceForMemory(), 1, product.getProduct_img()));
            inCartItemList.get(0).setInCartId(1);
            return inCartItemList;
        }
    }

    private void SetIdForProduct(){
        int i = 1;
        for (ProductInCartItem x : inCartItemList) {
            x.setInCartId(i);
            i++;
        }
    }

    @Override
    public void onItemCheckedChanged(int position, boolean isChecked) {
        ProductInCartItem SelectedProduct = inCartItemList.get(position);
        double totalPriceCheckedChanged = 0;
        if (isChecked) {
            SelectedProduct.setChecked(true);
            inCartSelectedList.add(SelectedProduct);
        }
        else {
            SelectedProduct.setChecked(false);
            inCartSelectedList.remove(SelectedProduct);
        }

        for (ProductInCartItem x : inCartSelectedList) {
            if (x.isChecked()){
                totalPriceCheckedChanged += x.getProductPrice() * x.getProductQuantity();
                totalPrice = totalPriceCheckedChanged;
            }
        }
        if (inCartSelectedList.size() == 0) {
            totalPrice = 0;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        formattedValue = decimalFormat.format(totalPrice);
        formattedValue = decimalFormat.format(totalPrice);
        tvCart_totalPrice.setText("$" + formattedValue);
    }

    private void PayControl(){
        btnPay.setOnClickListener(view -> {
            Set<ProductInCartItem> setInCart = new HashSet<>(inCartItemList);
            Set<ProductInCartItem> setInCartSelected = new HashSet<>(inCartSelectedList);

            setInCart.removeAll(setInCartSelected);
            inCartItemList = new ArrayList<>(setInCart);


            adapter = new ProductInCartAdapter(inCartItemList);
            adapter.setOnCheckedChangeListener(this);
            rvProductAdded.setAdapter(adapter);

            tvCart_totalPrice.setText("$00");
            totalPrice = 0;

        });

    }
     private void addControl(){
         btnApplyDiscount.setOnClickListener(view -> {
             if(edtDiscount.getText().toString().equals("VLUS")){
                 tvCart_discount.setText(String.valueOf(totalPrice * 0.25));
                 tvCart_totalPrice.setText(String.valueOf(totalPrice * 0.75));

             }

         });


     }



}