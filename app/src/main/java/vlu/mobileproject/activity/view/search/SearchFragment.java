package vlu.mobileproject.activity.view.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;


import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import vlu.mobileproject.AllFrag;
import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.R;
import vlu.mobileproject.adapter.ProductAdapter;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.Products;


public class SearchFragment extends Fragment implements filterBottomSheetFragment.FilterListener, GlobalData.Callback {

    SearchView searchItem;
    ListView listViewProduct;

    private ArrayList<Products> allProducts;
    private ArrayList<Products> displayedProducts;
    AllFrag allFragment = (AllFrag) getParentFragment();

    int minPriceValue, maxPriceValue = 5000;

    ImageButton btnFilter;
    private ProductAdapter productAdapter;
    RangeSlider rangeSlider;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment

        // addControls
        btnFilter = view.findViewById(R.id.btnFilter);
        listViewProduct = view.findViewById(R.id.LVProduct);
        searchItem = view.findViewById(R.id.searchItem);

        // Initialize views and set up RangeSlider listener
        rangeSlider = view.findViewById(R.id.range_slider);

        addEvents();

        // Connect to the parent fragment (AllFrag) and retrieve all products

        // Setup the search view for filtering
        searchItem.clearFocus();
        return view;
    }

    private void addEvents() {
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing on submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProductList(newText);
                return true;
            }
        });

        listViewProduct.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < displayedProducts.size()) {
                Products selectedProduct = displayedProducts.get(position);
                if (selectedProduct != null) {
                    Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
                    intent.putExtra("object_product", selectedProduct);
                    startActivity(intent);
                }
            }
        });

        btnFilter.setOnClickListener(v -> {
            filterBottomSheetFragment bottomSheetFragment = new filterBottomSheetFragment();
            bottomSheetFragment.setFilterListener(this); // Set the FilterListener to receive the filter values
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
        });
        GlobalData.initData(getContext(), this);
    }


    @Override
    public void onFilterApplied(int startValue, int endValue) {

        minPriceValue = startValue;
        maxPriceValue = endValue;
        filterProductList(searchItem.getQuery().toString());
    }

    @Override
    public void onCancelFilter() {

    }

    void LoadProductData() {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Product_2");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Products products = dataSnapshot.getValue(Products.class);
                        allProducts.add(products);
                    }

                    // Initialize the displayedProducts list and limit it to 5 items initially
                    displayedProducts = new ArrayList<>();
                    int limit = Math.min(allProducts.size(), 5);
                    for (int i = 0; i < limit; i++) {
                        displayedProducts.add(allProducts.get(i));
                    }

                    // Initialize the adapter with displayedProducts
                    productAdapter = new ProductAdapter(requireContext(), displayedProducts);
                    listViewProduct.setAdapter(productAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // function
    private void filterProductList(String query) {
        // Create a new list to store the filtered products
        List<Products> filteredProducts = new ArrayList<>();
        // Iterate through all products and check for matching names and price range
        for (Products product : allProducts) {
            // Check for matching names
            if (product.getProduct_name().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                // Check for price range
                int productPrice = (int) product.getPriceForMemory(); // Convert the price to int
                if (productPrice >= this.minPriceValue && productPrice <= this.maxPriceValue) {
                    filteredProducts.add(product);
                }
            }
        }

        // Update the displayedProducts list with the filtered list
        displayedProducts.clear();
        int limit = Math.min(filteredProducts.size(), 5);
        for (int i = 0; i < limit; i++) {
            displayedProducts.add(filteredProducts.get(i));
        }

        // Update the adapter data with the filtered list
        productAdapter.updateData(displayedProducts);
        listViewProduct.setAdapter(productAdapter);
    }

    @Override
    public void onCompleted(List<Products> products) {
        if (allFragment == null) {
            allProducts = new ArrayList<>(products);
        }

        // Initialize the displayedProducts list and limit it to 5 items initially
        displayedProducts = new ArrayList<>();
        int limit = Math.min(allProducts.size(), 5);
        for (int i = 0; i < limit; i++) {
            displayedProducts.add(allProducts.get(i));
        }

        // Initialize the adapter with displayedProducts
        productAdapter = new ProductAdapter(requireContext(), displayedProducts);
        listViewProduct.setAdapter(productAdapter);
    }
}