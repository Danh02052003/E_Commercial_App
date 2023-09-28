package vlu.mobileproject.activity.view.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.ProductInCartItem;
import vlu.mobileproject.R;
import vlu.mobileproject.ShoppingCart;
import vlu.mobileproject.adapter.ProductInCartAdapter;

public class Cart extends AppCompatActivity implements ProductInCartAdapter.OnCheckedChangeListener {
    RecyclerView rvProductAdded;
    List<ProductInCartItem> inCartItemList = new ArrayList<>();
    ProductInCartAdapter adapter;
    HomeChildItem product;
    CheckBox cbCartCheck;
    TextView tvCart_state, tvCart_totalPrice, tvCart_discount;
    Button btnPay, btnApplyDiscount;
    ImageButton btnBack;
    EditText edtDiscount;
    List<ProductInCartItem> inCartSelectedList = new ArrayList<>();
    double totalPrice = 0;

    String formattedValue;

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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            if (bundle.containsKey("object_product")) {
                product = (HomeChildItem) bundle.getSerializable("object_product");


                rvProductAdded = findViewById(R.id.rvProductAdded);
                rvProductAdded.setLayoutManager(new LinearLayoutManager(this));

                inCartItemList = getListProductAdded();
                adapter = new ProductInCartAdapter(inCartItemList);
                adapter.setOnCheckedChangeListener(this);
                rvProductAdded.setAdapter(adapter);
                rvProductAdded.setVisibility(View.VISIBLE);

                View cartItemView = getLayoutInflater().inflate(R.layout.cart_item,null);

                cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);

                PayControl();
                addControl();

            } else {
                tvCart_state = findViewById(R.id.tvCart_state);
                tvCart_state.setVisibility(View.VISIBLE);

            }
        }
        else {
            if(ShoppingCart.lstProduct.size() == 0){
                tvCart_state = findViewById(R.id.tvCart_state);
                tvCart_state.setVisibility(View.VISIBLE);
            }
            else {
                rvProductAdded = findViewById(R.id.rvProductAdded);
                rvProductAdded.setLayoutManager(new LinearLayoutManager(this));

                inCartItemList = getListFromShoppingCartOBJ(ShoppingCart.lstProduct);
                adapter = new ProductInCartAdapter(inCartItemList);
                adapter.setOnCheckedChangeListener(this);
                rvProductAdded.setAdapter(adapter);
                rvProductAdded.setVisibility(View.VISIBLE);

                View cartItemView = getLayoutInflater().inflate(R.layout.cart_item,null);
                cbCartCheck = cartItemView.findViewById(R.id.cbCartCheck);
                cbCartCheck.setChecked(true);

//                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//                rvProductAdded.addItemDecoration(itemDecoration);

                PayControl();
                addControl();
            }

        }



    }

    private List<ProductInCartItem> getListProductAdded(){
        inCartItemList = getListFromShoppingCartOBJ(ShoppingCart.lstProduct);
        if (inCartItemList.size() != 0){
            ProductInCartItem lastProduct = new ProductInCartItem(product.getProductName(), ShoppingCart.lstProduct.get(ShoppingCart.lstProduct.size()-1).price, ShoppingCart.lstProduct.get(ShoppingCart.lstProduct.size()-1).quantity, product.getProductImg());
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
            inCartItemList.add(new ProductInCartItem(product.getProductName(), product.getProductPrice(), product.getQuantity(), product.getProductImg()));
            inCartItemList.get(0).setInCartId(1);
            return inCartItemList;
        }

    }

    private List<ProductInCartItem> getListFromShoppingCartOBJ(List<ShoppingCart> lst){
        for (ShoppingCart productInCart : lst) {
            inCartItemList.add(new ProductInCartItem(productInCart.item.getProductName(), productInCart.price, productInCart.quantity, productInCart.item.getProductImg()));
        }
        SetIdForProduct();
        return inCartItemList;


//        for ( HomeChildItem product : lst) {
//            inCartItemList.add(new ProductInCartItem(product.getProductName(), product.getProductPrice(), product.getQuantity(), product.getProductImg()));
//
//        }

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
        ProductInCartItem product1 = inCartItemList.get(position);
        double totalPriceCheckedChanged = 0;
        if (isChecked) {
            product1.setChecked(true);
            inCartSelectedList.add(product1);
        }
        else {
            product1.setChecked(false);
            inCartSelectedList.remove(product1);
        }

        for (ProductInCartItem x : inCartSelectedList) {
            if (x.isChecked()){
                totalPriceCheckedChanged += x.getProductPrice() * x.getProductQuantity();
                totalPrice = totalPriceCheckedChanged;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                formattedValue = decimalFormat.format(totalPrice);

            }

        }
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