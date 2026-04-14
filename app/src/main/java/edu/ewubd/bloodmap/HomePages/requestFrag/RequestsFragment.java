package edu.ewubd.bloodmap.HomePages.requestFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import edu.ewubd.bloodmap.ClassModels.BloodTransactionModel;
import edu.ewubd.bloodmap.R;

public class RequestsFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<BloodTransactionModel> requestList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);
        
        loadRequests();
        
        return view;
    }

    private void loadRequests() {
        FirebaseFirestore.getInstance().collection("transactions")
            .whereEqualTo("status", "OPEN")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                requestList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    BloodTransactionModel model = doc.toObject(BloodTransactionModel.class);
                    requestList.add(model);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load requests", Toast.LENGTH_SHORT).show();
                }
            });
    }
}
