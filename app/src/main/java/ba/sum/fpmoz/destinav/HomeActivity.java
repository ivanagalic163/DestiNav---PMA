package ba.sum.fpmoz.destinav;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        TextView welcomeUser = findViewById(R.id.welcomeUser);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference base = FirebaseDatabase.getInstance().getReference("users");

        if(user != null) {
            String userId = user.getUid();
            base.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String firstName = snapshot.child("firstName").getValue(String.class);
                    welcomeUser.setText("Welcome " + firstName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                   // signOut(null);
                }
            });
            welcomeUser.setText("Welcome " + user.getEmail());
        }

    }

    public void goToAccount(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    public void goDiscover(View view){
        Intent intent = new Intent(this, DiscoverActivity.class);
        startActivity(intent);
        finish();
    }

    public void goReserve(View view){
        Intent intent = new Intent(this, ReservationsActivity.class);
        startActivity(intent);
        finish();
    }


}
