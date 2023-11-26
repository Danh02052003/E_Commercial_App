package vlu.mobileproject.login;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;
import vlu.mobileproject.R;

public class StartupActivity extends AppCompatActivity {
    FirebaseAuth mAuthLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuthLog=FirebaseAuth.getInstance();

        Paper.init(this);
       // Paper.book().destroy();

        String UserEmail = Paper.book().read("UserEmailKey");
        String UserPass = Paper.book().read("UserPassKey");

        if (UserEmail != "" && UserPass != "") {
            if (!TextUtils.isEmpty(UserEmail) && !TextUtils.isEmpty(UserPass)) {
                AllowUserAccess(UserEmail, UserPass);
            }
        }
    }

    public void onStartButtonClick(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    void AllowUserAccess(String email, String password) {
        mAuthLog.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UserManager.getInstance().setUserEmail(email);
                            Toast.makeText(StartupActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StartupActivity.this,vlu.mobileproject.activity.view.home.MainActivity.class);

                            // Pass user-specific data if needed
                            intent.putExtra("user_email", email);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(StartupActivity.this, "Mật khẩu hoặc Email không đúng.",
                                    Toast.LENGTH_SHORT).show();
                            Paper.book().destroy();
                        }
                    }
                });
    }
}

