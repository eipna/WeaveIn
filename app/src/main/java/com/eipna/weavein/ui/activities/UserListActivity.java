package com.eipna.weavein.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivityUserListBinding;
import com.eipna.weavein.databinding.ItemUserBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserListActivity extends AppCompatActivity {
    private ActivityUserListBinding binding;
    private UserAdapter adapter;
    private Database database;
    private List<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = new Database(this);
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();
        setupRecyclerView();
        setupFilterChips();
        loadUsers();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new UserAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupFilterChips() {
        binding.filterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (allUsers == null) return;
            
            List<User> filteredUsers;
            if (checkedId == R.id.chipAll) {
                filteredUsers = new ArrayList<>(allUsers);
            } else if (checkedId == R.id.chipFree) {
                filteredUsers = allUsers.stream()
                    .filter(user -> user.getType().equals(User.TYPE_FREE))
                    .collect(Collectors.toList());
            } else if (checkedId == R.id.chipPremium) {
                filteredUsers = allUsers.stream()
                    .filter(user -> user.getType().equals(User.TYPE_PREMIUM))
                    .collect(Collectors.toList());
            } else {
                filteredUsers = new ArrayList<>(allUsers);
            }
            
            updateUserList(filteredUsers);
        });
    }

    private void loadUsers() {
        allUsers = database.getAllUsers();
        updateUserList(allUsers);
    }

    private void updateUserList(List<User> users) {
        if (users.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(users);
        }
    }

    private static class UserAdapter extends androidx.recyclerview.widget.ListAdapter<User, UserAdapter.UserViewHolder> {
        public UserAdapter() {
            super(new androidx.recyclerview.widget.DiffUtil.ItemCallback<User>() {
                @Override
                public boolean areItemsTheSame(User oldItem, User newItem) {
                    return oldItem.getID() == newItem.getID();
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(User oldItem, User newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @Override
        public UserViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            ItemUserBinding binding = ItemUserBinding.inflate(
                android.view.LayoutInflater.from(parent.getContext()), parent, false);
            return new UserViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            holder.bind(getItem(position));
        }

        static class UserViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
            private final ItemUserBinding binding;

            public UserViewHolder(ItemUserBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(User user) {
                binding.userName.setText(user.getFullName());
                binding.userEmail.setText(user.getEmail());
                binding.userType.setText(user.getType().equals(User.TYPE_PREMIUM) ? User.TYPE_PREMIUM : User.TYPE_FREE);
            }
        }
    }
}