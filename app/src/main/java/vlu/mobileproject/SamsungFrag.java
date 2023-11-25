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
        DatabaseReference productRef = database.getReference("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products product = dataSnapshot.getValue(Products.class);

                    String imgUrl = dataSnapshot.child("productImage").getValue().toString();

                    product.setProduct_img(imgUrl);
                    products.add(product);
                }
                loadUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(this.toString(), "Error loading products from Firebase: " + error.getMessage());
            }
        });

//        List<HomeChildItem> forYou_list = new ArrayList<>();
//        forYou_list.add(new HomeChildItem("Galaxy S23 Plus", 1119.99, R.drawable.galaxy_s23_plus_graphite, 2023010160L, 1, 7, "01/02/2023", "Samsung Galaxy S23+ là phiên bản lớn hơn của Galaxy S23 thông thường với màn hình Dynamic AMOLED 6,6 inch với tốc độ làm mới 120Hz. Thông số kỹ thuật cũng bao gồm bộ vi xử lý Qualcomm Snapdragon 8 Gen 2, pin 4700mAh và thiết lập 3 camera ở mặt sau.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy Z Fold 3",  1799, R.drawable.galaxy_z_fold_3_phantom_silver, 2021010880L, 1, 5, "11/08/2021", "Samsung Galaxy Z Fold 3 là sản phẩm kế nhiệm của Galaxy Z Fold 2 và đi kèm với màn hình AMOLED 6,2 inch ở mặt trước và màn hình Dynamic AMOLED lớn 7,6 inch với tốc độ làm mới 120Hz khi mở ra. Bên dưới màn hình gập bên trong là camera dưới màn hình đầu tiên của Samsung với cảm biến 4MP. Thông số kỹ thuật cũng bao gồm bộ xử lý Qualcomm Snapdragon 888 dưới nắp máy, pin 4400mAh và thiết lập ba camera ở mặt sau với cảm biến chính 12MP.", new String[]{"256GB", "512GB", "1TB"}  ));
//        forYou_list.add(new HomeChildItem("Galaxy S21 Plus", 699, R.drawable.galaxy_s21_plus_purple, 2021010160L, 1, 4, "29/01/2021", "Samsung Galaxy S21+ là phiên bản lớn hơn của Galaxy S21 thông thường. Thông số kỹ thuật khôn ngoan, điện thoại mang chipset Snapdragon 888, khả năng 5G, dung lượng lưu trữ 128/256GB và pin 4800mAh. Màn hình Dynamic AMOLED 6,7 inch có tốc độ làm mới 120Hz thích ứng. Có một thiết lập ba camera với cảm biến chính 12MP ở mặt sau. Giá khởi điểm của Samsung Galaxy S21+ là 1000 USD.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21 Ultra", 749, R.drawable.galaxy_s21_ultra_caribbean, 2021010170L, 1, 3, "29/01/2021", "Samsung Galaxy S21 Ultra là sản phẩm tiêu biểu của dòng S21. Đây là điện thoại dòng S đầu tiên hỗ trợ bút S Pen của Samsung. Thông số kỹ thuật là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM lên tới 16GB và dung lượng lưu trữ 512GB. Viên pin 5000mAh giúp duy trì hoạt động của nhà máy điện này. Trong bộ phận máy ảnh, thiết lập bốn camera được cung cấp với hai cảm biến chụp ảnh xa.", new String[]{"256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy Note 20 Ultra", 599, R.drawable.galaxy_note_20_ultra_white, 2020010840L, 1, 3, "21/08/2020","Thông số kỹ thuật của Samsung Galaxy Note 20 bao gồm chipset Snapdragon 865 và một chip Exynos để phù hợp ở tất cả các khu vực ngoài Hoa Kỳ, đi kèm với 8GB RAM và dung lượng lưu trữ lên tới 256GB và pin 4300mAh. Ở phía sau là thiết lập ba camera từ Galaxy S20. Một màn hình sAMOLED 6,7 inch với tốc độ làm mới 60Hz ở phía trước. S Pen và 5G là tiêu chuẩn.", new String[]{"128GB", "256GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S23 Ultra", 1559.99, R.drawable.galaxy_s23_ultra_pink, 2023010170L, 1, 9, "01/02/2023", "Samsung Galaxy S23 Ultra là sản phẩm tiêu biểu của dòng S23. Các thông số kỹ thuật hàng đầu bao gồm màn hình Dynamic AMOLED 6,8 inch với tốc độ làm mới 120Hz, bộ xử lý Snapdragon 8 Gen 2, pin 5000mAh, RAM lên đến 12GB và dung lượng lưu trữ 1TB.", new String[]{"512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Galaxy Z Flip 3",  1149, R.drawable.galaxy_z_flip_3_green, 2021010890L, 1, 6, "27/08/2021", "Màn hình 6,7 inch của Galaxy Z Flip 3 uốn quanh trục ngang và có một màn hình nhỏ 1,9 inch ở bên ngoài cho mục đích ngày/giờ/thông báo. Dưới nắp máy, điện thoại cung cấp bộ vi xử lý Qualcomm Snapdragon 888, pin 3300mAh, Android 11 và camera kép ở mặt sau với cảm biến chính 12MP kết hợp với camera selfie 10MP.", new String[]{"256GB", "512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21", 499, R.drawable.galaxy_s21_white, 2021010150L, 1, 4,"29/01/2021", "Thông số kỹ thuật của Samsung Galaxy S21 là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM 8GB kết hợp với dung lượng lưu trữ 128/256GB và pin 4000mAh. Điện thoại có màn hình Dynamic AMOLED 6,2 inch với tốc độ làm mới 120Hz thích ứng. Trong bộ phận máy ảnh, một thiết lập ba cảm biến được trình bày.", new String[]{"128GB", "256GB", "512GB"}));
//
//        List<HomeChildItem> highlight_list = new ArrayList<>();
//        highlight_list.add(new HomeChildItem("Galaxy S21 Ultra", 749, R.drawable.galaxy_s21_ultra_caribbean, 2021010170L, 1, 3, "29/01/2021", "Samsung Galaxy S21 Ultra là sản phẩm tiêu biểu của dòng S21. Đây là điện thoại dòng S đầu tiên hỗ trợ bút S Pen của Samsung. Thông số kỹ thuật là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM lên tới 16GB và dung lượng lưu trữ 512GB. Viên pin 5000mAh giúp duy trì hoạt động của nhà máy điện này. Trong bộ phận máy ảnh, thiết lập bốn camera được cung cấp với hai cảm biến chụp ảnh xa.", new String[]{"256GB", "512GB"}));
//        highlight_list.add(new HomeChildItem("Galaxy Z Fold 3",  1799, R.drawable.galaxy_z_fold_3_phantom_silver, 2021010880L, 1, 5, "11/08/2021", "Samsung Galaxy Z Fold 3 là sản phẩm kế nhiệm của Galaxy Z Fold 2 và đi kèm với màn hình AMOLED 6,2 inch ở mặt trước và màn hình Dynamic AMOLED lớn 7,6 inch với tốc độ làm mới 120Hz khi mở ra. Bên dưới màn hình gập bên trong là camera dưới màn hình đầu tiên của Samsung với cảm biến 4MP. Thông số kỹ thuật cũng bao gồm bộ xử lý Qualcomm Snapdragon 888 dưới nắp máy, pin 4400mAh và thiết lập ba camera ở mặt sau với cảm biến chính 12MP.", new String[]{"256GB", "512GB", "1TB"}  ));
//        highlight_list.add(new HomeChildItem("Galaxy Z Flip 3",  1149, R.drawable.galaxy_z_flip_3_green, 2021010890L, 1, 6, "27/08/2021", "Màn hình 6,7 inch của Galaxy Z Flip 3 uốn quanh trục ngang và có một màn hình nhỏ 1,9 inch ở bên ngoài cho mục đích ngày/giờ/thông báo. Dưới nắp máy, điện thoại cung cấp bộ vi xử lý Qualcomm Snapdragon 888, pin 3300mAh, Android 11 và camera kép ở mặt sau với cảm biến chính 12MP kết hợp với camera selfie 10MP.", new String[]{"256GB", "512GB", "1TB"}));
//        highlight_list.add(new HomeChildItem("Galaxy S21", 499, R.drawable.galaxy_s21_white, 2021010150L, 1, 4,"29/01/2021", "Thông số kỹ thuật của Samsung Galaxy S21 là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM 8GB kết hợp với dung lượng lưu trữ 128/256GB và pin 4000mAh. Điện thoại có màn hình Dynamic AMOLED 6,2 inch với tốc độ làm mới 120Hz thích ứng. Trong bộ phận máy ảnh, một thiết lập ba cảm biến được trình bày.", new String[]{"128GB", "256GB", "512GB"}));
//        highlight_list.add(new HomeChildItem("Galaxy Note 20 Ultra", 599, R.drawable.galaxy_note_20_ultra_white, 2020010840L, 1, 3, "21/08/2020","Thông số kỹ thuật của Samsung Galaxy Note 20 bao gồm chipset Snapdragon 865 và một chip Exynos để phù hợp ở tất cả các khu vực ngoài Hoa Kỳ, đi kèm với 8GB RAM và dung lượng lưu trữ lên tới 256GB và pin 4300mAh. Ở phía sau là thiết lập ba camera từ Galaxy S20. Một màn hình sAMOLED 6,7 inch với tốc độ làm mới 60Hz ở phía trước. S Pen và 5G là tiêu chuẩn.", new String[]{"128GB", "256GB"}));
//

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