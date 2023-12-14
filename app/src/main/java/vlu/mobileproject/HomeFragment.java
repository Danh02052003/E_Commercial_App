package vlu.mobileproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vlu.mobileproject.activity.view.cart.Cart;
import vlu.mobileproject.activity.view.home.MainActivity;
import vlu.mobileproject.adapter.CategoriesAdapter;
import vlu.mobileproject.data.FirebaseReferenceKey;
import vlu.mobileproject.modle.Discount;

public class HomeFragment extends Fragment implements CategoriesAdapter.OnItemSelectedListener {
    private static final int POS_ALL = 0;
    private static final int POS_SAMSUNG = 1;
    private static final int POS_IPHONE = 2;

    private String[] screenCategories;
    RecyclerView rvCategories;

    TextView tvContent2;

    ImageView ivHero;
    Button tvHeroButton;
    Context context;
    TextView tvDiscountName;
    ImageView gifImageView;
    String productID = "";

    DatabaseReference discountReference;
    int currentIndex = 0;
    private Handler handler = new Handler();
    List<Discount> DiscountList = new ArrayList<>();
    private Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            if (DiscountList.size() == 0) {
                handler.postDelayed(this, 500);
                return;
            }

            if (currentIndex < DiscountList.size()) {
                animateTextChange(DiscountList.get(currentIndex).getDiscountName(), DiscountList.get(currentIndex).getDiscountUrl(), DiscountList.get(currentIndex).getDiscountDescription());
                //productID = "";
                currentIndex++;
            } else {
                currentIndex = 0;
            }

            handler.postDelayed(this, 4000);
        }
    };

    public HomeFragment() {

    }
    public HomeFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(updateTextRunnable, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        screenCategories = loadScreenTitles();
        rvCategories = view.findViewById(R.id.rvCategories);
        tvContent2 = view.findViewById(R.id.tvContent2);
        ivHero = view.findViewById(R.id.ivHero);
        tvDiscountName = view.findViewById(R.id.tvDiscountName);
        gifImageView = view.findViewById(R.id.gifImageView);
        //tvHeroButton = view.findViewById(R.id.tvHeroButton);

        CategoriesAdapter adapter = new CategoriesAdapter(Arrays.asList(
                createItemFor(POS_ALL).setChecked(true),
                createItemFor(POS_SAMSUNG),
                createItemFor(POS_IPHONE)
        ));
//        tvHeroButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ProductDetailsActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("productID", productID);
//            intent.putExtras(bundle);
//            context.startActivity(intent);
//        });

        LoadDiscountInfo(view);

        adapter.setListener((CategoriesAdapter.OnItemSelectedListener) this);

        rvCategories.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setAdapter(adapter);

        adapter.setSelected(POS_ALL);

        return view;
    }

    void LoadDiscountInfo(View view) {
        discountReference = FirebaseDatabase.getInstance().getReference(FirebaseReferenceKey.DISCOUNT.getReferenceKey());
        discountReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot discSnapshot : snapshot.getChildren()) {
                        Discount discount = discSnapshot.getValue(Discount.class);
                        DiscountList.add(discount);
                    }
                } else {
                    Toast.makeText(view.getContext(), "404 Không thấy mã giảm giá", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view.getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private CategoriesCheck createItemFor(int position){
        return new CategoriesItem(screenCategories[position])
                .withTextTint(color(R.color.greyTextVLUS))
                .withSelectedTextTint(color(R.color.white))
                .withBackgroundColor(color(R.color.greyVLUS))
                .withSelectedBackgroundColor(color(R.color.greenVLUS));
    }
    private void animateTextChange(String discountName, String discountImgUrl, String newDiscountDescription) {
        // Fade-out animation for discountDescription
        performFadeInOutAnimation(500, tvContent2, newDiscountDescription, null);
        performFadeInOutAnimation(500, tvDiscountName, discountName, null);
        performFadeInOutAnimation(500, ivHero, null, discountImgUrl);

        if (context == null)
            return;
        ObjectAnimator fadeOutDiscountDescription = ObjectAnimator.ofFloat(gifImageView, "alpha", 1f, 0f);
        fadeOutDiscountDescription.setDuration(500);
        fadeOutDiscountDescription.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Glide.with(context).load(R.drawable.discount_gif).into(gifImageView);
                ObjectAnimator fadeInDiscountDescription = ObjectAnimator.ofFloat(gifImageView, "alpha", 0f, 1f);
                fadeInDiscountDescription.setDuration(500);
                fadeInDiscountDescription.start();
            }
        });
        fadeOutDiscountDescription.start();
    }

    private void performFadeInOutAnimation(int animSpeed, View view, String textToShow, String imgUrl) {
        ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOutAnimation.setDuration(animSpeed);
        fadeOutAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(textToShow);
                } else if (view instanceof ImageView) {
                    String imgLink = imgUrl.isEmpty() ? "https://robohash.org/" + Math.random() : imgUrl;
                    Picasso.get().load(imgLink).into((ImageView) view);
                }

                ObjectAnimator fadeInAnimation = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeInAnimation.setDuration(animSpeed);
                fadeInAnimation.start();
            }
        });
        fadeOutAnimation.start();
    }

    @ColorInt
    private int color(@ColorRes int res){
        return ContextCompat.getColor(requireContext(), res);
    }
    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.id_activityScreenCategories);
    }

    @Override
    public void onItemSelected(int position) {
        if(position == POS_ALL) {
            loadFragment(new AllFrag());

        }

        else if(position == POS_SAMSUNG) {
            loadFragment(new SamsungFrag());

        }
        else if (position == POS_IPHONE) {
            loadFragment(new IphoneFrag());
        }

    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.flCategory, fragment);
        ft.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the callback to avoid memory leaks
        handler.removeCallbacks(updateTextRunnable);
    }
    public HomeFragment cloneFragment() {
        HomeFragment clonedFragment = new HomeFragment();
        // Copy any necessary arguments or data from the original fragment to the cloned fragment here
        Bundle args = getArguments();
        if (args != null) {
            clonedFragment.setArguments(new Bundle(args));
        }
        return clonedFragment;
    }
}