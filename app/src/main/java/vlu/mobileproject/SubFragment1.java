package vlu.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vlu.mobileproject.adapter.HomeChildAdapter;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.Products;

public class SubFragment1 extends Fragment implements GlobalData.Callback {

    RecyclerView RecNav;

    HomeChildAdapter adapter;

    ArrayList<Products> allProducts;

    TextView TitleText;
    boolean isDataLoaded = false;

    String typeOfPhone;
    public SubFragment1(String TypeofPhone) {
        typeOfPhone = TypeofPhone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sample_fragment, container, false);

        TitleText = root.findViewById(R.id.Title);
        TitleText.setText(typeOfPhone);
        GlobalData.initData(getContext(), this);

        List<Products> filteredProducts = new ArrayList<>();
        for (Products product : allProducts) {
            if (product.getProduct_name().toLowerCase(Locale.getDefault()).contains(typeOfPhone.toLowerCase(Locale.getDefault()))) {
                filteredProducts.add(product);
            }
        }
        adapter = new HomeChildAdapter(getContext(), filteredProducts);

        RecNav = root.findViewById(R.id.rvForFavorite);
        RecNav.setLayoutManager(new GridLayoutManager(getContext(), 2));

        RecNav.setAdapter(adapter);
        return root;
    }

    @Override
    public void onCompleted(List<Products> products) {
        allProducts = new ArrayList<>(products);
    }
}
