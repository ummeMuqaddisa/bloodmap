package edu.ewubd.bloodmap.admin.hospitalsManagement;
 
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
 
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
 
import java.util.ArrayList;
import java.util.List;
 
import edu.ewubd.bloodmap.ClassModels.HospitalContactModel;
import edu.ewubd.bloodmap.R;

public class AdminHospitalsActivity extends AppCompatActivity {
 
    private RecyclerView recyclerView;
    private AdminHospitalAdapter adapter;
    private List<HospitalContactModel> hospitalList;
    private FloatingActionButton fabAddHospital;
    private ProgressBar progressBar;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_contacts);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        
        recyclerView = findViewById(R.id.recyclerViewHospitals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        hospitalList = new ArrayList<>();
        adapter = new AdminHospitalAdapter(hospitalList);
        recyclerView.setAdapter(adapter);
        
        fabAddHospital = findViewById(R.id.fabAddHospital);
        fabAddHospital.setVisibility(View.VISIBLE);
        fabAddHospital.setOnClickListener(v -> startActivity(new Intent(this, AddHospitalActivity.class)));
        
        progressBar = findViewById(R.id.progressBarHospitals);
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        loadHospitals();
    }
 
    private void loadHospitals() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
 
        FirebaseFirestore.getInstance().collection("hospitals")
            .addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
                progressBar.setVisibility(View.GONE);
                
                if (e != null) {
                    Toast.makeText(this, "Failed to load hospitals", Toast.LENGTH_SHORT).show();
                    return;
                }
 
                if (queryDocumentSnapshots != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    hospitalList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        HospitalContactModel model = doc.toObject(HospitalContactModel.class);
                        hospitalList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }
}
