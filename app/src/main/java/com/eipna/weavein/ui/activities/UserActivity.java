package com.eipna.weavein.ui.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.R;
import com.eipna.weavein.databinding.ActivityUserBinding;
import com.eipna.weavein.ui.fragments.ChatFragment;
import com.eipna.weavein.ui.fragments.FeedFragment;
import com.eipna.weavein.ui.fragments.MatchesFragment;
import com.eipna.weavein.ui.fragments.ProfileFragment;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, new FeedFragment())
                .commit();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.feed) {
                binding.toolbar.setTitle("Feed");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new FeedFragment())
                        .commit();
            }

            if (item.getItemId() == R.id.matches) {
                binding.toolbar.setTitle("Matches");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new MatchesFragment())
                        .commit();
            }

            if (item.getItemId() == R.id.chat) {
                binding.toolbar.setTitle("Chat");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new ChatFragment())
                        .commit();
            }

            if (item.getItemId() == R.id.profile) {
                binding.toolbar.setTitle("Profile");
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
            }
            return true;
        });
    }
}