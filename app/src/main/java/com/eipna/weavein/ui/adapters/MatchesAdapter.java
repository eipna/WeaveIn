package com.eipna.weavein.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eipna.weavein.R;
import com.eipna.weavein.data.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    Context context;
    ArrayList<User> users;
    Listener listener;

    public interface Listener {
        void onSkipClick(int position);
        void onMatchClick(int position);
        void onClick(int position);
    }

    public MatchesAdapter(Context context, Listener listener, ArrayList<User> users) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.getFullName());
        holder.details.setText(String.format("%s | %s", user.getGender(), user.getAge()));
        holder.skip.setOnClickListener(v -> listener.onSkipClick(position));
        holder.match.setOnClickListener(v -> listener.onMatchClick(position));
        holder.itemView.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView name;
        MaterialTextView details;
        MaterialButton skip;
        MaterialButton match;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.account_name);
            details = itemView.findViewById(R.id.account_details);
            skip = itemView.findViewById(R.id.skip);
            match = itemView.findViewById(R.id.match);
        }
    }
}
