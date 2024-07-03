package ba.sum.fpmoz.destinav.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ba.sum.fpmoz.destinav.R;

import ba.sum.fpmoz.destinav.models.Item;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context mContext;
    private List<Item> mItems;

    public FoodAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Item currentItem = mItems.get(position);
        holder.textViewName.setText(currentItem.getName());
        holder.textViewDescription.setText(currentItem.getLocation());
        Glide.with(mContext).load(currentItem.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;
        public ImageView imageView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}