package com.eipna.weavein.ui.activities;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivityEditProfileBinding;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.chip.Chip;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private PreferenceUtil preferenceUtil;
    private Database database;
    private String[] hobbies;
    private User currentUser;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferenceUtil = new PreferenceUtil(this);
        database = new Database(this);
        currentUser = database.getUser(preferenceUtil.getUserID());
        hobbies = getResources().getStringArray(R.array.hobbies);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.privateSwitch.setVisibility(currentUser.getType().equals(User.TYPE_PREMIUM) ? View.VISIBLE : View.GONE);

        binding.fullName.setText(currentUser.getFullName());
        binding.age.setText(String.valueOf(currentUser.getAge()));
        binding.gender.setText(currentUser.getGender(), false);
        binding.emailAddress.setText(currentUser.getEmail());
        binding.phoneNumber.setText(currentUser.getPhoneNumber());
        binding.password.setText(currentUser.getPassword());
        binding.privateSwitch.setChecked(currentUser.getIsPrivate() == User.IS_PRIVATE);

        binding.saveProfile.setOnClickListener(v -> saveProfileDetails());
    }

    private void saveProfileDetails() {
        String fullNameText = binding.fullName.getText().toString();
        String ageText = binding.age.getText().toString();
        String genderText = binding.gender.getText().toString();
        String emailAddressText = binding.emailAddress.getText().toString();
        String passwordText = binding.password.getText().toString();
        String phoneNumberText = binding.phoneNumber.getText().toString();

        if (fullNameText.isEmpty() || ageText.isEmpty() || genderText.isEmpty() || emailAddressText.isEmpty() || passwordText.isEmpty() || phoneNumberText.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (Integer.parseInt(ageText) < 18 || Integer.parseInt(ageText) > 80) {
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
            return;
        }

        int isPrimateEnabled = (binding.privateSwitch.isChecked()) ? User.IS_PRIVATE : User.NOT_PRIVATE;
        User user = new User(currentUser.getID(), fullNameText, Integer.parseInt(ageText), genderText, emailAddressText, passwordText, User.TYPE_FREE, phoneNumberText, isPrimateEnabled);

        if (database.updateProfile(user)) {
            Toast.makeText(this, "Profile has been saved", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }
}