package ba.sum.fpmoz.destinav.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ba.sum.fpmoz.destinav.fragments.DestinationFragment;
import ba.sum.fpmoz.destinav.fragments.FoodFragment;
import ba.sum.fpmoz.destinav.fragments.HotelFragment;
import ba.sum.fpmoz.destinav.fragments.ShoppingFragment;


public class VPAdapter extends FragmentStateAdapter {
    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new DestinationFragment();
            case 2:
                return new FoodFragment();
            case 3:
                return new ShoppingFragment();
            default:
                return new HotelFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}