package ba.sum.fpmoz.destinav.fragments;

import android.app.DatePickerDialog;
import android.app.usage.NetworkStats;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ba.sum.fpmoz.destinav.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ba.sum.fpmoz.destinav.adapters.HotelAdapter;
import ba.sum.fpmoz.destinav.models.Item;

public class HotelFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private HotelAdapter mAdapter;
    private List<Item> mItemList;
    private DatabaseReference mDatabaseRef;
    private TextView mTextViewNoHotels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_hotels);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemList = new ArrayList<>();
        mAdapter = new HotelAdapter(getContext(), mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items");

        mTextViewNoHotels = view.findViewById(R.id.text_view_no_hotels);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItemList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if (item != null) {
                        item.setId(postSnapshot.getKey());
                        if ("Hotels".equals(item.getCategory())) {
                            Log.d("HotelFragment", "Loaded Item: " + item.getName() + ", ID: " + item.getId());
                            mItemList.add(item);
                        } else {
                            Log.e("HotelFragment", "Item is not a hotel");
                        }
                    } else {
                        Log.e("HotelFragment", "Item is null");
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mItemList.isEmpty()) {
                    mTextViewNoHotels.setVisibility(View.VISIBLE);
                } else {
                    mTextViewNoHotels.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HotelFragment", "Database error: " + databaseError.getMessage());
            }
        });

        mAdapter.setOnReserveClickListener(new HotelAdapter.OnReserveClickListener() {
            @Override
            public void onReserveClick(Item item) {
                showDatePicker(item);
            }
        });

        return view;
    }

    private void showDatePicker(final Item item) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String startDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                showEndDatePicker(item, startDate);
            }
        }, year, month, day);


        startDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        startDatePicker.show();
    }

    private void showEndDatePicker(final Item item, final String startDate) {

        String[] parts = startDate.split("/");
        int startDay = Integer.parseInt(parts[0]);
        int startMonth = Integer.parseInt(parts[1]) - 1;
        int startYear = Integer.parseInt(parts[2]);


        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, startYear);
        calendar.set(Calendar.MONTH, startMonth);
        calendar.set(Calendar.DAY_OF_MONTH, startDay);


        calendar.add(Calendar.DAY_OF_MONTH, 1);


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog endDatePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String endDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                checkExistingReservation(item, startDate, endDate);
            }
        }, year, month, day);


        endDatePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());


        endDatePicker.show();
    }

    private void checkExistingReservation(Item item, String startDate, String endDate) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations");
            reservationRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean alreadyReserved = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String hotelId = snapshot.child("hotelId").getValue(String.class);
                        if (hotelId != null && hotelId.equals(item.getId())) {
                            alreadyReserved = true;
                            break;
                        }
                    }

                    if (alreadyReserved) {
                        Toast.makeText(getContext(), "You have already reserved this hotel.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveReservation(item, startDate, endDate);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("HotelFragment", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("HotelFragment", "Current user is null");
        }
    }

    private void saveReservation(Item item, String startDate, String endDate) {
        Log.d("HotelFragment", "Saving reservation for item: " + item.getName() + ", ID: " + item.getId());

        DatabaseReference reservationRef = FirebaseDatabase.getInstance().getReference("reservations");
        String reservationId = reservationRef.push().getKey();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> reservation = new HashMap<>();
            reservation.put("id", reservationId);
            reservation.put("startDate", startDate);
            reservation.put("endDate", endDate);
            reservation.put("userId", userId);
            reservation.put("hotelId", item.getId());

            if (reservationId != null) {
                reservationRef.child(reservationId).setValue(reservation)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Reservation saved", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Failed to save reservation", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } else {
            Log.e("HotelFragment", "Current user is null");
        }
    }
}
