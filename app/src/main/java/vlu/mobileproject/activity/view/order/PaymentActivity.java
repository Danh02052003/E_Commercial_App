package vlu.mobileproject.activity.view.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;
import vlu.mobileproject.ProductInCartItem;
import vlu.mobileproject.R;
import vlu.mobileproject.data.DeliveryMethod;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;
import vlu.mobileproject.modle.Order;
import vlu.mobileproject.modle.OrderItem;

public class PaymentActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference cartReference, orderReference, orderItemReference;
    private static final String CART_REFERENCE_KEY = "Cart";
    private static final String ORDER_REFERENCE_KEY = "Order";
    private static final String ORDER_ITEM_REFERENCE_KEY = "OrderItem";
    private static final String PRODUCTS_REFERENCE_KEY = "Products_2";
    Map<String, PaymentMethod> paymentMethodMap;
    Map<String, DeliveryMethod> deleveryMethodMap;

    Button btnProceedToOrder;
    ImageButton btnBack;
    EditText shippingAddress, phoneNumber;
    List<ProductInCartItem> inCartSelectedList;
    double otherFees, totalPrice, discount;
    RadioGroup radioGroupPaymentMethod, radioGroupDeliveryMethod;
    RadioButton checkedRadioButtonPaymentMethod, checkedRadioButtonDeliveryMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        auth = FirebaseAuth.getInstance();

        paymentMethodMap = new HashMap<>();
        deleveryMethodMap = new HashMap<>();

        btnProceedToOrder = findViewById(R.id.btnProceedToOrder);
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod);
        radioGroupDeliveryMethod = findViewById(R.id.radioGroupDeliveryMethod);
        cartReference = FirebaseDatabase.getInstance().getReference(CART_REFERENCE_KEY);
        orderReference = FirebaseDatabase.getInstance().getReference(ORDER_REFERENCE_KEY);
        orderItemReference = FirebaseDatabase.getInstance().getReference(ORDER_ITEM_REFERENCE_KEY);
        shippingAddress = findViewById(R.id.shippingAddress);
        phoneNumber = findViewById(R.id.phoneNumber);
        btnBack = findViewById(R.id.btnBack);

        totalPrice = Paper.book().read("totalPrice");
        inCartSelectedList = Paper.book().read("inCartSelectedList");
        if (Paper.book().contains("discount")) {
            discount = Paper.book().read("discount");
        } else {
            discount = 0;
        }
        otherFees = 0;

        PaymentMethod paymentMethod;
        paymentMethodMap.put("Cash on Delivery", PaymentMethod.COD);
        paymentMethodMap.put("Banking", PaymentMethod.BANKING);
        paymentMethodMap.put("Credit Card", PaymentMethod.CREDIT_CARD);
        deleveryMethodMap.put("Normal speed", DeliveryMethod.NORMAL);
        deleveryMethodMap.put("Low cost", DeliveryMethod.LOW_COST);
        deleveryMethodMap.put("High speed", DeliveryMethod.HIGH_SPEED);
        Paper.delete("totalPrice");
        Paper.delete("inCartSelectedList");
        Paper.delete("discount");

        btnProceedToOrder.setOnClickListener(v -> {
            InitOrder(totalPrice, inCartSelectedList);
        });
        btnBack.setOnClickListener(view -> {
            finish();
        });

        phoneNumber.setText(auth.getCurrentUser().getPhoneNumber());
    }

    PaymentMethod getpaymentMethod(String paymentMethod) {
        return paymentMethodMap.get(paymentMethod);
    }

    DeliveryMethod getDeliveryMethod(String deliveryMethod) {
        return deleveryMethodMap.get(deliveryMethod);
    }

    void InitOrder(double totalPrice, List<ProductInCartItem> CheckedItems) {
        if (shippingAddress.getText().length() == 0 || phoneNumber.getText().length() == 0) {
            Toast.makeText(PaymentActivity.this, "Hãy nhập đầy đủ thông tin" , Toast.LENGTH_SHORT).show();
            return;
        }

        String UserID = auth.getCurrentUser().getUid();
        String newOrderKey = orderReference.push().getKey();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        checkedRadioButtonPaymentMethod = findViewById(radioGroupPaymentMethod.getCheckedRadioButtonId());
        checkedRadioButtonDeliveryMethod = findViewById(radioGroupDeliveryMethod.getCheckedRadioButtonId());
        PaymentMethod paymentMethod = getpaymentMethod(checkedRadioButtonPaymentMethod.getTag().toString());
        DeliveryMethod deliveryMethod = getDeliveryMethod(checkedRadioButtonDeliveryMethod.getTag().toString());

        Order newOrder = new Order(UserID, newOrderKey, totalPrice, discount, otherFees, currentDate, DeliveryStatus.PENDING, paymentMethod, deliveryMethod, shippingAddress.getText().toString(), phoneNumber.getText().toString());

        orderReference.child(newOrderKey).setValue(newOrder).addOnCompleteListener(taskAddOrder -> {
            if (taskAddOrder.isSuccessful()) {
                for (ProductInCartItem item : CheckedItems) {
                    String ProductID = item.getProductID();
                    String ProductOption = item.getProductOption();
                    int ProductQuantity = item.getProductQuantity();
                    String newOrderItemID = orderItemReference.push().getKey();
                    OrderItem neworderItem = new OrderItem(newOrderKey, ProductID, ProductOption, ProductQuantity);
                    orderItemReference.child(newOrderItemID).setValue(neworderItem).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (int i = 0; i < inCartSelectedList.size(); i++) {
                                cartReference.child(inCartSelectedList.get(i).getCartItemID()).removeValue();
                            }
                        } else {
                            Toast.makeText(PaymentActivity.this, "không thể thêm kiện hàng" + neworderItem.getProductName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Toast.makeText(PaymentActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentActivity.this, OrderActivity.class);
                Bundle bundle = new Bundle();
                finish();
                bundle.putString("OrderID", newOrderKey);
                intent.putExtras(bundle);
                startActivity(intent);

            } else {
                Toast.makeText(PaymentActivity.this, "Đặt không hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });


    }


}