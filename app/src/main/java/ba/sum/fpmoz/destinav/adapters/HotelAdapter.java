package ba.sum.fpmoz.destinav.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import  ba.sum.fpmoz.destinav.R;
import  ba.sum.fpmoz.destinav.models.Item;

import org.w3c.dom.Text;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private Context mContext;
    private static List<Item> mItems;
    private OnReserveClickListener mListener;

    public interface OnReserveClickListener {
        void onReserveClick(Item item);
    }

    public void setOnReserveClickListener(OnReserveClickListener listener) {
        mListener = listener;
    }

    public HotelAdapter(Context context, List<Item> items) {
        mContext = context;
        mItems = items;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.hotel_item, parent, false);
        return new HotelViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        Item currentItem = mItems.get(position);
        holder.textViewName.setText(currentItem.getName());
        holder.textViewDescription.setText(currentItem.getLocation());
        holder.textViewPrice.setText(currentItem.getPrice());
        Glide.with(mContext).load(currentItem.getImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewPrice;
        public ImageView imageView;
        public Button reserveButton;

        public HotelViewHolder(View itemView, final OnReserveClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            imageView = itemView.findViewById(R.id.image_view);
            reserveButton = itemView.findViewById(R.id.button_reserve);

            reserveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onReserveClick(mItems.get(position));
                    }
                }
            });
        }
    }
}
