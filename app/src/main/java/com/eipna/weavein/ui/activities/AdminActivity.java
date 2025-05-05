package com.eipna.weavein.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.data.Database;
import com.eipna.weavein.databinding.ActivityAdminBinding;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private Database database;
    private PreferenceUtil preferenceUtil;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = new Database(this);
        preferenceUtil = new PreferenceUtil(this);

        binding.btnViewAllUsers.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, UserListActivity.class)));
        binding.toolbar.setNavigationOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                    .setTitle("Logout account?")
                    .setMessage("This will log you out of the current session.")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Logout", (dialog, which) -> {
                        preferenceUtil.setUserID(-1);
                        Intent logoutIntent = new Intent(this, MainActivity.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logoutIntent);
                        finish();
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.btnDailyReport.setOnClickListener(v -> {
            Cursor freeUsersToday = database.getNewUsersForToday("free");
            Cursor premiumUsersToday = database.getNewUsersForToday("premium");
            showReportDialog("Daily Report", freeUsersToday, premiumUsersToday);
        });

        binding.btnWeeklyReport.setOnClickListener(v -> {
            Cursor freeUsersWeek = database.getNewUsersForWeek("free");
            Cursor premiumUsersWeek = database.getNewUsersForWeek("premium");
            showReportDialog("Weekly Report", freeUsersWeek, premiumUsersWeek);
        });

        binding.btnMonthlyReport.setOnClickListener(v -> {
            Cursor freeUsersMonth = database.getNewUsersForMonth("free");
            Cursor premiumUsersMonth = database.getNewUsersForMonth("premium");
            showReportDialog("Monthly Report", freeUsersMonth, premiumUsersMonth);
        });
    }

    private void showReportDialog(String title, Cursor freeUsers, Cursor premiumUsers) {
        String report = "Free Users: " + freeUsers.getCount() + "\n" +
                "Premium Users: " + premiumUsers.getCount() + "\n";

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(title);
        builder.setMessage(report);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}