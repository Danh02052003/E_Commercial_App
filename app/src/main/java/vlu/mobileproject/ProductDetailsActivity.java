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
import android.view.animation.Animation;
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

    ImageButton btnFavorite_empty, btnFavorite_full, ibtnDetails_remove_1, ibtnDetails_add_1, btnBack;

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

    boolean isFavoritePresent;

    boolean shouldAddQuantty = false;
    private final String userEmail = UserManager.getInstance().getUserEmail();

    List<TextView> listPrice;

    DatabaseReference cartReference, productRef;

    FirebaseAuth auth;

    String productSelectedOption = "";
    double selectedOptionPrice = 0.0;
    int selectedOptionQuantity = 0;

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

            capacitiesAdapter = new ArrayAdapter<>(this, R.layout.capacity_item, R.id.tvCapacity, product.getMemory());
            gvCapacities.setAdapter(capacitiesAdapter);
            String[] memoryOptions = product.getMemory();

            tvDetailsAddProduct_productPrice.setText("$" + String.valueOf(product.getPriceForMemory()));
            tvDetails_nProductLeft.setText("Tổng " + product.getTotalQuantity() + " sản phẩm.");

            cartReference = FirebaseDatabase.getInstance().getReference("Cart");
            productRef = FirebaseDatabase.getInstance().getReference("Products");
            auth = FirebaseAuth.getInstance();

            gvCapacities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if (gvCapacities.isItemChecked(position)) {
                        reset_gvCapacitiesBtns();
                        productSelectedOption = product.getMemoryOptionNames()[position];
                        selectedOptionPrice = product.getPriceForMemory(memoryOptions[position]);
                        tvDetailsAddProduct_productPrice.setText(String.valueOf(product.getPriceForMemory(memoryOptions[position])));
                        selectedOptionQuantity = product.getQuantityForMemory(memoryOptions[position]);
                        productQuantityAdded = 1;
                        SetTextForQuantity();

                        tvDetails_nProductLeft.setText(selectedOptionQuantity + " sản phẩm.");

                        btnDetails_wAddToCart.setEnabled(true);
                        btnDetails_wAddToCart.setBackgroundResource(R.color.greenVLUS);
                        btnDetails_wBuyNow.setEnabled(true);
                        btnDetails_wBuyNow.setBackgroundResource(R.color.greenVLUS);

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
//        tvDetails_productName.setTransitionName("product_name");
        tvDetails_productPrice = findViewById(R.id.tvDetails_productPrice);
//        tvDetails_productPrice.setTransitionName("product_price");
        tvDetails_productDescr = findViewById(R.id.tvDetails_productDescr);
        ivDetails_productIllustration = findViewById(R.id.ivDetails_productIllustration);
        ivDetails_productIllustration.setTransitionName("product_img");
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

        btnFavorite_empty = findViewById(R.id.btnFavorite_empty);
        btnFavorite_empty.setBackground(null);
        btnFavorite_full = findViewById(R.id.btnFavorite_full);
        btnFavorite_full.setBackground(null);
        gvCapacities = findViewById(R.id.gvCapacities);

        listPrice = Arrays.asList(tvDetails_productPrice);

    }

    @SuppressLint("SetTextI18n")
    private void addEvent() {
        btnDetails_addToCart.setOnClickListener(view -> {
            rlPopupWindow.setVisibility(View.VISIBLE);
            btnDetails_wAddToCart.setEnabled(false);
            btnDetails_wAddToCart.setBackgroundResource(R.color.greyIcon);
            btnDetails_wAddToCart.setVisibility(View.VISIBLE);
            reset_gvCapacitiesBtns();
        });

        btnDetails_buyNow.setOnClickListener(view -> {
            rlPopupWindow.setVisibility(View.VISIBLE);
            btnDetails_wBuyNow.setEnabled(false);
            btnDetails_wBuyNow.setBackgroundResource(R.color.greyIcon);
            btnDetails_wBuyNow.setVisibility(View.VISIBLE);
            reset_gvCapacitiesBtns();
        });


        rlPopupWindow.setOnClickListener(view -> {
            Rect cardViewRect = new Rect();
            cvPopupWindow_display.getHitRect(cardViewRect);
            if (!cardViewRect.contains((int) view.getX(), (int) view.getY())) {
                rlPopupWindow.setVisibility(View.INVISIBLE);
                btnDetails_wAddToCart.setVisibility(View.INVISIBLE);
                btnDetails_wBuyNow.setVisibility(View.INVISIBLE);

                view = gvCapacities;
                TextView textView = view.findViewById(R.id.tvCapacity);
                textView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.greyVLUS));
                textView.setTextColor(Color.BLACK);
            }
        });
        cvPopupWindow_display.setOnClickListener(view -> {

        });

        btnDetails_wBuyNow.setOnClickListener(view -> {
            AddProductItem2Cart(true);

        });

        btnDetails_wAddToCart.setOnClickListener(view -> {
            AddProductItem2Cart(false);
            rlPopupWindow.setVisibility(View.INVISIBLE);
            btnDetails_wAddToCart.setVisibility(View.INVISIBLE);
        });

        btnFavorite_empty.setOnClickListener(view -> {
//            if (isFavoritePresent) {
//                FavoriteProduct.lstProduct.remove(product);
//                btnFavorite.setImageResource(R.drawable.grey_heart_icon);
//                isFavoritePresent = false;
//            } else {
//                FavoriteProduct.lstProduct.add(product);
//                btnFavorite.setImageResource(R.drawable.red_heart_icon);
//                isFavoritePresent = true;
//                saveDataToDatabase(userEmail, product.getProduct_id());
//            }
            btnFavorite_full.setVisibility(View.VISIBLE);
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_heart);
            btnFavorite_full.startAnimation(animation);
            animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out_heart);
            btnFavorite_full.startAnimation(animation);
            btnFavorite_empty.setVisibility(View.INVISIBLE);
            btnFavorite_full.setClickable(true);

        });
        btnFavorite_full.setOnClickListener(view -> {
            btnFavorite_empty.setVisibility(View.VISIBLE);
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_heart);
            btnFavorite_full.startAnimation(animation);
            btnFavorite_full.setVisibility(View.INVISIBLE);
            btnFavorite_full.setClickable(false);
        });

        ibtnDetails_remove_1.setOnClickListener(view -> {
            if (productQuantityAdded >= 1) {
                productQuantityAdded--;
            }
            SetTextForQuantity();
        });

        ibtnDetails_remove_2.setOnClickListener(view -> {
            if (productQuantityAdded >= 1) {
                productQuantityAdded--;
            }
            SetTextForQuantity();
        });

        ibtnDetails_add_1.setOnClickListener(view -> {
            if (productQuantityAdded < selectedOptionQuantity) {
                productQuantityAdded++;
            }
            SetTextForQuantity();
        });

        ibtnDetails_add_2.setOnClickListener(view -> {
            if (productQuantityAdded < selectedOptionQuantity) {
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
            supportFinishAfterTransition();
        });
    }

    void reset_gvCapacitiesBtns() {
        for (int i = 0; i < gvCapacities.getChildCount(); i++) {
            TextView textView = gvCapacities.getChildAt(i).findViewById(R.id.tvCapacity);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.WHITE);
            gvCapacities.setItemChecked(i, false);
        }
    }

    void AddProductItem2Cart(boolean shouldMove2Cart) {
        cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ShoppingCart CartItemProduct = dataSnapshot.getValue(ShoppingCart.class);
                    String productID = CartItemProduct.getProductID();
                    String productMemOpt = CartItemProduct.getMemoryOptID();

                    shouldAddQuantty = productID.equals(product.getProductID()) && productSelectedOption.equals(productMemOpt);
                    if (shouldAddQuantty) {
                        if (selectedOptionQuantity >= CartItemProduct.getQuantity() + productQuantityAdded) {
                            int newQuantity = CartItemProduct.getQuantity() + productQuantityAdded;
                            dataSnapshot.getRef().child("quantity").setValue(newQuantity);
                            Toast.makeText(ProductDetailsActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Thêm thất bại. Số lượng sản phẩm có thể thêm: " + (selectedOptionQuantity - CartItemProduct.getQuantity()), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }

                boolean shouldCreateNewCartItem = !shouldAddQuantty;
                if (shouldCreateNewCartItem) {
                    ShoppingCart itemPutInCart = new ShoppingCart(product.getProductID(), productQuantityAdded, productSelectedOption);
                    SaveCardItem(itemPutInCart);
                }
                if (shouldMove2Cart) {
                    Move2Cart();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void Move2Cart(){
        Intent intentt = new Intent(ProductDetailsActivity.this, Cart.class);
        startActivity(intentt);
    }

    private void SaveCardItem(ShoppingCart CartItem) {
        String productId = cartReference.push().getKey();
        CartItem.setUserID(auth.getCurrentUser().getUid());
        String x = auth.getCurrentUser().getEmail();
        String y = auth.getCurrentUser().getPhoneNumber();
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