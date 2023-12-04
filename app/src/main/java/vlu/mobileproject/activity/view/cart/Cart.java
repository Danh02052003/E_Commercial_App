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
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.Products;

//public class Cart extends AppCompatActivity implements ProductInCartAdapter.OnCheckedChangeListener {
public class Cart extends AppCompatActivity implements GlobalData.AllCallBack {
    RecyclerView rvProductAdded;
    List<Products> inCartItemList = new ArrayList<>();
    ProductInCartAdapter adapter;
    Products product;
    CheckBox cbCartCheck;
    TextView tvCart_state, tvCart_totalPrice, tvCart_discount;
    Button btnPay, btnApplyDiscount;
    ImageButton btnBack;
    EditText edtDiscount;
    List<ProductInCartItem> inCartSelectedList = new ArrayList<>();
    List<Products> products = new ArrayList<>();
    List<Integer> quantityList = new ArrayList<>();
    List<String> memoryList = new ArrayList<>();
    double totalPrice = 0;

    String formattedValue;

    DatabaseReference cartReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        GlobalData.initData(this, this);

        rvProductAdded = findViewById(R.id.rvProductAdded);
        rvProductAdded.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ProductInCartAdapter(inCartItemList, quantityList, memoryList, getApplicationContext());
        rvProductAdded.setAdapter(adapter);

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
        getCart();

//        GetListFromShoppingCart();
//        if (inCartItemList.size() != 0) {
//
//            rvProductAdded = findViewById(R.id.rvProductAdded);
//            rvProductAdded.setLayoutManager(new LinearLayoutManager(this));
//
//            adapter = new ProductInCartAdapter(inCartItemList);
////            adapter.setOnCheckedChangeListener(this);
//            rvProductAdded.setAdapter(adapter);
//            rvProductAdded.setVisibility(View.VISIBLE);
//
//            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item,null);
//
//            cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);
//
////            PayControl();
//            addControl();
//        }
//        else {
//            tvCart_state = findViewById(R.id.tvCart_state);
//            tvCart_state.setVisibility(View.VISIBLE);
//        }
    }


//    void GetListFromShoppingCart() {
//        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ShoppingCart cartItem = dataSnapshot.getValue(ShoppingCart.class);
//                    String productId = cartItem.getProductID();
//
//                    DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
//                    productsRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot productSnapshot) {
//                            if (productSnapshot.exists()) {
//                                Products productDetails = productSnapshot.getValue(Products.class);
//                                int quantity = cartItem.getQuantity();
//                                double totalPrice = quantity * cartItem.getPrice();
//
//                                inCartItemList.add(new ProductInCartItem(productDetails.getProduct_name(), cartItem.price, cartItem.quantity, productDetails.getProduct_img()));
//
//                                Log.d("TotalPrice", String.valueOf(totalPrice));
//                                LoadCartItem2View();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle errors
//            }
//        });
//    }

//    void LoadCartItem2View() {
//        if (inCartItemList.size() != 0) {
//
//            rvProductAdded = findViewById(R.id.rvProductAdded);
//            rvProductAdded.setLayoutManager(new LinearLayoutManager(this));
//
//            adapter = new ProductInCartAdapter(inCartItemList);
//            adapter.setOnCheckedChangeListener(this);
//            adapter.notifyDataSetChanged();
//            rvProductAdded.setAdapter(adapter);
//            rvProductAdded.setVisibility(View.VISIBLE);
//
//            View cartItemView = getLayoutInflater().inflate(R.layout.cart_item,null);
//
//            cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);
//
//            PayControl();
//            addControl();
//        }
//        else {
//            tvCart_state = findViewById(R.id.tvCart_state);
//            tvCart_state.setVisibility(View.VISIBLE);
//        }
//    }

//    private List<Products> getListProductAdded(){
//        GetListFromShoppingCart();
//        if (inCartItemList.size() != 0){
//            ProductInCartItem lastProduct = new ProductInCartItem(product.getProduct_name(), ShoppingCart.lstProduct.get(ShoppingCart.lstProduct.size()-1).price, ShoppingCart.lstProduct.get(ShoppingCart.lstProduct.size()-1).quantity, product.getProduct_img());
//            inCartItemList.remove(inCartItemList.size()-1);
//            inCartItemList.add(0, lastProduct);
//
//            ShoppingCart last_product = ShoppingCart.lstProduct.remove(ShoppingCart.lstProduct.size() -1);
//            ShoppingCart.lstProduct.add(0, last_product);
//
////            int lastQuantity = ShoppingCart.lstQuantity.remove(ShoppingCart.lstQuantity.size() - 1);
////            ShoppingCart.lstQuantity.add(0, lastQuantity);
//
//            SetIdForProduct();
//            return inCartItemList;
//        }
//        else {
//            inCartItemList.add(new ProductInCartItem(product.getProduct_name(), product.getPriceForMemory(), 1, product.getProduct_img()));
//            inCartItemList.get(0).setInCartId(1);
//            return inCartItemList;
//        }
//    }

