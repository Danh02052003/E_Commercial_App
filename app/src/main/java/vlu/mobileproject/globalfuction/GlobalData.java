package vlu.mobileproject.globalfuction;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.AllFrag;
import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.HomeParentItem;
import vlu.mobileproject.R;
import vlu.mobileproject.adapter.HomeParentAdapter;
import vlu.mobileproject.modle.Product;
import vlu.mobileproject.modle.Products;

public class GlobalData {
    public interface Callback{
        void onCompleted(List<Products> foryou_list, List<Products> highlight_list);
    }
    public static List<Products> forYou_list;
    public static List<Products> highlight_list;
    static List<Products> products = new ArrayList<>();

    public static void initData(Context context, Callback callback) {

        forYou_list = new ArrayList<>();
        highlight_list = new ArrayList<>();


//        forYou_list.add(new HomeChildItem("Galaxy S23 Ultra", 1559.99, R.drawable.galaxy_s23_ultra_pink, 2023010170L, 1, 9, "01/02/2023", "Samsung Galaxy S23 Ultra là sản phẩm tiêu biểu của dòng S23. Các thông số kỹ thuật hàng đầu bao gồm màn hình Dynamic AMOLED 6,8 inch với tốc độ làm mới 120Hz, bộ xử lý Snapdragon 8 Gen 2, pin 5000mAh, RAM lên đến 12GB và dung lượng lưu trữ 1TB.", new String[]{"512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S23 Plus", 1119.99, R.drawable.galaxy_s23_plus_graphite, 2023010160L, 1, 7, "01/02/2023", "Samsung Galaxy S23+ là phiên bản lớn hơn của Galaxy S23 thông thường với màn hình Dynamic AMOLED 6,6 inch với tốc độ làm mới 120Hz. Thông số kỹ thuật cũng bao gồm bộ vi xử lý Qualcomm Snapdragon 8 Gen 2, pin 4700mAh và thiết lập 3 camera ở mặt sau.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21 Plus", 699, R.drawable.galaxy_s21_plus_purple, 2021010160L, 1, 4, "29/01/2021", "Samsung Galaxy S21+ là phiên bản lớn hơn của Galaxy S21 thông thường. Thông số kỹ thuật khôn ngoan, điện thoại mang chipset Snapdragon 888, khả năng 5G, dung lượng lưu trữ 128/256GB và pin 4800mAh. Màn hình Dynamic AMOLED 6,7 inch có tốc độ làm mới 120Hz thích ứng. Có một thiết lập ba camera với cảm biến chính 12MP ở mặt sau. Giá khởi điểm của Samsung Galaxy S21+ là 1000 USD.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21 Ultra", 749, R.drawable.galaxy_s21_ultra_caribbean, 2021010170L, 1, 3, "29/01/2021", "Samsung Galaxy S21 Ultra là sản phẩm tiêu biểu của dòng S21. Đây là điện thoại dòng S đầu tiên hỗ trợ bút S Pen của Samsung. Thông số kỹ thuật là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM lên tới 16GB và dung lượng lưu trữ 512GB. Viên pin 5000mAh giúp duy trì hoạt động của nhà máy điện này. Trong bộ phận máy ảnh, thiết lập bốn camera được cung cấp với hai cảm biến chụp ảnh xa.", new String[]{"256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy Z Fold 3",  1799, R.drawable.galaxy_z_fold_3_phantom_silver, 2021010880L, 1, 5, "11/08/2021", "Samsung Galaxy Z Fold 3 là sản phẩm kế nhiệm của Galaxy Z Fold 2 và đi kèm với màn hình AMOLED 6,2 inch ở mặt trước và màn hình Dynamic AMOLED lớn 7,6 inch với tốc độ làm mới 120Hz khi mở ra. Bên dưới màn hình gập bên trong là camera dưới màn hình đầu tiên của Samsung với cảm biến 4MP. Thông số kỹ thuật cũng bao gồm bộ xử lý Qualcomm Snapdragon 888 dưới nắp máy, pin 4400mAh và thiết lập ba camera ở mặt sau với cảm biến chính 12MP.", new String[]{"256GB", "512GB", "1TB"}  ));
//        forYou_list.add(new HomeChildItem("Galaxy Z Flip 3",  1149, R.drawable.galaxy_z_flip_3_green, 2021010890L, 1, 6, "27/08/2021", "Màn hình 6,7 inch của Galaxy Z Flip 3 uốn quanh trục ngang và có một màn hình nhỏ 1,9 inch ở bên ngoài cho mục đích ngày/giờ/thông báo. Dưới nắp máy, điện thoại cung cấp bộ vi xử lý Qualcomm Snapdragon 888, pin 3300mAh, Android 11 và camera kép ở mặt sau với cảm biến chính 12MP kết hợp với camera selfie 10MP.", new String[]{"256GB", "512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Galaxy S21", 499, R.drawable.galaxy_s21_white, 2021010150L, 1, 4,"29/01/2021", "Thông số kỹ thuật của Samsung Galaxy S21 là hàng đầu bao gồm chipset Snapdragon 888, khả năng 5G, RAM 8GB kết hợp với dung lượng lưu trữ 128/256GB và pin 4000mAh. Điện thoại có màn hình Dynamic AMOLED 6,2 inch với tốc độ làm mới 120Hz thích ứng. Trong bộ phận máy ảnh, một thiết lập ba cảm biến được trình bày.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Galaxy Note 20 Ultra", 599, R.drawable.galaxy_note_20_ultra_white, 2020010840L, 1, 3, "21/08/2020","Thông số kỹ thuật của Samsung Galaxy Note 20 bao gồm chipset Snapdragon 865 và một chip Exynos để phù hợp ở tất cả các khu vực ngoài Hoa Kỳ, đi kèm với 8GB RAM và dung lượng lưu trữ lên tới 256GB và pin 4300mAh. Ở phía sau là thiết lập ba camera từ Galaxy S20. Một màn hình sAMOLED 6,7 inch với tốc độ làm mới 60Hz ở phía trước. S Pen và 5G là tiêu chuẩn.", new String[]{"128GB", "256GB"}));
//        forYou_list.add(new HomeChildItem("iPhone 13 Pro Max", 1099, R.drawable.iphone_13_pro_max_sierra_blue, 2021020930L, 2, 12, "24/09/2021", "iPhone 13 Pro Max là mẫu lớn nhất và đắt nhất trong dòng điện thoại thông minh năm 2021 của Apple và có màn hình Super Retina XDR 6,7 inch với độ phân giải 1284 x 2778 pixel. Giống như iPhone 13 Pro nhỏ hơn, nó được trang bị chipset A15 Bionic mới nhất của Apple và đi kèm với bộ nhớ trong lên đến 1TB.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("iPhone 12 Pro Max", 849, R.drawable.iphone_12_pro_max_azul, 2020021130L, 2, 4, "13/11/2020", "iPhone 12 Pro Max có màn hình 6,7 inch (lớn hơn một chút so với 6,5 inch trên iPhone 11 Pro Max) và giá cơ bản của nó là 1.100 USD. Nó có cùng lựa chọn màu sắc: bạc, than chì, vàng và tùy chọn màu xanh lam mới và nó cũng được cung cấp với cùng dung lượng lưu trữ: 128GB, 256GB và 512GB.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Iphone 14 Pro Max", 1299, R.drawable.iphone_14_pro_max_deep_purple, 2022020930L, 2, 9, "07/09/2022", "iPhone 14 Plus đi kèm với màn hình OLED 6,7 inch với tốc độ làm mới 120Hz và bộ vi xử lý A16 Bionic cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ $1099.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Iphone 14", 899, R.drawable.iphone_14_blue, 2022020910L, 2, 12, "16/09/2022", "iPhone 14 đi kèm với màn hình OLED 6,1 inch và bộ xử lý Bionic A15 cải tiến của Apple. Ở mặt sau có thiết lập camera kép với camera chính 12MP và cảm biến siêu rộng 12MP. Giá bắt đầu từ 899 đô la.", new String[]{"128GB", "256GB", "512GB"}));
//        forYou_list.add(new HomeChildItem("Iphone 14 Pro", 949, R.drawable.iphone_14_pro_white, 2022020920L, 2, 13, "07/09/2022", "iPhone 14 Pro đi kèm với màn hình OLED 6,1 inch với tốc độ làm mới 120Hz và bộ xử lý Bionic A16 cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ 949 đô la.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        forYou_list.add(new HomeChildItem("Iphone 11 Pro Max", 509, R.drawable.iphone_11_pro_max_gray, 2019020930L, 2, 13, "10/09/2019", "Dòng iPhone 11 không nhận được nâng cấp lớn về mặt thẩm mỹ mà thay vào đó mang đến một loạt nâng cấp về hiệu suất. IPhone 11 Pro Max bao gồm hệ thống ba camera của Apple, màn hình Super Retina XDR 6,5 inch, chip A12 Bionic và pin lớn hơn cho thời lượng pin cả ngày.", new String[]{"128GB", "256GB", "512GB"}));
//        highlight_list.add(new HomeChildItem("iPhone 13 Pro Max", 1099, R.drawable.iphone_13_pro_max_sierra_blue, 2021020930L, 2, 12, "24/09/2021", "iPhone 13 Pro Max là mẫu lớn nhất và đắt nhất trong dòng điện thoại thông minh năm 2021 của Apple và có màn hình Super Retina XDR 6,7 inch với độ phân giải 1284 x 2778 pixel. Giống như iPhone 13 Pro nhỏ hơn, nó được trang bị chipset A15 Bionic mới nhất của Apple và đi kèm với bộ nhớ trong lên đến 1TB.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        highlight_list.add(new HomeChildItem("iPhone 12 Pro Max", 849, R.drawable.iphone_12_pro_max_azul, 2020021130L, 2, 4, "13/11/2020", "iPhone 12 Pro Max có màn hình 6,7 inch (lớn hơn một chút so với 6,5 inch trên iPhone 11 Pro Max) và giá cơ bản của nó là 1.100 USD. Nó có cùng lựa chọn màu sắc: bạc, than chì, vàng và tùy chọn màu xanh lam mới và nó cũng được cung cấp với cùng dung lượng lưu trữ: 128GB, 256GB và 512GB.", new String[]{"128GB", "256GB", "512GB"}));
//        highlight_list.add(new HomeChildItem("Iphone 14 Pro Max", 1299, R.drawable.iphone_14_pro_max_deep_purple, 2022020930L, 2, 9, "07/09/2022", "iPhone 14 Plus đi kèm với màn hình OLED 6,7 inch với tốc độ làm mới 120Hz và bộ vi xử lý A16 Bionic cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ $1099.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        highlight_list.add(new HomeChildItem("Iphone 14", 899, R.drawable.iphone_14_blue, 2022020910L, 2, 12, "16/09/2022", "iPhone 14 đi kèm với màn hình OLED 6,1 inch và bộ xử lý Bionic A15 cải tiến của Apple. Ở mặt sau có thiết lập camera kép với camera chính 12MP và cảm biến siêu rộng 12MP. Giá bắt đầu từ 899 đô la.", new String[]{"128GB", "256GB", "512GB"}));
//        highlight_list.add(new HomeChildItem("Iphone 14 Pro", 949, R.drawable.iphone_14_pro_white, 2022020920L, 2, 13, "07/09/2022", "iPhone 14 Pro đi kèm với màn hình OLED 6,1 inch với tốc độ làm mới 120Hz và bộ xử lý Bionic A16 cải tiến của Apple. Ở mặt sau có thiết lập 3 camera với camera chính 48MP. Giá bắt đầu từ 949 đô la.", new String[]{"128GB", "256GB", "512GB", "1TB"}));
//        highlight_list.add(new HomeChildItem("Iphone 11 Pro Max", 509, R.drawable.iphone_11_pro_max_gray, 2019020930L, 2, 13, "10/09/2019", "Dòng iPhone 11 không nhận được nâng cấp lớn về mặt thẩm mỹ mà thay vào đó mang đến một loạt nâng cấp về hiệu suất. IPhone 11 Pro Max bao gồm hệ thống ba camera của Apple, màn hình Super Retina XDR 6,5 inch, chip A12 Bionic và pin lớn hơn cho thời lượng pin cả ngày.", new String[]{"128GB", "256GB", "512GB"}));
//
        FirebaseApp.initializeApp(context);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://e-commerce-73482-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference productRef = database.getReference("Products");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Products product = dataSnapshot.getValue(Products.class);
                    Log.d("Product debug: ", "Product " + String.valueOf(product.getProduct_id()));
                    i++;
//                    String imgUrl = dataSnapshot.child("product_img").getValue().toString();

                    product.setProduct_img("product_image/samsung/"+product.getProduct_img());
                    products.add(product);
                }
                separateData();
                callback.onCompleted(forYou_list, highlight_list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    static void separateData() {
        for (int i = 0; i < products.size(); i++) {
            if (i < products.size() / 2)
                forYou_list.add(products.get(i));
            else highlight_list.add(products.get(i));
        }
    };




}
