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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements CategoriesAdapter.OnItemSelectedListener {
    private static final int POS_ALL = 0;
    private static final int POS_SAMSUNG = 1;
    private static final int POS_IPHONE = 2;

    private String[] screenCategories;
    RecyclerView rvCategories;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
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
            loadFragment(new SamsungFrag());
        }

    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.flCategory, fragment);
        ft.commit();

    }

}