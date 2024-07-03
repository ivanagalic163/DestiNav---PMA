package ba.sum.fpmoz.destinav;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
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


public class AccountActivity extends AppCompatActivity {
    private EditText userEmail;
    private EditText firstName;
    private EditText lastName;
    private FirebaseAuth auth;


    private EditText userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        userEmail = findViewById(R.id.emailUser);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

        if(user != null){
            String userId = user.getUid();
            database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String email = snapshot.child("email").getValue(String.class);
                    String name = snapshot.child("firstName").getValue(String.class);
                    String surname = snapshot.child("lastName").getValue(String.class);
                    userEmail.setHint(email);
                    firstName.setHint(name);
                    lastName.setHint(surname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    signOut(null);
                }
            });
        }

        if(user != null){
            String userId = user.getUid();
            ImageView dots = findViewById(R.id.imageView6);
            database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String userRole = snapshot.child("role").getValue(String.class);
                        if("administrator".equalsIgnoreCase(userRole)){

                            dots.setVisibility(View.VISIBLE);
                        }else{
                            dots.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    signOut(null);
                }
            });
        }

        ImageView dots = findViewById(R.id.imageView6);

        dots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);

            }
        });


    }

    public void showPopup(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.getMenuInflater().inflate(R.menu.administration, menu.getMenu());

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                final int OPTION_ID = R.id.options;
                final int ADMIN_ID = R.id.administration;

                if (id == ADMIN_ID) {
                    goToAdmin(null);
                } else if (id == OPTION_ID) {
                    goToOption(null);
                } else {
                    signOut(null);
                }

                return true;
            }
        });

        menu.show();
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

    public void goToAdmin(View view){
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToOption(View view){
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);
        finish();
    }


    public void signOut (View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}