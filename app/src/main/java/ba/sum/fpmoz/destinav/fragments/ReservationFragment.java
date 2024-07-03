package ba.sum.fpmoz.destinav.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ba.sum.fpmoz.destinav.R;
import ba.sum.fpmoz.destinav.adapters.ReservationAdapter;
import ba.sum.fpmoz.destinav.models.Reservation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fpmoz.destinav.adapters.ReservationAdapter;

public class ReservationFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ReservationAdapter mAdapter;
    private List<Reservation> mReservations;
    private TextView mNoReservationText;

    private DatabaseReference mDatabaseRef;

    public ReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reservations, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view_reservations);
        mNoReservationText = rootView.findViewById(R.id.text_view_no_reservation);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reservations");


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mReservations = new ArrayList<>();

        mAdapter = new ReservationAdapter(getContext(), mReservations);
        mRecyclerView.setAdapter(mAdapter);


        loadUserReservations();

        return rootView;
    }

    private void loadUserReservations() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();


            Query query = mDatabaseRef.orderByChild("userId").equalTo(userId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReservations.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Reservation reservation = snapshot.getValue(Reservation.class);
                        mReservations.add(reservation);
                    }

                    mAdapter.notifyDataSetChanged();


                    if (mReservations.isEmpty()) {
                        mNoReservationText.setVisibility(View.VISIBLE);
                    } else {
                        mNoReservationText.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                    Log.e("ReservationFragment", "Error loading reservations", databaseError.toException());
                }
            });
        }
    }
}
