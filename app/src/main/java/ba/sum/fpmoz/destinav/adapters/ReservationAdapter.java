package ba.sum.fpmoz.destinav.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ba.sum.fpmoz.destinav.R;
import ba.sum.fpmoz.destinav.models.Item;
import ba.sum.fpmoz.destinav.models.Reservation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context mContext;
    private List<Reservation> mReservations;

    public ReservationAdapter(Context context, List<Reservation> reservations) {
        mContext = context;
        mReservations = reservations;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReservationViewHolder holder, int position) {
        final Reservation currentReservation = mReservations.get(position);

        // Fetch hotel details using hotelId
        DatabaseReference hotelRef = FirebaseDatabase.getInstance().getReference("items").child(currentReservation.getHotelId());
        hotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Item hotel = dataSnapshot.getValue(Item.class);


                    holder.textViewName.setText(hotel.getName());
                    holder.textViewLocation.setText(hotel.getLocation());
                    holder.textViewPrice.setText(hotel.getPrice());
                    Glide.with(mContext).load(hotel.getImage()).into(holder.imageView);


                    holder.startDate.setText("Start date: " + currentReservation.getStartDate());
                    holder.endDate.setText("End date: " + currentReservation.getEndDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReservation(currentReservation.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReservations.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewLocation;
        public TextView textViewPrice;
        public ImageView imageView;
        public TextView startDate;
        public TextView endDate;
        public ImageView imageViewDelete;

        public ReservationViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewLocation = itemView.findViewById(R.id.text_view_description);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            imageView = itemView.findViewById(R.id.image_view);
            startDate = itemView.findViewById(R.id.start_date);
            endDate = itemView.findViewById(R.id.end_date);
            imageViewDelete = itemView.findViewById(R.id.imageView2);
        }
    }

    private void deleteReservation(final String reservationId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Potvrda brisanja");
        builder.setMessage("Jeste li sigurni da želite ukloniti rezervaciju?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations").child(reservationId);
                reservationRef.removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Successful deletion
                                Toast.makeText(mContext, "Reservation successfully deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Log.e("ReservationAdapter", "Greška pri brisanju rezervacije", e);
                                Toast.makeText(mContext, "Greška pri uklanjanju rezervacije", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
