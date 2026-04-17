package edu.ewubd.bloodmap.admin.locationManagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import edu.ewubd.bloodmap.ClassModels.LocationModel;
import edu.ewubd.bloodmap.R;

public class AdminLocationAdapter extends RecyclerView.Adapter<AdminLocationAdapter.LocationViewHolder> {

    private final Context context;
    private final List<LocationModel> locationList;
    private String collectionName;

    public AdminLocationAdapter(Context context, List<LocationModel> locationList, String collectionName) {
        this.context = context;
        this.locationList = locationList;
        this.collectionName = collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationModel model = locationList.get(position);
        holder.tvLocationName.setText(model.getName());
        holder.tvLocationCoords.setText(String.format(java.util.Locale.US, "Lat: %.4f, Lng: %.4f", model.getLatitude(), model.getLongitude()));

        holder.btnDeleteLocation.setOnClickListener(v -> deleteLocation(model, position));
    }

    private void deleteLocation(LocationModel model, int position) {
        if (model.getId() != null) {
            FirebaseFirestore.getInstance().collection(collectionName)
                .document(model.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    locationList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Location deleted securely.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "Please restart app to sync database IDs before deleting.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocationName, tvLocationCoords;
        ImageButton btnDeleteLocation;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocationName = itemView.findViewById(R.id.tvLocationName);
            tvLocationCoords = itemView.findViewById(R.id.tvLocationCoords);
            btnDeleteLocation = itemView.findViewById(R.id.btnDeleteLocation);
        }
    }
}
