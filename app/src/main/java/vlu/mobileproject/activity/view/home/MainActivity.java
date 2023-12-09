package vlu.mobileproject.activity.view.home;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import vlu.mobileproject.DrawerItem;
import vlu.mobileproject.Favorite;
import vlu.mobileproject.HomeFragment;
import vlu.mobileproject.R;
import vlu.mobileproject.SettingFragment;
import vlu.mobileproject.SimpleItem;
import vlu.mobileproject.SpaceItem;
import vlu.mobileproject.SubFragment1;
import vlu.mobileproject.SubFragment2;
import vlu.mobileproject.SubFragment3;
import vlu.mobileproject.activity.view.cart.Cart;
import vlu.mobileproject.activity.view.order.OrderHistoryActivity;
import vlu.mobileproject.activity.view.search.SearchFragment;
import vlu.mobileproject.adapter.DrawerAdapter;
import vlu.mobileproject.activity.view.profile.Information_Account;
import vlu.mobileproject.databinding.ActivityMainBinding;
import vlu.mobileproject.globalfuction.DarkModeUtils;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.globalfuction.ImageHandler;
import vlu.mobileproject.login.LoginActivity;
import vlu.mobileproject.login.SignupActivity;
import vlu.mobileproject.login.StartupActivity;
import vlu.mobileproject.login.loading;
import vlu.mobileproject.modle.Products;
import vlu.mobileproject.modle.User;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_MAIN_MENU = 0;
    private static final int POS_SUB_ITEM1 = 1;
    private static final int POS_SUB_ITEM2 = 2;
    private static final int POS_SUB_ITEM3 = 3;
    private static final int POS_SETTING = 4;

    // bottom navigation View
    ActionBar actionBar;
    BottomNavigationView BotNavMenu;
    Toolbar toolbar;
    RelativeLayout layoutBorder;
    ImageButton myImageButton, ibtnCart;
    public static String bet = "https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/";

    TextView tvDisplayName, tvUserEmail;
    private String[] screenTitles;

    private SlidingRootNav slidingRootNav;
    CardView cvBorderNavigation;
    boolean toReverseCorner = true;
    boolean isDataLoaded = false;
    RelativeLayout btnLogout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
        loadActivity();

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withDragDistance(245)
                .withRootViewScale(0.70f)
                .withRootViewElevation(25)
                .withToolbarMenuToggle(toolbar)
                .withMenuLayout(R.layout.drawer_navigation)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .addDragStateListener(new DragStateListener() {
                    @Override
                    public void onDragStart() {
                        animateCornerRadius(0, 70, toReverseCorner);
                    }

                    @Override
                    public void onDragEnd(boolean isMenuOpened) {
                        if(isMenuOpened)
                            toReverseCorner = false;
                        else toReverseCorner = true;

                    }
                })
                .inject();
        toolbar.setNavigationIcon(R.drawable.menu_icon);
        // Load the saved night mode state from SharedPreferences and apply it


        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_MAIN_MENU).setChecked(true),
                createItemFor(POS_SUB_ITEM1),
                createItemFor(POS_SUB_ITEM2),
                createItemFor(POS_SUB_ITEM3),
                createItemFor(POS_SETTING)

        ));
        adapter.setListener(this);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView list = findViewById(R.id.drawer_list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_MAIN_MENU);

        myImageButton = findViewById(R.id.ibClose);

        myImageButton.setOnClickListener(v -> slidingRootNav.closeMenu());

        boolean isMenuClosed = slidingRootNav.isMenuClosed();
        changeLayoutColor(isMenuClosed);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout ll_logout = findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(this::onLayoutClicked);
    }
    private void applyNightMode(boolean isNightMode) {
        // Apply the selected mode to the entire app using DarkModeUtils
        DarkModeUtils.applyNightMode(this, isNightMode);
    }
    private void addControls() {
        View  drawer_navigation = getLayoutInflater().inflate(R.layout.drawer_navigation, null);

        ibtnCart= findViewById(R.id.ibtnCart);

        layoutBorder = findViewById(R.id.layout_border);
        toolbar = findViewById(R.id.toolbar);
        BotNavMenu = findViewById(R.id.BotNavMenu);
        //from drawer_navigation layout

        btnLogout = drawer_navigation.findViewById(R.id.btnLogout);

        cvBorderNavigation = findViewById(R.id.cvBorderNavigation);
    }

    private void addEvents() {
        BotNavMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            actionBar = getSupportActionBar();

            if (id == R.id.item_btt_nav_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.item_btt_nav_favorite) {
                Intent intent = new Intent(MainActivity.this, Favorite.class);
                startActivity(intent);

                return true;
            } else if (id == R.id.item_btt_nav_QR) {
                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
                // Load the QRFragment (if available)
                //loadFragment(new QRFragment());
                return true;
            }else if (id == R.id.item_btt_nav_search) {
                loadFragment(new SearchFragment());
                return true;
            } else if (id == R.id.item_btt_nav_profile) {
                Intent intent = new Intent(MainActivity.this, Information_Account.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        ibtnCart.setOnClickListener(view -> {
            Intent openCart = new Intent(MainActivity.this, Cart.class);
            startActivity(openCart);

        });

        btnLogout.setOnClickListener(view -> {
            Intent logout = new Intent(MainActivity.this, Cart.class);
            startActivity(logout);
        });

    }

    public void loadActivity(){
        // apply night mode for all activity
        boolean isNightMode = DarkModeUtils.isNightModeEnabled(this);
        applyNightMode(isNightMode);

    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer, fragment);
        fragmentTransaction.commit();
    }

    private void changeLayoutColor(boolean isMenuClosed) {
        if (isMenuClosed) {
            layoutBorder.setBackground(null);
            layoutBorder.setBackgroundColor(Color.WHITE);
        } else {
            layoutBorder.setBackground(null);
        }
    }

    public void onLayoutClicked(View view) {
        if (view.getId() == R.id.ll_logout || view.getId() == R.id.ivCircle || view.getId() == R.id.ivCircle || view.getId() == R.id.tvLogout) {
            Logout();
        }
    }

    void Logout(){
        FirebaseAuth.getInstance().signOut();
        Paper.init(MainActivity.this);
        Paper.book().destroy();

        Intent intent = new Intent(MainActivity.this, StartupActivity.class);
        startActivity(intent);
        finish();
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenTitles[position])
                .withTextTint(color(R.color.white))
                .withSelectedTextTint(color(R.color.white));
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenTitles);
    }

    /*@Override
    public void onBackPressed() {
        finish();
    }*/

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (position == POS_MAIN_MENU) {
            HomeFragment homeFragment = new HomeFragment();
            transaction.replace(R.id.mainContainer, homeFragment);
        } else if (position == POS_SUB_ITEM1) {
            SubFragment1 subFragment1 = new SubFragment1("Iphone");
            transaction.replace(R.id.mainContainer, subFragment1);
        } else if (position == POS_SUB_ITEM2) {
            SubFragment1 subFragment1 = new SubFragment1("Samsung");
            transaction.replace(R.id.mainContainer, subFragment1);
        } else if (position == POS_SUB_ITEM3) {
            SubFragment3 subFragment3 = new SubFragment3();
            transaction.replace(R.id.mainContainer, subFragment3);
        } else if (position == POS_SETTING) {
            Intent intent = new Intent(MainActivity.this, Information_Account.class);
            startActivity(intent);
        }

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void animateCornerRadius(int startRadius, int endRadius, boolean x) {
        ValueAnimator valueAnimator;
        if(x){
            valueAnimator = ValueAnimator.ofFloat(startRadius, endRadius);
        }
        else{
            valueAnimator = ValueAnimator.ofFloat(endRadius, startRadius);
        }

        valueAnimator.setDuration(200); // Set the duration of the animation in milliseconds
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                cvBorderNavigation.setRadius(dpToPx(animatedValue));
            }
        });

        valueAnimator.start();
    }

    private float dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return dp * density;
    }

}