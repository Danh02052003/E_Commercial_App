package vlu.mobileproject.login;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    @Override
    public void OnLoginSuccessful() {
        super.OnLoginSuccessful();

        FirebaseUser user2ChangePass = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = edtNewPassword.getText().toString().trim();
        user2ChangePass.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Đổi mật khẩu thành công.", Toast.LENGTH_SHORT).show();
                    } else {
                        Exception exception = task.getException();
                        Toast.makeText(ForgotPassword.this, "Đổi mật khẩu không thành công." + exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
