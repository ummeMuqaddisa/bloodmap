package edu.ewubd.bloodmap.OfflineMode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import edu.ewubd.bloodmap.DrawerPages.nearBloodBank.BloodBanksActivity;
import edu.ewubd.bloodmap.DrawerPages.nearHospital.HospitalContactsActivity;
import edu.ewubd.bloodmap.HomePages.availableFrag.AvailableFragment;
import edu.ewubd.bloodmap.R;

public class OfflineDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_dashboard);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        findViewById(R.id.cardOfflineHospital).setOnClickListener(v -> {
            startActivity(new Intent(this, HospitalContactsActivity.class));
        });

        findViewById(R.id.cardOfflineBloodBank).setOnClickListener(v -> {
            startActivity(new Intent(this, BloodBanksActivity.class));
        });

        // Load Available Donors Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_offline, new AvailableFragment())
                .commit();
        }
    }
}