//    private void SetIdForProduct(){
//        int i = 1;
//        for (ProductInCartItem x : inCartItemList) {
//            x.setInCartId(i);
//            i++;
//        }
//    }

//    @Override
//    public void onItemCheckedChanged(int position, boolean isChecked) {
//        ProductInCartItem SelectedProduct = inCartItemList.get(position);
//        double totalPriceCheckedChanged = 0;
//        if (isChecked) {
//            SelectedProduct.setChecked(true);
//            inCartSelectedList.add(SelectedProduct);
//        }
//        else {
//            SelectedProduct.setChecked(false);
//            inCartSelectedList.remove(SelectedProduct);
//        }
//
//        for (ProductInCartItem x : inCartSelectedList) {
//            if (x.isChecked()){
//                totalPriceCheckedChanged += x.getProductPrice() * x.getProductQuantity();
//                totalPrice = totalPriceCheckedChanged;
//            }
//        }
//        if (inCartSelectedList.size() == 0) {
//            totalPrice = 0;
//        }
//
//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        formattedValue = decimalFormat.format(totalPrice);
//        formattedValue = decimalFormat.format(totalPrice);
//        tvCart_totalPrice.setText("$" + formattedValue);
//    }

//    private void PayControl(){
//        btnPay.setOnClickListener(view -> {
//            Set<ProductInCartItem> setInCart = new HashSet<>(inCartItemList);
//            Set<ProductInCartItem> setInCartSelected = new HashSet<>(inCartSelectedList);
//
//            setInCart.removeAll(setInCartSelected);
//            inCartItemList = new ArrayList<>(setInCart);
//
//
//            adapter = new ProductInCartAdapter(inCartItemList);
//            adapter.setOnCheckedChangeListener(this);
//            rvProductAdded.setAdapter(adapter);
//
//            tvCart_totalPrice.setText("$00");
//            totalPrice = 0;
//
//        });
//
//    }
     private void addControl(){
         btnApplyDiscount.setOnClickListener(view -> {
             if(edtDiscount.getText().toString().equals("VLUS")){
                 tvCart_discount.setText(String.valueOf(totalPrice * 0.25));
                 tvCart_totalPrice.setText(String.valueOf(totalPrice * 0.75));

             }

         });


     }
     void getCart(){

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart_2");
         cartRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 int i = 0;
                 for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                     CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                     Log.d("Cart debug: ", "Cart " + String.valueOf(cartItem.product_id));
                     FindProductInCart(cartItem.product_id, cartItem.quantity_added, cartItem.memory);
                 }
                 Log.d("Cart debug: ", "Cart " + String.valueOf(inCartItemList.size()));
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         adapter.notifyDataSetChanged();
                         Log.d("Adapter debug: ", "Cart " + String.valueOf(adapter.getItemCount()));
                     }
                 });
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
     }


    @Override
    public void onCompleted(List<Products> products) {
        this.products = products;

    }
    void FindProductInCart(int id, int quantity, String memory) {
        for (Products productOBJ : products) {
            if (productOBJ.getProduct_id() == id) {
                inCartItemList.add(productOBJ);
                quantityList.add(quantity);
                memoryList.add(memory);
                break;
            }
        }

    }

    public static class CartItem {
        String memory;
        int quantity_added;
        int product_id;

        public CartItem() {}
        public CartItem(String memory, int quantity_added, int product_id) {
            this.memory = memory;
            this.quantity_added = quantity_added;
            this.product_id = product_id;
        }

        public String getMemory() {
            return memory;
        }

        public void setMemory(String memory) {
            this.memory = memory;
        }

        public int getQuantity_added() {
            return quantity_added;
        }

        public void setQuantity_added(int quantity_added) {
            this.quantity_added = quantity_added;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }


    }
}