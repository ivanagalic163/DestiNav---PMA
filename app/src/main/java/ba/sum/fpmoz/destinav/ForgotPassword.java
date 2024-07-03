package ba.sum.fpmoz.destinav;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPassword extends AppCompatActivity{
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        auth = FirebaseAuth.getInstance();

    }

    public void confirmPassword(View view) {
        EditText email = findViewById(R.id.emailUser);
        String emailUser = email.getText().toString();
        if (emailUser.isEmpty()) {
            Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show();
        } else {
            auth.sendPasswordResetEmail(emailUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "Email for reset password has been sent", Toast.LENGTH_SHORT).show();
                        backToLogin(view);
                    } else {
                        Toast.makeText(ForgotPassword.this, "Password reset email failed to send", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void backToLogin(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}