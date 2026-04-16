package edu.ewubd.bloodmap.DrawerPages.nearHospital;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import edu.ewubd.bloodmap.ClassModels.HospitalContactModel;
import edu.ewubd.bloodmap.R;
import edu.ewubd.bloodmap.admin.hospitalsManagement.AddHospitalActivity;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private List<HospitalContactModel> hospitalList;


    public HospitalAdapter(List<HospitalContactModel> hospitalList) {
        this.hospitalList = hospitalList;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        HospitalContactModel model = hospitalList.get(position);
        holder.tvHospitalName.setText(model.getHospitalName());
        holder.tvHospitalContact.setText("Phone: " + model.getContactNumber());
        holder.tvHospitalAddress.setText(model.getAddress());

        // Display Facilities
        if (model.getAvailableFacilities() != null && !model.getAvailableFacilities().isEmpty()) {
            StringBuilder sb = new StringBuilder("Facilities: ");
            for (int i = 0; i < model.getAvailableFacilities().size(); i++) {
                sb.append(model.getAvailableFacilities().get(i));
                if (i < model.getAvailableFacilities().size() - 1) sb.append(", ");
            }
            holder.tvFacilities.setText(sb.toString());
            holder.tvFacilities.setVisibility(View.VISIBLE);
        } else {
            holder.tvFacilities.setVisibility(View.GONE);
        }

        // Display Coordinates
        holder.tvCoordinates.setText(String.format("Coordinates: %.4f, %.4f", model.getLatitude(), model.getLongitude()));

        // Display Blood Bank Status
        if (model.isHasBloodBank()) {
            holder.tvBloodBankStatus.setVisibility(View.VISIBLE);
        } else {
            holder.tvBloodBankStatus.setVisibility(View.GONE);
        }

        // Always hide edit button for normal users
        holder.btnEditHospital.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    static class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospitalName, tvHospitalContact, tvHospitalAddress, tvFacilities, tvCoordinates, tvBloodBankStatus;
        ImageButton btnEditHospital;

        public HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvHospitalContact = itemView.findViewById(R.id.tvHospitalContact);
            tvHospitalAddress = itemView.findViewById(R.id.tvHospitalAddress);
            tvFacilities = itemView.findViewById(R.id.tvFacilities);
            tvCoordinates = itemView.findViewById(R.id.tvCoordinates);
            tvBloodBankStatus = itemView.findViewById(R.id.tvBloodBankStatus);
            btnEditHospital = itemView.findViewById(R.id.btnEditHospital);
        }
    }
}
