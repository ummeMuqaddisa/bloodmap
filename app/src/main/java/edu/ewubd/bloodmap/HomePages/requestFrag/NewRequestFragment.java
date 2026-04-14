package edu.ewubd.bloodmap.HomePages.requestFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import edu.ewubd.bloodmap.ClassModels.BloodTransactionModel;
import edu.ewubd.bloodmap.R;

public class NewRequestFragment extends Fragment {
    private EditText etPatientName, etBloodGroup, etHospitalDetails, etContactNumber;
    private Button btnSubmitRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_request, container, false);
        etPatientName = view.findViewById(R.id.etPatientName);
        etBloodGroup = view.findViewById(R.id.etBloodGroup);
        etHospitalDetails = view.findViewById(R.id.etHospitalDetails);
        etContactNumber = view.findViewById(R.id.etContactNumber);
        btnSubmitRequest = view.findViewById(R.id.btnSubmitRequest);

        btnSubmitRequest.setOnClickListener(v -> submitRequest());
        return view;
    }

    private void submitRequest() {
        String patientName = etPatientName.getText().toString().trim();
        String bloodGroup = etBloodGroup.getText().toString().trim();
        String hospitalDetails = etHospitalDetails.getText().toString().trim();
        String contactNumber = etContactNumber.getText().toString().trim();

        if (patientName.isEmpty() || bloodGroup.isEmpty() || hospitalDetails.isEmpty() || contactNumber.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "You must be logged in to request blood", Toast.LENGTH_SHORT).show();
            return;
        }

        String transactionId = UUID.randomUUID().toString();
        BloodTransactionModel model = new BloodTransactionModel(
            transactionId,
            user.getUid(),
            bloodGroup,
            hospitalDetails,
            patientName,
            contactNumber,
            "ASAP",
            "OPEN"
        );

        FirebaseFirestore.getInstance().collection("transactions").document(transactionId).set(model)
            .addOnSuccessListener(aVoid -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Blood request submitted successfully!", Toast.LENGTH_SHORT).show();
                    etPatientName.setText("");
                    etBloodGroup.setText("");
                    etHospitalDetails.setText("");
                    etContactNumber.setText("");
                }
            })
            .addOnFailureListener(e -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to submit request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }
}
