package vlu.mobileproject.login;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vlu.mobileproject.R;

public class VerifyDialog {

    public interface OnVerifyListener {
        void onVerify(String input);
    }

    public static void showDialog(Context context, final OnVerifyListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_verify);
        dialog.setTitle("Verification");

        final EditText editText = dialog.findViewById(R.id.editText);
        Button btnVerify = dialog.findViewById(R.id.btnVerify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString().trim();
                if (!input.isEmpty()) {
                    listener.onVerify(input);
                    dialog.dismiss();
                } else {
                    editText.setError("Please enter a value");
                }
            }
        });

        dialog.show();
    }
}
