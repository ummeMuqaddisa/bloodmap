package edu.ewubd.bloodmap.admin.requestManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ewubd.bloodmap.ClassModels.BloodTransactionModel;
import edu.ewubd.bloodmap.R;

public class AdminRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminRequestAdapter adapter;
    private List<BloodTransactionModel> requestList;
    private String statusFilter = "OPEN";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_requests);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        if (getIntent() != null && getIntent().hasExtra("STATUS_FILTER")) {
            statusFilter = getIntent().getStringExtra("STATUS_FILTER");
        }

        TextView tvTitle = findViewById(R.id.tvAdminRequestsTitle);
        tvTitle.setText(statusFilter.equals("OPEN") ? "Active Requests" : "Previous Requests");

        recyclerView = findViewById(R.id.recyclerViewAdminRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        adapter = new AdminRequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBarAdminRequests);
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        loadRequests();
    }

    private void loadRequests() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("transactions");

        if ("OPEN".equals(statusFilter)) {
            query = query.whereEqualTo("status", "OPEN");
        }
        // for history 

        query.addSnapshotListener(this, (queryDocumentSnapshots, e) -> {
            progressBar.setVisibility(View.GONE);
            
            if (e != null) {
                Toast.makeText(this, "Failed to load requests", Toast.LENGTH_SHORT).show();
                return;
            }

            if (queryDocumentSnapshots != null) {
                recyclerView.setVisibility(View.VISIBLE);
                requestList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    BloodTransactionModel model = doc.toObject(BloodTransactionModel.class);
                    if ("OPEN".equals(statusFilter)) {
                        requestList.add(model);
                    } else if (model.getStatus() != null && !model.getStatus().equals("OPEN")) {
                        requestList.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
