package ba.sum.fpmoz.destinav;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText oldPassword;
    EditText newPassword;
    private Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        auth = FirebaseAuth.getInstance();
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.passwordUser);
        changePassword = findViewById(R.id.changePassword);


    }


    public void confirmPassword(View view){
        String oldPass = oldPassword.getText().toString();
        String newPass = newPassword.getText().toString();

        if(oldPass.isEmpty() || newPass.isEmpty()){
            Toast.makeText(this, "Input fields are required", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseUser user = auth.getCurrentUser();
            if(user != null){
                auth.signInWithEmailAndPassword(user.getEmail(), oldPass).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        user.updatePassword(newPass).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(this, "Password change failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(this, "Its not your old password", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    public void goHome(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToAccount(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

}