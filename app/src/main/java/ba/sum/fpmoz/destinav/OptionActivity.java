package ba.sum.fpmoz.destinav;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.destinav.models.Item;

public class OptionActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_layout);

        tableLayout = findViewById(R.id.tableLayout);
        emptyTextView = findViewById(R.id.data);


        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items");


        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tableLayout.removeAllViews();


                addTableHeader();

                if (!dataSnapshot.exists()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                } else {
                    emptyTextView.setVisibility(View.GONE);
                }


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemId = snapshot.getKey();

                    Item  item = snapshot.getValue(Item.class);
                    if (item != null) {

                        addTableRow(itemId, item);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addTableHeader() {
        TableRow row = new TableRow(this);


        row.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));


        row.addView(createHeaderTextView("Name"));
        row.addView(createHeaderTextView("Location"));
        row.addView(createHeaderTextView("Category"));
        row.addView(createHeaderTextView("Delete"));




        tableLayout.addView(row);
    }

    private void addTableRow(String itemId, Item item) {

        TableRow row = new TableRow(this);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(layoutParams);


        row.addView(createTextView(item.getName()));
        row.addView(createTextView(item.getLocation()));
        row.addView(createTextView(item.getCategory()));


        row.addView(createDeleteImageView(itemId));


        tableLayout.addView(row);
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView.setPadding(8, 8, 8, 8);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(android.R.color.white));
        textView.setTextSize(16);
        textView.setAllCaps(true);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        textView.setLayoutParams(params);
        textView.setPadding(8, 8, 8, 8);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(android.R.color.black));

        return textView;
    }

    private ImageView createDeleteImageView(final String itemId) {
        ImageView imageView = new ImageView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(params);
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageResource(android.R.drawable.ic_delete);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(itemId);
            }
        });

        return imageView;
    }

    private void deleteItem(final String itemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionActivity.this);
        builder.setMessage("Jeste li sigurni da želite izbrisati? ");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child(itemId);


                databaseRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OptionActivity.this, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("OptionActivity", "Greška pri brisanju predmeta", e);
                        Toast.makeText(OptionActivity.this, "Greška.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Odustani", null);
        builder.show();
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
