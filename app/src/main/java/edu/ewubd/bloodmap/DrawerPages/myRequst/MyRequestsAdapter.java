package edu.ewubd.bloodmap.DrawerPages.myRequst;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.ewubd.bloodmap.ClassModels.BloodTransactionModel;
import edu.ewubd.bloodmap.R;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.ViewHolder> {

    public interface OnManageClickListener {
        void onManageClick(BloodTransactionModel model, int position);
    }

    private List<BloodTransactionModel> requestList;
    private OnManageClickListener listener;

    public MyRequestsAdapter(List<BloodTransactionModel> requestList, OnManageClickListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodTransactionModel model = requestList.get(position);

        holder.tvTitle.setText(model.getBloodGroup() + " Blood Required (" + model.getUnitsRequired() + " Units)");

        if (model.getUrgencyLevel() != null && !model.getUrgencyLevel().isEmpty()) {
            holder.tvUrgency.setText(model.getUrgencyLevel());
            holder.tvUrgency.setVisibility(View.VISIBLE);
        } else {
            holder.tvUrgency.setVisibility(View.GONE);
        }

        String patientInfo = "Patient: " + model.getPatientName();
        if (model.getPatientAge() != null && !model.getPatientAge().isEmpty()) {
            patientInfo += " (Age: " + model.getPatientAge();
            if (model.getPatientGender() != null && !model.getPatientGender().isEmpty()) {
                patientInfo += ", Gender: " + model.getPatientGender();
            }
            patientInfo += ")";
        } else if (model.getPatientGender() != null && !model.getPatientGender().isEmpty()) {
            patientInfo += " (Gender: " + model.getPatientGender() + ")";
        }
        holder.tvPatientDetails.setText(patientInfo);

        if (model.getReason() != null && !model.getReason().isEmpty()) {
            holder.tvReason.setText("Reason: " + model.getReason());
            holder.tvReason.setVisibility(View.VISIBLE);
        } else {
            holder.tvReason.setVisibility(View.GONE);
        }

        String location = model.getHospitalNameArea() != null ? model.getHospitalNameArea() : "";
        if (model.getArea() != null && !model.getArea().isEmpty()) {
            if (!location.isEmpty()) location += ", ";
            location += model.getArea();
        }
        holder.tvHospital.setText("Location: " + location);

        if (model.getNeededByTime() == 0) {
            holder.tvTime.setText("Needed by: ASAP");
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
            String dateStr = sdf.format(new java.util.Date(model.getNeededByTime()));
            holder.tvTime.setText("Needed by: " + dateStr);
        }

        int responses = model.getResponderUids() != null ? model.getResponderUids().size() : 0;
        holder.tvResponsesCount.setText(responses + " Responses Received");

        holder.btnManage.setOnClickListener(v -> {
            if (listener != null) listener.onManageClick(model, position);
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvHospital, tvTime, tvUrgency, tvPatientDetails, tvReason, tvResponsesCount;
        Button btnManage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvHospital = itemView.findViewById(R.id.tvHospital);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvUrgency = itemView.findViewById(R.id.tvUrgency);
            tvPatientDetails = itemView.findViewById(R.id.tvPatientDetails);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvResponsesCount = itemView.findViewById(R.id.tvResponsesCount);
            btnManage = itemView.findViewById(R.id.btnManage);
        }
    }
}
