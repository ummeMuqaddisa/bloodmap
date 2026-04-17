package edu.ewubd.bloodmap.admin.locationManagement;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ewubd.bloodmap.ClassModels.LocationModel;
import edu.ewubd.bloodmap.R;

public class AdminManageLocationsActivity extends AppCompatActivity {


    private String currentCollection = "locations_hospitals"; // Default
    private String currentType = "hospital";

    private RecyclerView rvLocations;
    private AdminLocationAdapter adapter;
    private final List<LocationModel> locationList = new ArrayList<>();
    private ProgressBar progressBar;
    private ListenerRegistration currentListener;
    private boolean spinnerReady = false; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_locations);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rvLocations = findViewById(R.id.rvLocations);
        rvLocations.setLayoutManager(new LinearLayoutManager(this));

        // Create adapter 
        adapter = new AdminLocationAdapter(this, locationList, currentCollection);
        rvLocations.setAdapter(adapter);

        Spinner spinnerType = findViewById(R.id.spinnerCollectionType);
        String[] types = {"Hospitals", "Areas"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(spinnerAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerReady) { spinnerReady = true; return; } 
                currentCollection = (position == 0) ? "locations_hospitals" : "locations_areas";
                currentType = (position == 0) ? "hospital" : "area";
                adapter.setCollectionName(currentCollection);
                loadLocations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        findViewById(R.id.fabAddLocation).setOnClickListener(v -> showAddLocationDialog());

        progressBar = findViewById(R.id.progressBarLocations);
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        loadLocations();
    }

    private void loadLocations() {
        progressBar.setVisibility(View.VISIBLE);
        rvLocations.setVisibility(View.GONE);

        if (currentListener != null) {
            currentListener.remove();
        }

        currentListener = FirebaseFirestore.getInstance().collection(currentCollection)
                .addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
                    progressBar.setVisibility(View.GONE);
                    if (e != null) {
                        Toast.makeText(this, "Failed to load instances.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (queryDocumentSnapshots != null) {
                        rvLocations.setVisibility(View.VISIBLE);
                        locationList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            try {
                                locationList.add(doc.toObject(LocationModel.class));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void showAddLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New " + (currentType.equals("hospital") ? "Hospital" : "Area"));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 16, 32, 16);

        final EditText nameBox = new EditText(this);
        nameBox.setHint("Precise Name Prefix (e.g. Labaid Dhanmondi)");
        layout.addView(nameBox);

        final EditText latBox = new EditText(this);
        latBox.setHint("Latitude (e.g. 23.7533)");
        latBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(latBox);

        final EditText lonBox = new EditText(this);
        lonBox.setHint("Longitude (e.g. 90.3813)");
        lonBox.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(lonBox);

        builder.setView(layout);

        builder.setPositiveButton("Deploy", (dialog, which) -> {
            String name = nameBox.getText().toString().trim();
            String latStr = latBox.getText().toString().trim();
            String lonStr = lonBox.getText().toString().trim();

            if (name.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                Toast.makeText(this, "All fields are rigidly required.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double latitude = Double.parseDouble(latStr);
                double longitude = Double.parseDouble(lonStr);

                LocationModel newModel = new LocationModel(name, latitude, longitude, currentType);
                pushLocationToFirebase(newModel);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Latitude/Longitude must be exact decimals.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void pushLocationToFirebase(LocationModel model) {
        FirebaseFirestore.getInstance().collection(currentCollection).document().set(model)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cloud Injection Successful!", Toast.LENGTH_SHORT).show();
                    loadLocations(); 
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Deployment Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
