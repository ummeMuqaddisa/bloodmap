package edu.ewubd.bloodmap.admin;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ewubd.bloodmap.ClassModels.UserModel;
import edu.ewubd.bloodmap.R;

public class UserListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<UserModel> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        findViewById(R.id.back_button).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        adapter = new UserAdapter(userList);
        recyclerView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                userList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    UserModel model = doc.toObject(UserModel.class);
                    userList.add(model);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}
