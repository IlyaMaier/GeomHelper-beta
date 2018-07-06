package com.example.geomhelper.resources;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geomhelper.R;
import com.example.geomhelper.retrofit.User;
import com.example.geomhelper.sqlite.DB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static com.example.geomhelper.sqlite.OpenHelper.NUM_IMAGE_LEADERS;

public class RVLeaderboardAdapter extends RecyclerView.Adapter<RVLeaderboardAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;
    private DB db;
    private boolean friend;

    public void setData(List<User> u) {
        users = u;
    }

    public RVLeaderboardAdapter(Context context, List<User> users, boolean friend) {
        this.context = context;
        this.users = users;
        this.friend = friend;
        db = new DB(context);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rv_leaderboard_content, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        final User user = users.get(position);

        if (position == 0)
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.gold));
        else if (position == 1)
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.silver));
        else if (position == 2)
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.bronze));

        if (position >= 9) holder.place.setTextSize(17);
        holder.place.setText(String.valueOf(position + 1));
        if (user == null)
            holder.name.setText("");
        else holder.name.setText(user.getName());
        if (user != null)
            holder.experience.setText(String.valueOf(user.getExperience()));
        else
            holder.experience.setText("");
        if (db.getInt(NUM_IMAGE_LEADERS) == 1 && user != null) {
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference profileRef = mStorageRef.child(String.valueOf(user.getId()));
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(uri)
                            .into(holder.image);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView name, experience, place;
        ImageView image;
        CardView cardView;
        LinearLayout linearLayout;

        UserViewHolder(final View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.line_leaderboard);
            name = itemView.findViewById(R.id.text_name_leaderboard);
            experience = itemView.findViewById(R.id.text_experience_leaderboard);
            place = itemView.findViewById(R.id.text_place_leaderboard);
            image = itemView.findViewById(R.id.image_profile_leaderboard);
            cardView = itemView.findViewById(R.id.cardView_leaderboard);

            if (friend)
                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });
        }

    }

}
