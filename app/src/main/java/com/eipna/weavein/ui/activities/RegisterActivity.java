package com.eipna.weavein.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivityRegisterBinding;
import com.google.android.material.chip.ChipGroup;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Database database;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = new Database(this);

        binding.register.setOnClickListener(v -> {
            String fullNameText = binding.inputFullName.getText().toString();
            String ageText = binding.inputAge.getText().toString();
            String genderText = binding.inputGender.getText().toString();
            String emailText = binding.inputEmail.getText().toString();
            String phoneNumberText = binding.inputPhoneNumber.getText().toString();
            String passwordText = binding.inputPassword.getText().toString();
            String confirmPasswordText = binding.inputConfirmPassword.getText().toString();

            if (emailText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (Integer.parseInt(ageText) < 18 || Integer.parseInt(ageText) > 80) {
                Toast.makeText(this, "Age is invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                Toast.makeText(RegisterActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (database.register(fullNameText, Integer.parseInt(ageText), genderText, emailText, phoneNumberText, passwordText, User.TYPE_FREE, User.NOT_PRIVATE)) {
                binding.inputFullName.setText("");
                binding.inputAge.setText("");
                binding.inputGender.setText("");
                binding.inputEmail.setText("");
                binding.inputPhoneNumber.setText("");
                binding.inputPassword.setText("");

                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            } else {
                binding.inputEmail.setText("");
                binding.inputPhoneNumber.setText("");
                binding.inputPassword.setText("");
                Toast.makeText(RegisterActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
            }
        });

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }
}