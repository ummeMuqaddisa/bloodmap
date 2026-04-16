package edu.ewubd.bloodmap.DrawerPages.myRequst;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ewubd.bloodmap.ClassModels.BloodTransactionModel;
import edu.ewubd.bloodmap.ClassModels.UserModel;
import edu.ewubd.bloodmap.R;

public class ManageRequestActivity extends AppCompatActivity implements ResponderAdapter.OnAcceptDonorListener {

    private RecyclerView recyclerView;
    private ResponderAdapter adapter;
    private List<UserModel> responderList;
    private TextView tvManageHeader, tvEmpty;
    private TextView tvPatientDetails, tvReason, tvUrgency, tvHospital, tvTime;
    private Button btnCloseRequest;

    private String transactionId;
    private BloodTransactionModel currentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_request);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        tvManageHeader = findViewById(R.id.tvManageHeader);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvPatientDetails = findViewById(R.id.tvPatientDetails);
        tvReason = findViewById(R.id.tvReason);
        tvUrgency = findViewById(R.id.tvUrgency);
        tvHospital = findViewById(R.id.tvHospital);
        tvTime = findViewById(R.id.tvTime);
        btnCloseRequest = findViewById(R.id.btnCloseRequest);
        recyclerView = findViewById(R.id.recyclerViewResponders);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        responderList = new ArrayList<>();
        adapter = new ResponderAdapter(responderList, this);
        recyclerView.setAdapter(adapter);

        transactionId = getIntent().getStringExtra("transactionId");
        if (transactionId == null) {
            Toast.makeText(this, "Error fetching transaction context.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnCloseRequest.setOnClickListener(v -> handleCloseRequest());

        fetchTransactionContext();
    }

    private void fetchTransactionContext() {
        FirebaseFirestore.getInstance().collection("transactions").document(transactionId).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    currentTransaction = documentSnapshot.toObject(BloodTransactionModel.class);
                    if (currentTransaction != null) {
                        tvManageHeader.setText(currentTransaction.getBloodGroup() + " Required (" + currentTransaction.getUnitsRequired() + " Units)");
                        
                        String patientStr = "Patient: " + currentTransaction.getPatientName();
                        if (currentTransaction.getPatientAge() != null && !currentTransaction.getPatientAge().isEmpty()) {
                            patientStr += " (Age: " + currentTransaction.getPatientAge();
                            if (currentTransaction.getPatientGender() != null && !currentTransaction.getPatientGender().isEmpty()) {
                                patientStr += ", " + currentTransaction.getPatientGender();
                            }
                            patientStr += ")";
                        } else if (currentTransaction.getPatientGender() != null && !currentTransaction.getPatientGender().isEmpty()) {
                            patientStr += " (" + currentTransaction.getPatientGender() + ")";
                        }
                        tvPatientDetails.setText(patientStr);

                        if (currentTransaction.getReason() != null && !currentTransaction.getReason().isEmpty()) {
                            tvReason.setText("Reason: " + currentTransaction.getReason());
                            tvReason.setVisibility(View.VISIBLE);
                        } else {
                            tvReason.setVisibility(View.GONE);
                        }

                        if (currentTransaction.getUrgencyLevel() != null && !currentTransaction.getUrgencyLevel().isEmpty()) {
                            tvUrgency.setText(currentTransaction.getUrgencyLevel());
                            tvUrgency.setVisibility(View.VISIBLE);
                        } else {
                            tvUrgency.setVisibility(View.GONE);
                        }

                        String location = currentTransaction.getHospitalNameArea() != null ? currentTransaction.getHospitalNameArea() : "";
                        if (currentTransaction.getArea() != null && !currentTransaction.getArea().isEmpty()) {
                            if (!location.isEmpty()) location += ", ";
                            location += currentTransaction.getArea();
                        }
                        tvHospital.setText("Location: " + location);

                        if (currentTransaction.getNeededByTime() > 0) {
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
                            tvTime.setText("Needed By: " + sdf.format(new java.util.Date(currentTransaction.getNeededByTime())));
                        } else {
                            tvTime.setText("Needed By: ASAP");
                        }

                        fetchResponders(currentTransaction.getResponderUids());
                    }
                } else {
                    tvManageHeader.setText("Transaction Deleted");
                }
            })
            .addOnFailureListener(e -> {
                tvManageHeader.setText("Error loading transaction.");
            });
    }

    private void fetchResponders(List<String> responderUids) {
        if (responderUids == null || responderUids.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (String uid : responderUids) {
            tasks.add(db.collection("users").document(uid).get());
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            responderList.clear();
            for (Object obj : objects) {
                DocumentSnapshot doc = (DocumentSnapshot) obj;
                if (doc.exists()) {
                    responderList.add(doc.toObject(UserModel.class));
                }
            }
            adapter.notifyDataSetChanged();
            if (responderList.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                tvEmpty.setVisibility(View.GONE);
            }
        });
    }

    private void handleCloseRequest() {
        new AlertDialog.Builder(this)
            .setTitle("Close Request Globally")
            .setMessage("Are you sure you want to close this request without assigning a donor? This will completely cancel the active request.")
            .setPositiveButton("YES, CLOSE", (dialog, which) -> {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "CANCELLED");
                updates.put("completedAt", System.currentTimeMillis());

                FirebaseFirestore.getInstance().collection("transactions").document(transactionId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Request safely closed.", Toast.LENGTH_SHORT).show();
                        finish();
                    });
            })
            .setNegativeButton("NO", null)
            .show();
    }

    @Override
    public void onAcceptDonorClick(UserModel userModel, int position) {
        new AlertDialog.Builder(this)
            .setTitle("Accept External Donor")
            .setMessage("Are you sure you want to officially accept " + userModel.getName() + " as the assigned responder? This permanently completes the transaction dynamically!")
            .setPositiveButton("ACCEPT", (dialog, which) -> {
                Map<String, Object> updates = new HashMap<>();
                updates.put("status", "COMPLETED");
                updates.put("selectedDonorUid", userModel.getUid());
                updates.put("completedAt", System.currentTimeMillis());

                FirebaseFirestore.getInstance().collection("transactions").document(transactionId)
                    .update(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Transaction completed globally!", Toast.LENGTH_LONG).show();
                        finish();
                    });
            })
            .setNegativeButton("CANCEL", null)
            .show();
    }
}
