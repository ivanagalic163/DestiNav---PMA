package ba.sum.fpmoz.destinav;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ba.sum.fpmoz.destinav.adapters.VPAdapter;

public class DiscoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discover);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);

        VPAdapter adapter = new VPAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Hotel");
                                break;
                            case 1:
                                tab.setText("Destination");
                                break;
                            case 2:
                                tab.setText("Food");
                                break;
                            case 3:
                                tab.setText("Shopping");
                                break;
                        }
                    }
                }).attach();
    }
    public void goHome(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToAccount(View view){
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

}