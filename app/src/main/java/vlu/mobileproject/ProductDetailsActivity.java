package vlu.mobileproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import vlu.mobileproject.activity.view.cart.Cart;
import vlu.mobileproject.login.LoginActivity;
import vlu.mobileproject.login.UserManager;
import vlu.mobileproject.login.user;
import vlu.mobileproject.modle.FavoriteFirebase;

public class ProductDetailsActivity extends AppCompatActivity {
    static String bet = "https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/";

    Button btnDetails_addToCart, btnDetails_buyNow;
    
    ImageButton btnFavorite, ibtnDetails_remove_1, ibtnDetails_add_1, btnBack;

    RelativeLayout btnDetails_wAddToCart, btnDetails_wBuyNow;
    RelativeLayout rlPopupWindow;
    CardView cvPopupWindow_display;
    GridView gvCapacities;
    ArrayAdapter<String> capacitiesAdapter;
    HomeChildItem product;
    TextView tvDetails_productName, tvDetails_productPrice, tvDetails_productDescr, tvDetailsAddProduct_productPrice,
            tvDetails_quantity, tvDetails_quantity_2, tvDetails_nProductLeft, tvDetails_expand;
    ImageView ivDetails_productIllustration, ivDetailsAddProduct_productImg, ibtnDetails_remove_2, ibtnDetails_add_2;
    double productPriceBasedCapacity;

    int productQuantityAdded = 1;
    boolean isFavoritePresent;
    private FirebaseAuth firebaseAuth;
    private String userEmail = UserManager.getInstance().getUserEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        addControl();
        addEvent();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        product = (HomeChildItem) bundle.getSerializable("object_product");
        // Check if the product object is not null before accessing its properties
        if (product != null) {
            tvDetails_productName.setText(product.getProductName());
            tvDetails_productPrice.setText("$" + String.valueOf(product.getProductPrice()));
            tvDetails_productDescr.setText("Ngày sản xuất: " + product.getCreatedDate() + "\n" + product.getProductDescription());
            ivDetails_productIllustration.setImageResource(product.getProductImg());
            ivDetailsAddProduct_productImg.setImageResource(product.getProductImg());
            tvDetailsAddProduct_productPrice.setText("$" + String.valueOf(product.getProductPrice()));
            tvDetails_nProductLeft.setText("Còn " + product.getQuantity() + " sản phẩm.");

            isFavoritePresent = FavoriteProduct.lstProduct.contains(product);
            if(isFavoritePresent)
                btnFavorite.setImageResource(R.drawable.red_heart_icon);

            capacitiesAdapter = new ArrayAdapter<>(this, R.layout.capacity_item, R.id.tvCapacity, product.getCapacities());
            gvCapacities.setAdapter(capacitiesAdapter);
            gvCapacities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if(gvCapacities.isItemChecked(position)) {
                        view = gvCapacities.getChildAt(position);
                        view.setBackgroundColor(Color.BLUE);
                        productPriceBasedCapacity = position * 50;
                    }else {
                        view = gvCapacities.getChildAt(position);
                        view.setBackgroundColor(124333);

                    }
                    capacitiesAdapter.notifyDataSetChanged();
            }});

        }
            else {
        }

        SetTextForQuantity();


    }

    private void addControl(){
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

        firebaseAuth = FirebaseAuth.getInstance();

        tvDetails_quantity = findViewById(R.id.tvDetails_quantity);
        tvDetails_quantity_2 = findViewById(R.id.tvDetails_quantity_2);

        btnFavorite = findViewById(R.id.btnFavorite);
        gvCapacities = findViewById(R.id.gvCapacities);

    }

    private void addEvent(){
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
            bundleSender.putSerializable("object_product",product);
            intentt.putExtras(bundleSender);
            ShoppingCart itemPutInCart = new ShoppingCart(product, productQuantityAdded, product.getProductPrice() + productPriceBasedCapacity);
//           ShoppingCart.lstProduct.add(product);
//           ShoppingCart.lstQuantity.add(productQuantityAdded);
            startActivity(intentt);

        });

        btnDetails_wAddToCart.setOnClickListener(view -> {
           boolean found = false;

            for (int i = 0; i < ShoppingCart.lstProduct.size(); i++) {
                if (ShoppingCart.lstProduct.get(i).item.equals(product)) {
                    ShoppingCart.lstProduct.get(i).setQuantity(ShoppingCart.lstProduct.get(i).quantity + 1);
                    found = true; // Set the flag to true when the condition is satisfied
                    break;
                }
            }

            if (!found) {
                ShoppingCart itemPutInCart = new ShoppingCart(product, productQuantityAdded, product.getProductPrice() + productPriceBasedCapacity);
                // Perform any necessary operations related to the new itemPutInCart
            }

            rlPopupWindow.setVisibility(View.INVISIBLE);
            btnDetails_wAddToCart.setVisibility(View.INVISIBLE);
        });

        btnFavorite.setOnClickListener(view -> {
            if(isFavoritePresent){
                FavoriteProduct.lstProduct.remove(product);
                btnFavorite.setImageResource(R.drawable.grey_heart_icon);
                isFavoritePresent = false;
            }
            else {
                FavoriteProduct.lstProduct.add(product);
                btnFavorite.setImageResource(R.drawable.red_heart_icon);
                isFavoritePresent = true;
                saveDataToDatabase(userEmail, product.getProductId());
            }

        });

        ibtnDetails_remove_1.setOnClickListener(view -> {
            if (productQuantityAdded != 0){
                productQuantityAdded--;
            }
            SetTextForQuantity();
        });

        ibtnDetails_remove_2.setOnClickListener(view -> {
            if (productQuantityAdded != 0){
                productQuantityAdded--;
            }
            SetTextForQuantity();
        });

        ibtnDetails_add_1.setOnClickListener(view -> {
            if (productQuantityAdded < product.getQuantity()){
                productQuantityAdded++;
            }
            SetTextForQuantity();
        });

        ibtnDetails_add_2.setOnClickListener(view -> {
            if (productQuantityAdded < product.getQuantity()){
                productQuantityAdded++;
            }
            SetTextForQuantity();
        });

        tvDetails_expand.setOnClickListener(view -> {
            if (tvDetails_expand.getText().equals("Xem thêm >")){
                tvDetails_productDescr.setMaxLines(10);
                tvDetails_expand.setText("Thu gọn <");
            }
            else {
                tvDetails_productDescr.setMaxLines(2);
                tvDetails_expand.setText("Xem thêm >");
            }


        });

        btnBack.setOnClickListener(view -> {
            finish();
        });

    }

    private void SetTextForQuantity(){
        tvDetails_quantity.setText(String.valueOf(productQuantityAdded));
        tvDetails_quantity_2.setText(String.valueOf(productQuantityAdded));
    }

    private void saveDataToDatabase(String user_email, long product_id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(bet);
        DatabaseReference usersRef = database.getReference("favorite");
        FavoriteFirebase ff = new FavoriteFirebase(user_email, product_id);
        Log.d("ProductDetailsActivity", "User Email: " + userEmail); // Add this line to check the value in Logcat

        // Use push() method to generate a unique key for each entry
        DatabaseReference newFavoriteRef = usersRef.push();

        newFavoriteRef.setValue(ff)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Added to Favorites successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to add to Favorites.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}