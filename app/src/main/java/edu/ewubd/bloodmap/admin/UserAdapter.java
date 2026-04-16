package edu.ewubd.bloodmap.admin;
 
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import edu.ewubd.bloodmap.ClassModels.UserModel;
import edu.ewubd.bloodmap.R;
 
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
 
    private List<UserModel> userList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
 
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
        
        // 1. Name
        holder.tvUserName.setText(user.getName() != null && !user.getName().isEmpty() ? user.getName() : "Anonymous");
        
        // 2. Email
        holder.tvUserEmail.setText(user.getEmail());
        
        // 3. Contact Number
        holder.tvUserContact.setText("Phone: " + (user.getContactNumber() == null || user.getContactNumber().isEmpty() ? "Not set" : user.getContactNumber()));
        
        // 4. Profile Image (Picasso)
        if (user.getProfileImageUrl() != null && !user.getProfileImageUrl().isEmpty()) {
            Picasso.get().load(user.getProfileImageUrl()).placeholder(android.R.drawable.ic_menu_gallery).into(holder.ivUserProfile);
        } else {
            holder.ivUserProfile.setImageResource(android.R.drawable.ic_menu_gallery);
        }
 
        // 5. isAdmin
        if (user.isAdmin()) {
            holder.tvAdminBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvAdminBadge.setVisibility(View.GONE);
        }
        
        // 6. Status
        String status = user.getStatus();
        if (status == null || status.isEmpty()) status = "ACTIVE";
        holder.tvUserStatus.setText(status);
        if (status.equalsIgnoreCase("BLOCKED")) {
            holder.tvUserStatus.setBackgroundColor(0xFFFFCDD2);
            holder.tvUserStatus.setTextColor(0xFFD32F2F);
        } else {
            holder.tvUserStatus.setBackgroundColor(0xFFE8F5E9);
            holder.tvUserStatus.setTextColor(0xFF2E7D32);
        }
        
        // 7. Blood Group
        String bg = user.getBloodGroup();
        if (bg == null || bg.isEmpty()) {
            holder.tvUserBloodGroup.setText("N/A");
            holder.tvUserBloodGroup.setBackgroundColor(0xFF888888);
        } else {
            holder.tvUserBloodGroup.setText(bg);
            holder.tvUserBloodGroup.setBackgroundColor(0xFFD32F2F);
        }
 
        // 8. Availability
        if (user.isAvailableToDonate()) {
            holder.tvAvailabilityBadge.setText("AVAILABLE");
            holder.tvAvailabilityBadge.setBackgroundColor(0xFFFFF3E0);
            holder.tvAvailabilityBadge.setTextColor(0xFFE65100);
        } else {
            holder.tvAvailabilityBadge.setText("UNAVAILABLE");
            holder.tvAvailabilityBadge.setBackgroundColor(0xFFEEEEEE);
            holder.tvAvailabilityBadge.setTextColor(0xFF757575);
        }
 
        // 9. Total Donations
        holder.tvUserDonations.setText("Donations: " + user.getTotalDonations());
 
        // 10. Total Requests
        holder.tvUserRequests.setText("Requests: " + user.getTotalRequests());
 
        // 11. Last Donation Date
        holder.tvLastDonationDate.setText("Last: " + (user.getLastDonationDate() != null ? dateFormat.format(user.getLastDonationDate()) : "N/A"));
 
        // 12. Next Eligible Date
        holder.tvNextEligibleDate.setText("Next: " + (user.getNextEligibleDate() != null ? dateFormat.format(user.getNextEligibleDate()) : "TBD"));
 
        // 13. Location
        holder.tvUserLocation.setText("Area: " + (user.getLocationArea() == null || user.getLocationArea().isEmpty() ? "Not set" : user.getLocationArea()));
 
        // 14. Subscription
        String plan = user.getSubscriptionPlan();
        holder.tvSubscriptionPlan.setText("Plan: " + (plan == null || plan.isEmpty() ? "FREE" : plan));
 
        // 15. Created At
        holder.tvCreatedAt.setText("Joined: " + (user.getCreatedAt() != null ? dateFormat.format(user.getCreatedAt()) : "N/A"));
    }
 
    @Override
    public int getItemCount() {
        return userList.size();
    }
 
    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserProfile;
        TextView tvUserName, tvUserEmail, tvUserContact, tvUserLocation, tvUserBloodGroup, tvAdminBadge, tvUserStatus;
        TextView tvAvailabilityBadge, tvUserDonations, tvUserRequests, tvLastDonationDate, tvNextEligibleDate, tvSubscriptionPlan, tvCreatedAt;
 
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserProfile = itemView.findViewById(R.id.ivUserProfile);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);
            tvUserContact = itemView.findViewById(R.id.tvUserContact);
            tvUserLocation = itemView.findViewById(R.id.tvUserLocation);
            tvUserBloodGroup = itemView.findViewById(R.id.tvUserBloodGroup);
            tvAdminBadge = itemView.findViewById(R.id.tvAdminBadge);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
            tvAvailabilityBadge = itemView.findViewById(R.id.tvAvailabilityBadge);
            tvUserDonations = itemView.findViewById(R.id.tvUserDonations);
            tvUserRequests = itemView.findViewById(R.id.tvUserRequests);
            tvLastDonationDate = itemView.findViewById(R.id.tvLastDonationDate);
            tvNextEligibleDate = itemView.findViewById(R.id.tvNextEligibleDate);
            tvSubscriptionPlan = itemView.findViewById(R.id.tvSubscriptionPlan);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
