package vlu.mobileproject.data;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.adapter.OrderHistoryAdapter;
import vlu.mobileproject.modle.Discount;

public class Animation {
    public interface IAnimation {
        public static void animateChange(Object... parameters) {
            // Your code here
            for (Object parameter : parameters) {
                // Process each parameter as needed
                System.out.println(parameter);
            }
        }

        public static void SetAnimateChange() {

        }
    }

    int currentIndex = 0;
    private Handler handler = new Handler();
    List<Discount> DiscountList = new ArrayList<>();
    private Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            if (DiscountList.size() == 0) {
                handler.postDelayed(this, 500);
                return;
            }
            IAnimation.SetAnimateChange();
            if (currentIndex < DiscountList.size()) {
                currentIndex++;
            } else {
                currentIndex = 0;
            }

            handler.postDelayed(this, 4000);
        }
    };

}
