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
 * Use the {@link IphoneFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IphoneFrag extends Fragment {
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

    public IphoneFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IphoneFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static IphoneFrag newInstance(String param1, String param2) {
        IphoneFrag fragment = new IphoneFrag();
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
        View view = inflater.inflate(R.layout.fragment_iphone, container, false);
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

        List<HomeChildItem> forYou_list = new ArrayList<>();
        forYou_list.add(new HomeChildItem("iPhone 13 Pro Max", 1099, R.drawable.iphone_13_pro_max_sierra_blue, 2021020930L, 2, 12, "24/09/2021", "iPhone 13 Pro Max là mẫu lớn nhất và đắt nhất trong dòng điện thoại thông minh năm 2021 của Apple và có màn hình Super Retina XDR 6,7 inch với độ phân giải 1284 x 2778 pixel. Giống như iPhone 13 Pro nhỏ hơn, nó được trang bị chipset A15 Bionic mới nhất của Apple và đi kèm với bộ nhớ trong lên đến 1TB.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
        forYou_list.add(new HomeChildItem("Iphone 14 Pro Max", 1299, R.drawable.iphone_14_pro_max_deep_purple, 2022020930L, 2, 9, "07/09/2022", "iPhone 14 Plus đi kèm với màn hình OLED 6,7 inch với tốc độ làm mới 120Hz và bộ vi xử lý A16 Bionic cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ $1099.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
        forYou_list.add(new HomeChildItem("iPhone 12 Pro Max", 849, R.drawable.iphone_12_pro_max_azul, 2020021130L, 2, 4, "13/11/2020", "iPhone 12 Pro Max có màn hình 6,7 inch (lớn hơn một chút so với 6,5 inch trên iPhone 11 Pro Max) và giá cơ bản của nó là 1.100 USD. Nó có cùng lựa chọn màu sắc: bạc, than chì, vàng và tùy chọn màu xanh lam mới và nó cũng được cung cấp với cùng dung lượng lưu trữ: 128GB, 256GB và 512GB.", new String[]{"128GB", "256GB", "512GB"}));
        forYou_list.add(new HomeChildItem("Iphone 14 Pro", 949, R.drawable.iphone_14_pro_white, 2022020920L, 2, 13, "07/09/2022", "iPhone 14 Pro đi kèm với màn hình OLED 6,1 inch với tốc độ làm mới 120Hz và bộ xử lý Bionic A16 cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ 949 đô la.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
        forYou_list.add(new HomeChildItem("Iphone 14", 899, R.drawable.iphone_14_blue, 2022020910L, 2, 12, "16/09/2022", "iPhone 14 đi kèm với màn hình OLED 6,1 inch và bộ xử lý Bionic A15 cải tiến của Apple. Ở mặt sau có thiết lập camera kép với camera chính 12MP và cảm biến siêu rộng 12MP. Giá bắt đầu từ 899 đô la.", new String[]{"128GB", "256GB", "512GB"}));
        forYou_list.add(new HomeChildItem("Iphone 11 Pro Max", 509, R.drawable.iphone_11_pro_max_gray, 2019020930L, 2, 13, "10/09/2019", "Dòng iPhone 11 không nhận được nâng cấp lớn về mặt thẩm mỹ mà thay vào đó mang đến một loạt nâng cấp về hiệu suất. IPhone 11 Pro Max bao gồm hệ thống ba camera của Apple, màn hình Super Retina XDR 6,5 inch, chip A12 Bionic và pin lớn hơn cho thời lượng pin cả ngày.", new String[]{"128GB", "256GB", "512GB"}));

        List<HomeChildItem> highlight_list = new ArrayList<>();
        highlight_list.add(new HomeChildItem("Iphone 11 Pro Max", 509, R.drawable.iphone_11_pro_max_gray, 2019020930L, 2, 13, "10/09/2019", "Dòng iPhone 11 không nhận được nâng cấp lớn về mặt thẩm mỹ mà thay vào đó mang đến một loạt nâng cấp về hiệu suất. IPhone 11 Pro Max bao gồm hệ thống ba camera của Apple, màn hình Super Retina XDR 6,5 inch, chip A12 Bionic và pin lớn hơn cho thời lượng pin cả ngày.", new String[]{"128GB", "256GB", "512GB"}));
        highlight_list.add(new HomeChildItem("Iphone 14 Pro", 949, R.drawable.iphone_14_pro_white, 2022020920L, 2, 13, "07/09/2022", "iPhone 14 Pro đi kèm với màn hình OLED 6,1 inch với tốc độ làm mới 120Hz và bộ xử lý Bionic A16 cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ 949 đô la.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
        highlight_list.add(new HomeChildItem("Iphone 14", 899, R.drawable.iphone_14_blue, 2022020910L, 2, 12, "16/09/2022", "iPhone 14 đi kèm với màn hình OLED 6,1 inch và bộ xử lý Bionic A15 cải tiến của Apple. Ở mặt sau có thiết lập camera kép với camera chính 12MP và cảm biến siêu rộng 12MP. Giá bắt đầu từ 899 đô la.", new String[]{"128GB", "256GB", "512GB"}));
        highlight_list.add(new HomeChildItem("Iphone 14 Pro Max", 1299, R.drawable.iphone_14_pro_max_deep_purple, 2022020930L, 2, 9, "07/09/2022", "iPhone 14 Plus đi kèm với màn hình OLED 6,7 inch với tốc độ làm mới 120Hz và bộ vi xử lý A16 Bionic cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ $1099.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
        highlight_list.add(new HomeChildItem("iPhone 12 Pro Max", 849, R.drawable.iphone_12_pro_max_azul, 2020021130L, 2, 4, "13/11/2020", "iPhone 12 Pro Max có màn hình 6,7 inch (lớn hơn một chút so với 6,5 inch trên iPhone 11 Pro Max) và giá cơ bản của nó là 1.100 USD. Nó có cùng lựa chọn màu sắc: bạc, than chì, vàng và tùy chọn màu xanh lam mới và nó cũng được cung cấp với cùng dung lượng lưu trữ: 128GB, 256GB và 512GB.", new String[]{"128GB", "256GB", "512GB"}));
        highlight_list.add(new HomeChildItem("iPhone 13 Pro Max", 1099, R.drawable.iphone_13_pro_max_sierra_blue, 2021020930L, 2, 12, "24/09/2021", "iPhone 13 Pro Max là mẫu lớn nhất và đắt nhất trong dòng điện thoại thông minh năm 2021 của Apple và có màn hình Super Retina XDR 6,7 inch với độ phân giải 1284 x 2778 pixel. Giống như iPhone 13 Pro nhỏ hơn, nó được trang bị chipset A15 Bionic mới nhất của Apple và đi kèm với bộ nhớ trong lên đến 1TB.", new String[]{"128GB", "256GB", "512GB", "1TB"}));

//
//        parentItemList = new ArrayList<>();
//        parentItemList.add(new HomeParentItem("Dành cho bạn", forYou_list));
//        parentItemList.add(new HomeParentItem("Sản phẩm nổi bật", highlight_list));
//
//        //Dẫn xuất và gán ParentAdapter cho RecycleView cha
//        parentAdapter = new HomeParentAdapter(parentItemList);
//        rvCategory.setAdapter(parentAdapter);
//

        return view;
    }
}