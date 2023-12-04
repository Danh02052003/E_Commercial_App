package vlu.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Arrays;
import java.util.List;

import vlu.mobileproject.activity.view.cart.Cart;
import vlu.mobileproject.activity.view.home.MainActivity;
import vlu.mobileproject.login.UserManager;
import vlu.mobileproject.modle.FavoriteFirebase;

import vlu.mobileproject.modle.Products;

import vlu.mobileproject.translate.UpdateLang;
import vlu.mobileproject.translate.language;

public class ProductDetailsActivity extends AppCompatActivity {
//    static String bet = "https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/";

    Button btnDetails_addToCart, btnDetails_buyNow;

    ImageButton btnFavorite, ibtnDetails_remove_1, ibtnDetails_add_1, btnBack;

    RelativeLayout btnDetails_wAddToCart, btnDetails_wBuyNow;
    RelativeLayout rlPopupWindow;
    CardView cvPopupWindow_display;
    GridView gvCapacities;
    ArrayAdapter<String> capacitiesAdapter;
    Products product;
    TextView tvDetails_productName, tvDetails_productPrice, tvDetails_productDescr, tvDetailsAddProduct_productPrice, tvDetails_quantity, tvDetails_quantity_2, tvDetails_nProductLeft, tvDetails_expand;
    ImageView ivDetails_productIllustration, ivDetailsAddProduct_productImg, ibtnDetails_remove_2, ibtnDetails_add_2;
    double productPriceBasedCapacity;

    int productQuantityAdded = 1;
    int productQuantity;
    boolean isFavoritePresent;

    boolean shouldCreateNewCartItem = false;
    private final String userEmail = UserManager.getInstance().getUserEmail();

    List<TextView> listPrice;

    DatabaseReference cartReference, productRef;

