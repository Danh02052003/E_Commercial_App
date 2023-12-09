package vlu.mobileproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                animateTextChange(DiscountList.get(currentIndex).getDiscountUrl(), DiscountList.get(currentIndex).getDiscountDescription());
                currentIndex++;
            } else {
                currentIndex = 0;
            }

            handler.postDelayed(this, 4000);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
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

        CategoriesAdapter adapter = new CategoriesAdapter(Arrays.asList(
                createItemFor(POS_ALL).setChecked(true),
                createItemFor(POS_SAMSUNG),
                createItemFor(POS_IPHONE)
        ));

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
    private void animateTextChange(String discountImgUrl, String newDiscountDescription) {
        // Fade-out animation for discountDescription
        ObjectAnimator fadeOutDiscountDescription = ObjectAnimator.ofFloat(tvContent2, "alpha", 1f, 0f);
        ObjectAnimator fadeOutDiscountImage = ObjectAnimator.ofFloat(ivHero, "alpha", 1f, 0f);
        fadeOutDiscountDescription.setDuration(500);
        fadeOutDiscountImage.setDuration(500);

        fadeOutDiscountDescription.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tvContent2.setText(newDiscountDescription);
                ObjectAnimator fadeInDiscountDescription = ObjectAnimator.ofFloat(tvContent2, "alpha", 0f, 1f);
                fadeInDiscountDescription.setDuration(500);
                fadeInDiscountDescription.start();
            }
        });
        fadeOutDiscountImage.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Picasso.get().load(discountImgUrl).into(ivHero);
                ObjectAnimator fadeOutDiscountImage = ObjectAnimator.ofFloat(ivHero, "alpha", 0f, 1f);
                fadeOutDiscountImage.setDuration(500);
                fadeOutDiscountImage.start();
            }
        });

        fadeOutDiscountImage.start();
        fadeOutDiscountDescription.start();
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
}