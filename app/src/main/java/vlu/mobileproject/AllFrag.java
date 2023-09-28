package vlu.mobileproject;

import static vlu.mobileproject.globalfuction.GlobalData.forYou_list;
import static vlu.mobileproject.globalfuction.GlobalData.highlight_list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.adapter.HomeParentAdapter;
import vlu.mobileproject.globalfuction.GlobalData;


public class AllFrag extends Fragment {
    RecyclerView rvCategory;
    RecyclerView rvChildList;
    private HomeParentAdapter parentAdapter;
    private List<HomeParentItem> parentItemList;

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
        // Inflate the layout for this fragment
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
        // Initialize the parentItemList if it's null or empty
        if (parentItemList == null || parentItemList.isEmpty()) {
            GlobalData.initData();
        }
        rvChildList = anotherLayout.findViewById(R.id.rvChildList);
        rvChildList.setLayoutManager(layoutManager);
        //Dẫn xuất và gán ParentAdapter cho RecycleView cha

        parentItemList = new ArrayList<>();
        parentAdapter = new HomeParentAdapter(parentItemList);
        rvCategory.setAdapter(parentAdapter);
        parentItemList.add(new HomeParentItem("Dành cho bạn", forYou_list));
        parentItemList.add(new HomeParentItem("Sản phẩm nổi bật", highlight_list));
        return view;
    }

}