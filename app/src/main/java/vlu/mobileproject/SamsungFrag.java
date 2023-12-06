package vlu.mobileproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.adapter.HomeParentAdapter;
import vlu.mobileproject.modle.Products;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SamsungFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SamsungFrag extends Fragment {
    RecyclerView rvCategory;
    RecyclerView rvChildList;
    private HomeParentAdapter parentAdapter;
    private List<HomeParentItem> parentItemList;
    List<Products> products = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SamsungFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SamsungFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SamsungFrag newInstance(String param1, String param2) {
        SamsungFrag fragment = new SamsungFrag();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_samsung, container, false);
        rvCategory = view.findViewById(R.id.rvCategory);

        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }});


        View anotherLayout = LayoutInflater.from(getContext()).inflate(R.layout.home_parent_item, null);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvChildList = anotherLayout.findViewById(R.id.rvChildList);
        rvChildList.setLayoutManager(layoutManager);

        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference productRef = database.getReference("Products_2");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products product = dataSnapshot.getValue(Products.class);

                    String imageCategory_path = "samsung";
                    product.setProduct_img("product_image/"+imageCategory_path+"/"+product.getProduct_img());
                    products.add(product);
                    product.setProductID(dataSnapshot.getKey());
                }
                loadUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(this.toString(), "Error loading products from Firebase: " + error.getMessage());
            }
        });
        return view;
    }
    void loadUI(){
        List<Products> forYou_list = new ArrayList<>();
        List<Products> highlight_list = new ArrayList<>();
        for(int i = 0; i < products.size(); i++){
            if(i<products.size()/2)
                forYou_list.add(products.get(i));
            else highlight_list.add(products.get(i));
        }
        parentItemList = new ArrayList<>();
        parentItemList.add(new HomeParentItem("Dành cho bạn", forYou_list));
        parentItemList.add(new HomeParentItem("Sản phẩm nổi bật", highlight_list));

        parentAdapter = new HomeParentAdapter(parentItemList);
        rvCategory.setAdapter(parentAdapter);

    }
}