package vlu.mobileproject;

import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import vlu.mobileproject.adapter.CategoriesAdapter;

public class HomeFragment extends Fragment implements CategoriesAdapter.OnItemSelectedListener {
    private static final int POS_ALL = 0;
    private static final int POS_SAMSUNG = 1;
    private static final int POS_IPHONE = 2;

    private String[] screenCategories;
    RecyclerView rvCategories;


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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        screenCategories = loadScreenTitles();
        rvCategories = view.findViewById(R.id.rvCategories);

        CategoriesAdapter adapter = new CategoriesAdapter(Arrays.asList(
                createItemFor(POS_ALL).setChecked(true),
                createItemFor(POS_SAMSUNG),
                createItemFor(POS_IPHONE)
        ));

        adapter.setListener((CategoriesAdapter.OnItemSelectedListener) this);

        rvCategories.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setAdapter(adapter);

        adapter.setSelected(POS_ALL);

        return view;
    }

    private CategoriesCheck createItemFor(int position){
        return new CategoriesItem(screenCategories[position])
                .withTextTint(color(R.color.greyTextVLUS))
                .withSelectedTextTint(color(R.color.white))
                .withBackgroundColor(color(R.color.greyVLUS))
                .withSelectedBackgroundColor(color(R.color.greenVLUS));
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

}