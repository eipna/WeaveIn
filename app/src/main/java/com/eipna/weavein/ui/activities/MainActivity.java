package com.eipna.weavein.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.data.Database;
import com.eipna.weavein.databinding.ActivityMainBinding;
import com.eipna.weavein.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferenceUtil = new PreferenceUtil(this);
        database = new Database(this);

        binding.login.setOnClickListener(v -> {
            String emailText = binding.inputEmail.getText().toString();
            String passwordText = binding.inputPassword.getText().toString();

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (emailText.equals("admin") && passwordText.equals("admin")) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
                return;
            }

            int userID = database.login(emailText, passwordText);
            if (userID == -1) {
                binding.inputEmail.setText("");
                binding.inputPassword.setText("");
                Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
            } else {
                binding.inputEmail.setText("");
                binding.inputPassword.setText("");

                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                preferenceUtil.setUserID(userID);

                Intent loginIntent = new Intent(MainActivity.this, UserActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        binding.register.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }
}