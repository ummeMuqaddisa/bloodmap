package edu.ewubd.bloodmap.admin.bloodbankManagement;
 
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
 
import edu.ewubd.bloodmap.ClassModels.BloodBankModel;
import edu.ewubd.bloodmap.R;

public class AdminBloodBanksActivity extends AppCompatActivity {
 
    private RecyclerView recyclerView;
    private AdminBloodBankAdapter adapter;
    private List<BloodBankModel> bankList;
    private FloatingActionButton fabAddBloodBank;
    private ProgressBar progressBar;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_banks);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
 
        recyclerView = findViewById(R.id.recyclerViewBloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
 
        bankList = new ArrayList<>();
        adapter = new AdminBloodBankAdapter(bankList);
        recyclerView.setAdapter(adapter);
 
        fabAddBloodBank = findViewById(R.id.fabAddBloodBank);
        fabAddBloodBank.setVisibility(View.VISIBLE);
        fabAddBloodBank.setOnClickListener(v -> startActivity(new Intent(this, AddBloodBankActivity.class)));
 
        progressBar = findViewById(R.id.progressBarBanks);
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        loadBloodBanks();
    }
 
    private void loadBloodBanks() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
 
        FirebaseFirestore.getInstance().collection("blood_banks")
            .addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
                progressBar.setVisibility(View.GONE);
 
                if (e != null) {
                    Toast.makeText(this, "Failed to load blood banks", Toast.LENGTH_SHORT).show();
                    return;
                }
 
                if (queryDocumentSnapshots != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    bankList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        BloodBankModel model = doc.toObject(BloodBankModel.class);
                        bankList.add(model);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }
}
