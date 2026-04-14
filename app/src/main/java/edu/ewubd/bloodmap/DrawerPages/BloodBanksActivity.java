package edu.ewubd.bloodmap.DrawerPages;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ewubd.bloodmap.ClassModels.BloodBankModel;
import edu.ewubd.bloodmap.R;

public class BloodBanksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BloodBankAdapter adapter;
    private List<BloodBankModel> bankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_banks);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        
        recyclerView = findViewById(R.id.recyclerViewBloodBanks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        bankList = new ArrayList<>();
        adapter = new BloodBankAdapter(bankList);
        recyclerView.setAdapter(adapter);
        
        loadBloodBanks();
    }

    private void loadBloodBanks() {
        FirebaseFirestore.getInstance().collection("blood_banks")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                bankList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    BloodBankModel model = doc.toObject(BloodBankModel.class);
                    bankList.add(model);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to load blood banks", Toast.LENGTH_SHORT).show();
            });
    }
}
