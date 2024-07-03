package ba.sum.fpmoz.destinav.fragments;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ba.sum.fpmoz.destinav.R;
import ba.sum.fpmoz.destinav.adapters.DestinationAdapter;
import ba.sum.fpmoz.destinav.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DestinationFragment extends Fragment {

    private static final String TAG = "DestinationFragment";

    private RecyclerView mRecyclerView;
    private DestinationAdapter mAdapter;
    private List<Item> mItemList;
    private DatabaseReference mDatabaseRef;
    private TextView mTextViewNoDestination;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_destinations);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemList = new ArrayList<>();
        mAdapter = new DestinationAdapter(getContext(), mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items");

        mTextViewNoDestination = view.findViewById(R.id.text_view_no_destination);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItemList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if (item != null && "Destination".equals(item.getCategory())) {
                        mItemList.add(item);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mItemList.isEmpty()) {
                    mTextViewNoDestination.setVisibility(View.VISIBLE);
                } else {
                    mTextViewNoDestination.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to read value.", databaseError.toException());
            }
        });

        return view;
    }
}
