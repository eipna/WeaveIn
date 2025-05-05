package com.eipna.weavein.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivitySubscriptionBinding;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SubscriptionActivity extends AppCompatActivity {

    private ActivitySubscriptionBinding binding;
    private String selectedPaymentMethod = null;
    private Database database;
    private PreferenceUtil preferenceUtil;
    private User currentUser;
    private static final int LOADING_DURATION = 2000;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        database = new Database(this);
        preferenceUtil = new PreferenceUtil(this);
        currentUser = database.getUser(preferenceUtil.getUserID());

        setupWindowInsets();
        setupToolbar();
        setupPaymentCheckboxes();
        setupSubscriptionButtons();
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }

    private void setupPaymentCheckboxes() {
        View.OnClickListener checkboxListener = v -> {
            MaterialCheckBox clickedCheckbox = (MaterialCheckBox) v;
            
            binding.chkCreditCard.setChecked(clickedCheckbox == binding.chkCreditCard);
            binding.chkDebitCard.setChecked(clickedCheckbox == binding.chkDebitCard);
            binding.chkEWallet.setChecked(clickedCheckbox == binding.chkEWallet);
            
            selectedPaymentMethod = clickedCheckbox.isChecked() ? clickedCheckbox.getText().toString() : null;
        };

        binding.chkCreditCard.setOnClickListener(checkboxListener);
        binding.chkDebitCard.setOnClickListener(checkboxListener);
        binding.chkEWallet.setOnClickListener(checkboxListener);
    }

    private void setupSubscriptionButtons() {
        binding.btnMonthlySubscribe.setOnClickListener(v -> handleSubscription("monthly"));
        binding.btnAnnualSubscribe.setOnClickListener(v -> handleSubscription("annual"));
    }

    private void handleSubscription(String planType) {
        if (selectedPaymentMethod == null) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        showPaymentDetailsDialog(planType);
    }

    private void showPaymentDetailsDialog(String planType) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_payment_details, null);
        TextInputLayout cardNumberLayout = dialogView.findViewById(R.id.cardNumberLayout);
        TextInputLayout expiryDateLayout = dialogView.findViewById(R.id.expiryDateLayout);
        TextInputLayout cvvLayout = dialogView.findViewById(R.id.cvvLayout);
        TextInputLayout nameLayout = dialogView.findViewById(R.id.nameLayout);
        TextInputLayout emailLayout = dialogView.findViewById(R.id.emailLayout);

        if (selectedPaymentMethod.equals("E-Wallet")) {
            cardNumberLayout.setVisibility(View.GONE);
            expiryDateLayout.setVisibility(View.GONE);
            cvvLayout.setVisibility(View.GONE);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Enter Payment Details")
                .setView(dialogView)
                .setPositiveButton("Proceed", (dialog, which) -> {
                    String cardNumber = ((TextInputEditText) dialogView.findViewById(R.id.cardNumberInput)).getText().toString();
                    String expiryDate = ((TextInputEditText) dialogView.findViewById(R.id.expiryDateInput)).getText().toString();
                    String cvv = ((TextInputEditText) dialogView.findViewById(R.id.cvvInput)).getText().toString();
                    String name = ((TextInputEditText) dialogView.findViewById(R.id.nameInput)).getText().toString();
                    String email = ((TextInputEditText) dialogView.findViewById(R.id.emailInput)).getText().toString();

                    if (selectedPaymentMethod.equals("E-Wallet")) {
                        if (name.isEmpty() || email.isEmpty()) {
                            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty() || name.isEmpty() || email.isEmpty()) {
                            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    showLoadingDialog(planType);
                })
                .setNegativeButton("Cancel", null);

        builder.show();
    }

    private void showLoadingDialog(String planType) {
        View loadingView = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        CircularProgressIndicator progressIndicator = loadingView.findViewById(R.id.progressIndicator);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setView(loadingView)
                .setCancelable(false);

        AlertDialog loadingDialog = builder.create();
        loadingDialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadingDialog.dismiss();
            showSuccessDialog(planType);
        }, LOADING_DURATION);
    }

    private void showSuccessDialog(String planType) {
        String planDetails = planType.equals("monthly") ? "Monthly Plan (USD 12)" : "Annual Plan (USD 100)";
        
        new MaterialAlertDialogBuilder(this)
                .setTitle("Payment Successful!")
                .setMessage(String.format("Thank you for subscribing to our %s. Your premium features are now activated.", planDetails))
                .setPositiveButton("Return home", (dialog, which) -> {
                    currentUser.setType(User.TYPE_PREMIUM);
                    database.updateProfile(currentUser);

                    setResult(RESULT_OK);
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}