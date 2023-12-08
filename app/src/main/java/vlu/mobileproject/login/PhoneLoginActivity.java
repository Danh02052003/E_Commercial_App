package vlu.mobileproject.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.login.data.LoginParent;

public class PhoneLoginActivity extends LoginParent {

    EditText userPhoneNumber;
    Button loginBtn;
    CheckBox rememberMe;
    FirebaseAuth firebaseAuth;
    TextView taotaikhoan, loginWithEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        userPhoneNumber = findViewById(R.id.userPhoneNumber);
        loginWithEmail = findViewById(R.id.loginWithEmail);
        loginBtn = findViewById(R.id.loginbutton);
        rememberMe = findViewById(R.id.RememberUser);
        firebaseAuth = FirebaseAuth.getInstance();

        loginWithEmail.setOnClickListener(v -> {
            OpenLoginWithEmailActivity();
        });

        loginBtn.setOnClickListener(v -> loginUser());

        taotaikhoan = findViewById(R.id.taotaikhoan);
        taotaikhoan.setOnClickListener(view -> {
            OpenSignupActivity();
        });
    }

    @Override
    protected void loginUser() {
        super.loginUser();
        final String phone = userPhoneNumber.getText().toString().trim();
        SendVerificationCode(phone);
    }

    @Override
    public void OnLoginSuccessful() {
        super.OnLoginSuccessful();

        Paper.book().write("RememberUser", rememberMe.isChecked());
    }
}