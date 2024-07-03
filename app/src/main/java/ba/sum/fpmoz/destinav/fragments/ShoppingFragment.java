package ba.sum.fpmoz.destinav.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ba.sum.fpmoz.destinav.R;
import ba.sum.fpmoz.destinav.adapters.ShoppingAdapter;
import ba.sum.fpmoz.destinav.models.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ShoppingAdapter mAdapter;
    private List<Item> mItemList;
    private DatabaseReference mDatabaseRef;
    private TextView mTextViewNoShopping;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_shoppings);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mItemList = new ArrayList<>();
        mAdapter = new ShoppingAdapter(getContext(), mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items");

        mTextViewNoShopping = view.findViewById(R.id.text_view_no_shopping);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mItemList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    if (item != null && "Shopping".equals(item.getCategory())) {
                        mItemList.add(item);
                    }
                }
                mAdapter.notifyDataSetChanged();

                if (mItemList.isEmpty()) {
                    mTextViewNoShopping.setVisibility(View.VISIBLE);
                } else {
                    mTextViewNoShopping.setVisibility(View.GONE);
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