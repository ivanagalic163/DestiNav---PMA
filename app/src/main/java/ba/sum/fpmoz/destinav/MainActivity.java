package ba.sum.fpmoz.destinav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void loginUser(View view){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);

        String emailUser = email.getText().toString();
        String passwordUser = password.getText().toString();

        if(emailUser.isEmpty() || passwordUser.isEmpty()){
            Toast.makeText(this, "Input fields are required", Toast.LENGTH_SHORT).show();
            return;
        }else{
            auth.signInWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(MainActivity.this, "Login successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent (MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else{
                        String error = task.getException().getMessage();
                        Log.e("Login error", error);
                        Toast.makeText(MainActivity.this, "Login failed. ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void goRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void goChange(View view){
      Intent intent = new Intent(this, ForgotPassword.class);
      startActivity(intent);
     }
}