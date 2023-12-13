package vlu.mobileproject.login;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import vlu.mobileproject.R;

public class VerifyDialog {

    public interface OnVerifyListener {
        void onVerify(String input);
    }

    public static void showDialog(String phoneNumber, Context context, final OnVerifyListener listener) {
        final Dialog dialog = new Dialog(context, R.style.RoundedDialog); // Apply the custom style
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_verify);

        // Set the dialog window to fill the screen
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        final EditText editText = dialog.findViewById(R.id.editText);
        TextView txtPhone = dialog.findViewById(R.id.txtPhone);
        txtPhone.setText(phoneNumber);
        Button btnVerify = dialog.findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(v -> {
            String input = editText.getText().toString().trim();
            if (!input.isEmpty()) {
                listener.onVerify(input);
                dialog.dismiss();
            } else {
                editText.setError("Please enter a value");
            }
        });

        dialog.show();
    }
}
