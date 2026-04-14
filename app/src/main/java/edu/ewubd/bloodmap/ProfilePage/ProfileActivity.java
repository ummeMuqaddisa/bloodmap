package edu.ewubd.bloodmap.ProfilePage;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.ewubd.bloodmap.ClassModels.userModel;
import edu.ewubd.bloodmap.R;

public class ProfileActivity extends AppCompatActivity {
    private EditText etFullName, etBloodGroup, etLocation, etPhone;
    private Button btnSaveProfile;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    
    private userModel currentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
        
        etFullName = findViewById(R.id.etFullName);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etLocation = findViewById(R.id.etLocation);
        etPhone = findViewById(R.id.etPhone);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            loadUserProfile();
        } else {
            Toast.makeText(this, "Not logged in!", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSaveProfile.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        db.collection("users").document(currentUser.getUid()).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    currentModel = documentSnapshot.toObject(userModel.class);
                    if (currentModel != null) {
                        etFullName.setText(currentModel.getName());
                        etBloodGroup.setText(currentModel.getBloodGroup());
                        etLocation.setText(currentModel.getLocationArea());
                        etPhone.setText(currentModel.getContactNumber());
                    }
                }
            })
            .addOnFailureListener(e -> Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void saveUserProfile() {
        if (currentModel == null) return;
        
        currentModel.setName(etFullName.getText().toString().trim());
        currentModel.setBloodGroup(etBloodGroup.getText().toString().trim());
        currentModel.setLocationArea(etLocation.getText().toString().trim());
        currentModel.setContactNumber(etPhone.getText().toString().trim());

        db.collection("users").document(currentUser.getUid()).set(currentModel)
            .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show())
            .addOnFailureListener(e -> Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show());
    }
}
