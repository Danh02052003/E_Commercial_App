package vlu.mobileproject.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import vlu.mobileproject.R;
import vlu.mobileproject.login.data.LoginParent;

public class ForgotPassword extends LoginParent {
    private TextView textLogin,bttdangnhap;
    private EditText editTextEmailPhone;
    private Button buttonResetPassword;
    private EditText edtNewPassword;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        textLogin = findViewById(R.id.dangNhap);
        editTextEmailPhone = findViewById(R.id.edtEmailPhone);
        buttonResetPassword = findViewById(R.id.bttResetPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        bttdangnhap = findViewById(R.id.bttdangnhap);

        bttdangnhap.setOnClickListener(view -> {
            OpenLoginWithEmailActivity();
        });

        textLogin.setOnClickListener(v -> {
            OpenLoginWithEmailActivity();
        });

        buttonResetPassword.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String emailPhone = editTextEmailPhone.getText().toString().trim();

        if (TextUtils.isEmpty(emailPhone)) {
            Toast.makeText(this, "Please enter your registered phone", Toast.LENGTH_SHORT).show();
            return;
        }

        SendVerificationCode(emailPhone);
    }
}
