package ba.sum.fpmoz.destinav.fragments;

import android.os.Bundle;
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
import ba.sum.fpmoz.destinav.adapters.FoodAdapter;
import ba.sum.fpmoz.destinav.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FoodAdapter mAdapter;
    private List<Item> mItemList;
    private DatabaseReference mDatabaseRef;
    private TextView mTextViewNoFood;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_foods);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemList = new ArrayList<>();
        mAdapter = new FoodAdapter(getContext(), mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items");

        mTextViewNoFood = view.findViewById(R.id.text_view_no_food);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItemList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if (item != null && "Food".equals(item.getCategory())) {
                        mItemList.add(item);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mItemList.isEmpty()) {
                    mTextViewNoFood.setVisibility(View.VISIBLE);
                } else {
                    mTextViewNoFood.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        return view;
    }
}