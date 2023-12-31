package vlu.mobileproject.activity.view.home;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
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
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

import java.util.Arrays;

import io.paperdb.Paper;
import vlu.mobileproject.DrawerItem;
import vlu.mobileproject.Favorite;
import vlu.mobileproject.HomeFragment;
import vlu.mobileproject.R;
import vlu.mobileproject.SimpleItem;
import vlu.mobileproject.SubFragment1;
import vlu.mobileproject.SubFragment3;
import vlu.mobileproject.activity.view.cart.Cart;
import vlu.mobileproject.activity.view.order.OrderHistoryActivity;
import vlu.mobileproject.activity.view.search.SearchFragment;
import vlu.mobileproject.adapter.DrawerAdapter;
import vlu.mobileproject.activity.view.profile.Information_Account;
import vlu.mobileproject.data.PhoneType;
import vlu.mobileproject.globalfuction.DarkModeUtils;
import vlu.mobileproject.login.StartupActivity;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_MAIN_MENU = 0;
    private static final int POS_SUB_ITEM1 = 1;
    private static final int POS_SUB_ITEM2 = 2;
    private static final int POS_SUB_ITEM3 = 3;
    private static final int POS_SETTING = 4;

    // bottom navigation View
    ActionBar actionBar;
    BottomNavigationView BotNavMenu;
    Toolbar toolbar, toolbar_2;
    RelativeLayout layoutBorder;
    ImageButton myImageButton, ibtnCart;
    public static String bet = "https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/";

    TextView tvDisplayName, tvUserEmail;
    private String[] screenTitles;

    private SlidingRootNav slidingRootNav;
    CardView cvBorderNavigation, cvLayout_shadow;
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
                .withDragDistance(220)
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
                        animateCornerRadius(0, 50, toReverseCorner);
                    }

                    @Override
                    public void onDragEnd(boolean isMenuOpened) {
                        if(isMenuOpened)
                            toReverseCorner = false;
                        else {
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_anim_1_reverse);
                            cvLayout_shadow.startAnimation(animation);
                            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_anim_2_reverse);
                            cvBorderNavigation.startAnimation(animation);
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                cvLayout_shadow.clearAnimation();
                                cvBorderNavigation.clearAnimation();
                            }, 200);
                            toReverseCorner = true;}


                    }
                })
                .inject();
        toolbar.setNavigationIcon(R.drawable.menu_icon);
        toolbar_2.setNavigationIcon(R.drawable.menu_icon);

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
        View drawer_navigation = getLayoutInflater().inflate(R.layout.drawer_navigation, null);

        ibtnCart= findViewById(R.id.ibtnCart);

        layoutBorder = findViewById(R.id.layout_border);
        toolbar = findViewById(R.id.toolbar);
        toolbar_2 = findViewById(R.id.toolbar_2);
        BotNavMenu = findViewById(R.id.BotNavMenu);
        //from drawer_navigation layout

        btnLogout = drawer_navigation.findViewById(R.id.btnLogout);

        cvBorderNavigation = findViewById(R.id.cvBorderNavigation);
        cvLayout_shadow = findViewById(R.id.cvLayout_shadow);
    }

    private void addEvents() {
        BotNavMenu.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            actionBar = getSupportActionBar();

            if (id == R.id.item_btt_nav_home) {
                loadFragment(new HomeFragment(this));
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        if (position == POS_MAIN_MENU) {
            HomeFragment homeFragment = new HomeFragment(this);
            HomeFragment homeFragment1  = homeFragment.cloneFragment();
            transaction.replace(R.id.mainContainer, homeFragment);
            transaction.replace(R.id.mainContainer_2, homeFragment1);


        } else if (position == POS_SUB_ITEM1) {
            SubFragment1 subFragment1 = new SubFragment1(PhoneType.Iphone);
            SubFragment1 subFragment1_1 = new SubFragment1(PhoneType.Iphone);
            transaction.replace(R.id.mainContainer, subFragment1);
//            transaction_shadow.replace(R.id.mainContainer_2, subFragment1_1);
        } else if (position == POS_SUB_ITEM2) {
            SubFragment1 subFragment1 = new SubFragment1(PhoneType.Samsung);
            SubFragment1 subFragment1_1 = new SubFragment1(PhoneType.Iphone);
            transaction.replace(R.id.mainContainer, subFragment1);
//            transaction_shadow.replace(R.id.mainContainer_2, subFragment1_1);
        } else if (position == POS_SUB_ITEM3) {
            Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
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
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_anim_1);
        cvLayout_shadow.startAnimation(animation);
        animation = android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.main_anim_2);
        cvBorderNavigation.startAnimation(animation);

        valueAnimator.start();
    }

    private float dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return dp * density;
    }

}