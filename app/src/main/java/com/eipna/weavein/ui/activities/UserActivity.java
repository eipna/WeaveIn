package com.eipna.weavein.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivityUserBinding;
import com.eipna.weavein.ui.fragments.ChatFragment;
import com.eipna.weavein.ui.fragments.FeedFragment;
import com.eipna.weavein.ui.fragments.MatchesFragment;
import com.eipna.weavein.ui.fragments.ProfileFragment;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;
    private PreferenceUtil preferenceUtil;

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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        preferenceUtil = new PreferenceUtil(this);

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

        binding.toolbar.setNavigationOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(UserActivity.this)
                    .setTitle("Logout account?")
                    .setMessage("This will log you out of the current session.")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Logout", (dialog, which) -> {
                        preferenceUtil.setUserID(-1);
                        Intent logoutIntent = new Intent(UserActivity.this, MainActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logoutIntent);
                        finish();
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}