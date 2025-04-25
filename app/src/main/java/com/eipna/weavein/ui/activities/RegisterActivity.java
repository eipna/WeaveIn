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
import com.eipna.weavein.databinding.ActivityRegisterBinding;

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

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = binding.inputEmail.getText().toString();
                String phoneNumberText = binding.inputPhoneNumber.getText().toString();
                String passwordText = binding.inputPassword.getText().toString();

                if (emailText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (database.register(emailText, phoneNumberText, passwordText)) {
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
                    Toast.makeText(RegisterActivity.this, "Email or Phone Number already exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        });
    }
}