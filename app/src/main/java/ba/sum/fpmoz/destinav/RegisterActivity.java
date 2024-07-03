package ba.sum.fpmoz.destinav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.destinav.models.User;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void registerUser(View view){
        EditText firstName = findViewById(R.id.first_name);
        EditText lastName = findViewById(R.id.last_name);
        EditText email = findViewById(R.id.emailUser);
        EditText password = findViewById(R.id.passwordUser);

        String name = firstName.getText().toString();
        String surname = lastName.getText().toString();
        String emailUser = email.getText().toString();
        String passwordUser = password.getText().toString();

        if(name.isEmpty() || surname.isEmpty() || emailUser.isEmpty() || passwordUser.isEmpty()){
            Toast.makeText(this, "All input fields are required", Toast.LENGTH_SHORT).show();
            return;
        }else{
            firebaseAuth.createUserWithEmailAndPassword(emailUser,passwordUser).addOnCompleteListener(this,task->{
                if(task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String role = "user";
                    User member = new User(name,surname,emailUser,passwordUser, role);
                    String userId = user.getUid();

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
                    databaseReference.child(user.getUid()).setValue(member);
                    Toast.makeText(this, "Registration successful. Thank you!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    String error = task.getException().getMessage();
                    Log.e("RegisterActivity", "Registration failed" + task.getException().getMessage());
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void goLogin(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goChange(View view){
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
    }
}