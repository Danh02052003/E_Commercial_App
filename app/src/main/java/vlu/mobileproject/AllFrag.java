package vlu.mobileproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vlu.mobileproject.adapter.HomeParentAdapter;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.globalfuction.RecentlyViewedManager;
import vlu.mobileproject.modle.Product;
import vlu.mobileproject.modle.Products;


public class AllFrag extends Fragment implements GlobalData.Callback {
    RecyclerView rvCategory;
    RecyclerView rvChildList;
    private HomeParentAdapter parentAdapter;
    private List<HomeParentItem> parentItemList;
    List<Products> products = new ArrayList<>();
    boolean isDataLoaded = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AllFrag() {
    }
    public static AllFrag newInstance(String param1, String param2) {
        AllFrag fragment = new AllFrag();
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
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        rvCategory = view.findViewById(R.id.rvCategory);

        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }});
        View anotherLayout = LayoutInflater.from(getContext()).inflate(R.layout.home_parent_item, null);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

//        if (parentItemList == null || parentItemList.isEmpty()) {
//            GlobalData.initData(getContext(), this);
//
//        }
        if(isDataLoaded == false){
            GlobalData.initData(getContext(), this);
        }
        rvChildList = anotherLayout.findViewById(R.id.rvChildList);
        rvChildList.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onCompleted(List<Products> products) {
        if(isDataLoaded == false){
            this.products = products;
//            Collections.shuffle(products);
            isDataLoaded = true;
            loadUI();
        }
        else {
            loadUI();
        }

    }
    void loadUI(){
        List<Products> foryou_list = new ArrayList<>();
        List<Products> highlight_list = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (i < products.size() / 2)
                foryou_list.add(products.get(i));
            else highlight_list.add(products.get(i));
        }
        parentItemList = new ArrayList<>();
        parentItemList.add(new HomeParentItem("Dành cho bạn", foryou_list));
        parentItemList.add(new HomeParentItem("Sản phẩm nổi bật", highlight_list));
        parentAdapter = new HomeParentAdapter(parentItemList);
        rvCategory.setAdapter(parentAdapter);
        parentAdapter.notifyDataSetChanged();
        RecentlyViewedManager.recentlyViewedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();

                if(count > 0){
                    RecentlyViewedManager.GetRecentlyViewedProducts(new ValueEventListener() {
                        List<Products> recently_viewed_list = new ArrayList<>();
                        List<String> products_key = new ArrayList<>();
                        List<Long> time_viewed = new ArrayList<>();
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            recently_viewed_list.clear();
                            products_key.clear();
                            time_viewed.clear();

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                time_viewed.add(dataSnapshot.getValue(Long.class));
                                Collections.sort(time_viewed, Collections.reverseOrder());
                            }

                            for(int i = 0; i < time_viewed.size(); i++)
                                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                                    if(dataSnapshot.getValue(Long.class).equals(time_viewed.get(i)))
                                        products_key.add(dataSnapshot.getKey());


                            for(int i = 0; i < products_key.size(); i++)
                                for(Products product : products)
                                    if(product.getProductID().equals(products_key.get(i)))
                                        recently_viewed_list.add(product);

                            if(parentItemList.size() == 2){
                                parentItemList.add(new HomeParentItem("Đã xem gần đây", recently_viewed_list));
                                parentAdapter.notifyDataSetChanged();
                            }else{
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        parentAdapter.notifyDataSetChanged();
                                    }
                                }, 5000);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}