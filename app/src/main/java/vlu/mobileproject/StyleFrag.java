package vlu.mobileproject;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StyleFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StyleFrag extends Fragment {
    RecyclerView rvCategory;
    RecyclerView rvChildList;
    private HomeParentAdapter parentAdapter;
    private List<HomeParentItem> parentItemList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StyleFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StyleFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static StyleFrag newInstance(String param1, String param2) {
        StyleFrag fragment = new StyleFrag();
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
//        rvCategory = view.findViewById(R.id.rvCategory);
//
//        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
//
//
//        View anotherLayout = LayoutInflater.from(getContext()).inflate(R.layout.home_parent_item, null);
//
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//
//        rvChildList = anotherLayout.findViewById(R.id.rvChildList);
//        rvChildList.setLayoutManager(layoutManager);
//
//        List<HomeChildItem> forYou_list = new ArrayList<>();
//        forYou_list.add(new HomeChildItem("Galaxy S23 Ultra","Samsung", "$1559,99", R.drawable.galaxy_s23_ultra_pink, 2023010170L, 1, 9, "01/02/2023", "Samsung Galaxy S23 Ultra là sản phẩm tiêu biểu của dòng S23. Các thông số kỹ thuật hàng đầu bao gồm màn hình Dynamic AMOLED 6,8 inch với tốc độ làm mới 120Hz, bộ xử lý Snapdragon 8 Gen 2, pin 5000mAh, RAM lên đến 12GB và dung lượng lưu trữ 1TB.", new String[]{"512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S23 Plus","Samsung", "$1119.99", R.drawable.galaxy_s23_plus_graphite, 2023010160L, 1, 7, "01/02/2023", "Samsung Galaxy S23+ là phiên bản lớn hơn của Galaxy S23 thông thường với màn hình Dynamic AMOLED 6,6 inch với tốc độ làm mới 120Hz. Thông số kỹ thuật cũng bao gồm bộ vi xử lý Qualcomm Snapdragon 8 Gen 2, pin 4700mAh và thiết lập 3 camera ở mặt sau.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21 Plus","Samsung", "$699", R.drawable.galaxy_s21_plus_purple, 2021010160L, 1, 4, "29/01/2021", "Samsung Galaxy S21+ là phiên bản lớn hơn của Galaxy S21 thông thường. Thông số kỹ thuật khôn ngoan, điện thoại mang chipset Snapdragon 888, khả năng 5G, dung lượng lưu trữ 128/256GB và pin 4800mAh. Màn hình Dynamic AMOLED 6,7 inch có tốc độ làm mới 120Hz thích ứng. Có một thiết lập ba camera với cảm biến chính 12MP ở mặt sau. Giá khởi điểm của Samsung Galaxy S21+ là 1000 USD.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21 Ultra","Samsung", "$749", R.drawable.galaxy_s21_ultra_caribbean, 2021010170L, 1, 3, "29/01/2023", "Samsung Galaxy S21 Ultra là sản phẩm tiêu biểu của dòng S21. Đây là điện thoại dòng S đầu tiên hỗ trợ bút S Pen của Samsung. Thông số kỹ thuật là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM lên tới 16GB và dung lượng lưu trữ 512GB. Viên pin 5000mAh giúp duy trì hoạt động của nhà máy điện này. Trong bộ phận máy ảnh, thiết lập bốn camera được cung cấp với hai cảm biến chụp ảnh xa.", new String[]{"256GB", "512GB"}));
//
//        List<HomeChildItem> highlight_list = new ArrayList<>();
//        highlight_list.add(new HomeChildItem("iPhone 13 Pro Max","iPhone", "$1099", R.drawable.iphone_13_pro_max_sierra_blue, 2021020930L, 2, 12, "24/09/2021", "iPhone 13 Pro Max là mẫu lớn nhất và đắt nhất trong dòng điện thoại thông minh năm 2021 của Apple và có màn hình Super Retina XDR 6,7 inch với độ phân giải 1284 x 2778 pixel. Giống như iPhone 13 Pro nhỏ hơn, nó được trang bị chipset A15 Bionic mới nhất của Apple và đi kèm với bộ nhớ trong lên đến 1TB.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        highlight_list.add(new HomeChildItem("iPhone 12 Pro Max","iPhone", "$849", R.drawable.iphone_12_pro_max_azul, 2020021130L, 2, 4, "13/11/2020", "iPhone 12 Pro Max có màn hình 6,7 inch (lớn hơn một chút so với 6,5 inch trên iPhone 11 Pro Max) và giá cơ bản của nó là 1.100 USD. Nó có cùng lựa chọn màu sắc: bạc, than chì, vàng và tùy chọn màu xanh lam mới và nó cũng được cung cấp với cùng dung lượng lưu trữ: 128GB, 256GB và 512GB.", new String[]{"128GB", "256GB", "512GB"}));
//        highlight_list.add(new HomeChildItem("Iphone 14 Pro Max","iPhone", "$1299", R.drawable.iphone_14_pro_max_deep_purple, 2022020930L, 2, 9, "07/09/2022", "iPhone 14 Plus đi kèm với màn hình OLED 6,7 inch với tốc độ làm mới 120Hz và bộ vi xử lý A16 Bionic cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ $1099.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        highlight_list.add(new HomeChildItem("Iphone 14","iPhone", "$799", R.drawable.iphone_14_blue, 2022020910L, 2, 12, "16/09/2022", "IPhone 14 đi kèm với màn hình OLED 6,1 inch và bộ xử lý Bionic A15 cải tiến của Apple. Ở mặt sau có thiết lập camera kép với camera chính 12MP và cảm biến siêu rộng 12MP. Giá bắt đầu từ 799 đô la.", new String[]{"128GB", "256GB", "512GB"}));
//
//        parentItemList = new ArrayList<>();
//        parentItemList.add(new HomeParentItem("Sản phẩm nổi bật", highlight_list));
//        parentItemList.add(new HomeParentItem("Dành cho bạn", forYou_list));
//
//        //Dẫn xuất và gán ParentAdapter cho RecycleView cha
//        parentAdapter = new HomeParentAdapter(parentItemList);
//        rvCategory.setAdapter(parentAdapter);

        return view;
    }
}