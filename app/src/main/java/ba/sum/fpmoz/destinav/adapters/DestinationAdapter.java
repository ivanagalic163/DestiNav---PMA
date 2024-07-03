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

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private Context mContext;
    private List<Item> mItems;

    public DestinationAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.destination_item, parent, false);
        return new DestinationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Item currentItem = mItems.get(position);
        holder.textViewName.setText(currentItem.getName());
        holder.textViewDescription.setText(currentItem.getLocation());

        Glide.with(mContext).load(currentItem.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;
        public ImageView imageView;

        public DestinationViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}