    FirebaseAuth auth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        addControl();
        addEvent();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        product = (Products) bundle.getSerializable("object_product");
        if (product != null) {

            tvDetails_productName.setText(product.getProduct_name());
            tvDetails_productPrice.setText("$" + String.valueOf(product.getPriceForMemory()));
            tvDetails_productDescr.setText("Ngày sản xuất: " + product.getProduct_createdDate() + "\n" + product.getProduct_description());

            Glide.with(this).load(product.getProduct_img()).into(ivDetails_productIllustration);
            Glide.with(this).load(product.getProduct_img()).into(ivDetailsAddProduct_productImg);

            tvDetailsAddProduct_productPrice.setText("$" + String.valueOf(product.getPriceForMemory()));


//            tvDetails_productName.setText(product.getProduct_name());
//            tvDetails_productPrice.setText("$" + product.getProductPrice());
//            tvDetails_productDescr.setText("Ngày sản xuất: " + product.getCreatedDate() + "\n" + product.getProductDescription());
//            ivDetails_productIllustration.setImageResource(product.getProductImg());
//            ivDetailsAddProduct_productImg.setImageResource(product.getProductImg());
//            tvDetailsAddProduct_productPrice.setText("$" + String.valueOf(product.getProductPrice()));
//            tvDetails_nProductLeft.setText("Còn " + product.getQuantity() + " sản phẩm.");

            isFavoritePresent = FavoriteProduct.lstProduct.contains(product);
            if (isFavoritePresent) btnFavorite.setImageResource(R.drawable.red_heart_icon);

            capacitiesAdapter = new ArrayAdapter<>(this, R.layout.capacity_item, R.id.tvCapacity, product.getMemory());
            gvCapacities.setAdapter(capacitiesAdapter);
            String[] memoryOptions = product.getMemory();

            cartReference = FirebaseDatabase.getInstance().getReference("Cart");
            productRef = FirebaseDatabase.getInstance().getReference("Products");
            auth = FirebaseAuth.getInstance();

            gvCapacities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (gvCapacities.isItemChecked(position)) {
                        for (int i = 0; i < gvCapacities.getChildCount(); i++) {
                            if (i == position) {
                                continue;
                            }
                            TextView textView = gvCapacities.getChildAt(i).findViewById(R.id.tvCapacity);
                            textView.setTextColor(Color.BLACK);
                            textView.setBackgroundColor(Color.WHITE);
                            gvCapacities.setItemChecked(i, false);
                            tvDetailsAddProduct_productPrice.setText(String.valueOf(product.getPriceForMemory(memoryOptions[position])));
                            productQuantity = product.getQuantityForMemory(memoryOptions[position]);
                            productQuantityAdded = 1;
                            SetTextForQuantity();
                            if(productQuantity<=10)
                                tvDetails_nProductLeft.setText("Chỉ còn " + productQuantity + " sản phẩm.");
                            else {
                                tvDetails_nProductLeft.setText("Còn nhiều sản phẩm.");
                            }

                        }
                        view = gvCapacities.getChildAt(position);
                        TextView textView = view.findViewById(R.id.tvCapacity);
                        textView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                        textView.setTextColor(Color.WHITE);
                    } else {

                    }
                    capacitiesAdapter.notifyDataSetChanged();
                }
            });
        }

        SetTextForQuantity();
        String lang = language.getPresentLang();
        UpdateLang.exchangeCurrency(listPrice.toArray(new TextView[0]), this);


    }

    private void addControl() {
        btnDetails_addToCart = findViewById(R.id.btnDetails_addToCart);
        btnDetails_buyNow = findViewById(R.id.btnDetails_buyNow);
        btnDetails_wAddToCart = findViewById(R.id.btnDetails_wAddToCart);
        btnDetails_wBuyNow = findViewById(R.id.btnDetails_wBuyNow);
        rlPopupWindow = findViewById(R.id.rlPopupWindow);
        cvPopupWindow_display = findViewById(R.id.cvPopupWindow_display);
        tvDetails_productName = findViewById(R.id.tvDetails_productName);
        tvDetails_productPrice = findViewById(R.id.tvDetails_productPrice);
        tvDetails_productDescr = findViewById(R.id.tvDetails_productDescr);
        ivDetails_productIllustration = findViewById(R.id.ivDetails_productIllustration);
        ivDetailsAddProduct_productImg = findViewById(R.id.ivDetailsAddProduct_productImg);
        tvDetailsAddProduct_productPrice = findViewById(R.id.tvDetailsAddProduct_productPrice);
        ibtnDetails_remove_1 = findViewById(R.id.ibtnDetails_remove_1);
        ibtnDetails_remove_2 = findViewById(R.id.ibtnDetails_remove_2);
        ibtnDetails_add_1 = findViewById(R.id.ibtnDetails_add_1);
        ibtnDetails_add_2 = findViewById(R.id.ibtnDetails_add_2);
        tvDetails_nProductLeft = findViewById(R.id.tvDetails_nProductLeft);
        tvDetails_expand = findViewById(R.id.tvDetails_expand);
        btnBack = findViewById(R.id.btnBack);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        tvDetails_quantity = findViewById(R.id.tvDetails_quantity);
        tvDetails_quantity_2 = findViewById(R.id.tvDetails_quantity_2);

        btnFavorite = findViewById(R.id.btnFavorite);
        gvCapacities = findViewById(R.id.gvCapacities);

        listPrice = Arrays.asList(tvDetails_productPrice);

    }

    @SuppressLint("SetTextI18n")
    private void addEvent() {
        btnDetails_addToCart.setOnClickListener(view -> {
            rlPopupWindow.setVisibility(View.VISIBLE);
            btnDetails_wAddToCart.setVisibility(View.VISIBLE);
        });

        btnDetails_buyNow.setOnClickListener(view -> {
            rlPopupWindow.setVisibility(View.VISIBLE);
            btnDetails_wBuyNow.setVisibility(View.VISIBLE);
        });


        rlPopupWindow.setOnClickListener(view -> {
            Rect cardViewRect = new Rect();
            cvPopupWindow_display.getHitRect(cardViewRect);
            if (!cardViewRect.contains((int) view.getX(), (int) view.getY())) {
                rlPopupWindow.setVisibility(View.INVISIBLE);
                btnDetails_wAddToCart.setVisibility(View.INVISIBLE);
                btnDetails_wBuyNow.setVisibility(View.INVISIBLE);
            }
        });
        cvPopupWindow_display.setOnClickListener(view -> {

        });

        btnDetails_wBuyNow.setOnClickListener(view -> {
            Intent intentt = new Intent(ProductDetailsActivity.this, Cart.class);

            Bundle bundleSender = new Bundle();
            bundleSender.putSerializable("object_product", product);
            intentt.putExtras(bundleSender);
            //ShoppingCart itemPutInCart = new ShoppingCart(product, productQuantityAdded, product.getPriceForMemory() + productPriceBasedCapacity);
//           ShoppingCart.lstProduct.add(product);
//           ShoppingCart.lstQuantity.add(productQuantityAdded);
            startActivity(intentt);

        });

        btnDetails_wAddToCart.setOnClickListener(view -> {

            cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ShoppingCart CartItemProduct = dataSnapshot.getValue(ShoppingCart.class);
                        String productID = CartItemProduct.getProductID();

                        shouldCreateNewCartItem = productID.equals(product.getProductID());
                        if (shouldCreateNewCartItem) {
                            int newQuantity = CartItemProduct.getQuantity() + productQuantityAdded;
                            dataSnapshot.getRef().child("quantity").setValue(newQuantity);
                            break;
                        }
                    }
                    if (!shouldCreateNewCartItem) {
                        ShoppingCart itemPutInCart = new ShoppingCart(product.getProductID(), productQuantityAdded, product.getPriceForMemory() + productPriceBasedCapacity);
                        SaveCardItem(itemPutInCart);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            rlPopupWindow.setVisibility(View.INVISIBLE);
            btnDetails_wAddToCart.setVisibility(View.INVISIBLE);
        });

        btnFavorite.setOnClickListener(view -> {
            if (isFavoritePresent) {
                FavoriteProduct.lstProduct.remove(product);
                btnFavorite.setImageResource(R.drawable.grey_heart_icon);
                isFavoritePresent = false;
            } else {
                FavoriteProduct.lstProduct.add(product);
                btnFavorite.setImageResource(R.drawable.red_heart_icon);
                isFavoritePresent = true;
                saveDataToDatabase(userEmail, product.getProduct_id());
            }

        });

        ibtnDetails_remove_1.setOnClickListener(view -> {
            if (productQuantityAdded != 0) {
                productQuantityAdded--;
            }
            SetTextForQuantity();
        });

        ibtnDetails_remove_2.setOnClickListener(view -> {
            if (productQuantityAdded != 0) {
                productQuantityAdded--;
            }
            SetTextForQuantity();
        });

        ibtnDetails_add_1.setOnClickListener(view -> {
            productQuantityAdded++;

//            if (productQuantityAdded < product.getQuantity()){
//            }
            SetTextForQuantity();
        });

        ibtnDetails_add_2.setOnClickListener(view -> {
            if (productQuantityAdded < productQuantity){
                productQuantityAdded++;
            }
            SetTextForQuantity();
        });

        tvDetails_expand.setOnClickListener(view -> {
            if (tvDetails_expand.getText().equals("Xem thêm >")) {
                tvDetails_productDescr.setMaxLines(10);
                tvDetails_expand.setText("Thu gọn <");
            } else {
                tvDetails_productDescr.setMaxLines(2);
                tvDetails_expand.setText("Xem thêm >");
            }


        });

        btnBack.setOnClickListener(view -> {
            finish();
        });
    }

    private void SaveCardItem(ShoppingCart CartItem) {
        String productId = cartReference.push().getKey();
        CartItem.setUserID(auth.getCurrentUser().getUid());
        cartReference.child(productId).setValue(CartItem);

        Toast.makeText(this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    private void SetTextForQuantity() {
        tvDetails_quantity.setText(String.valueOf(productQuantityAdded));
        tvDetails_quantity_2.setText(String.valueOf(productQuantityAdded));
    }

    private void saveDataToDatabase(String user_email, long product_id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(MainActivity.bet);
        DatabaseReference usersRef = database.getReference("favorite");
        FavoriteFirebase ff = new FavoriteFirebase(user_email, product_id);
        Log.d("ProductDetailsActivity", "User Email: " + userEmail); // Add this line to check the value in Logcat

        // Use push() method to generate a unique key for each entry
        DatabaseReference newFavoriteRef = usersRef.push();

        newFavoriteRef.setValue(ff).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Added to Favorites successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to add to Favorites.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}