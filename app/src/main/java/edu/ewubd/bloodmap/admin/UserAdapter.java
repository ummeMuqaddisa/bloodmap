package edu.ewubd.bloodmap.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.ewubd.bloodmap.ClassModels.UserModel;
import edu.ewubd.bloodmap.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserModel> userList;

    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserModel user = userList.get(position);
        
        holder.tvUserName.setText(user.getName());
        holder.tvUserEmail.setText("Email: " + user.getEmail());
        holder.tvUserContact.setText("Phone: " + (user.getContactNumber() == null || user.getContactNumber().isEmpty() ? "Not set" : user.getContactNumber()));
        holder.tvUserLocation.setText("Area: " + (user.getLocationArea() == null || user.getLocationArea().isEmpty() ? "Not set" : user.getLocationArea()));
        
        String bg = user.getBloodGroup();
        if (bg == null || bg.isEmpty()) {
            holder.tvUserBloodGroup.setText("N/A");
            holder.tvUserBloodGroup.setBackgroundColor(0xFF888888);
        } else {
            holder.tvUserBloodGroup.setText(bg);
            holder.tvUserBloodGroup.setBackgroundColor(0xFFD11A2A);
        }

        if (user.isAdmin()) {
            holder.tvAdminBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvAdminBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserEmail, tvUserContact, tvUserLocation, tvUserBloodGroup, tvAdminBadge;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserContact = itemView.findViewById(R.id.tvUserContact);
            tvUserLocation = itemView.findViewById(R.id.tvUserLocation);
            tvUserBloodGroup = itemView.findViewById(R.id.tvUserBloodGroup);
            tvAdminBadge = itemView.findViewById(R.id.tvAdminBadge);
        }
    }
}
