package vlu.mobileproject.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;

import io.paperdb.Paper;
import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.home.MainActivity;

public class StartupActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firebaseAuth = FirebaseAuth.getInstance();

        Paper.init(this);
    }

    public void onStartButtonClick(View view) {
        String UserEmail = Paper.book().read("UserEmailKey");
        String UserPass = Paper.book().read("UserPassKey");
        PhoneAuthCredential credential = Paper.book().read("credential");
        signInWithPhoneAuthCredential(credential);

        if (!TextUtils.isEmpty(UserEmail) && !TextUtils.isEmpty(UserPass) || !TextUtils.isEmpty(credential.zzd())) {
            if (!TextUtils.isEmpty(UserEmail) && !TextUtils.isEmpty(UserPass)) {
                AllowUserAccess(UserEmail, UserPass);
                return;
            }

        } else {
            OpenLoginActivity();
        }
    }

    void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnFailureListener(e -> {
            Toast.makeText(StartupActivity.this, "Đặng nhập Ko công" + e, Toast.LENGTH_SHORT).show();
           // FirebaseAuthSettings
        })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(StartupActivity.this, "Đặng nhập thành công", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(StartupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    void OpenLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    void AllowUserAccess(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserManager.getInstance().setUserEmail(email);
                    Toast.makeText(StartupActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StartupActivity.this, vlu.mobileproject.activity.view.home.MainActivity.class);

                    // Pass user-specific data if needed
                    intent.putExtra("user_email", email);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(StartupActivity.this, "Mật khẩu đã bị sửa đổi hoặc tài khoản đã bị xóa.", Toast.LENGTH_SHORT).show();
                    Paper.book().destroy();
                    OpenLoginActivity();
                }
            }
        });
    }
}

