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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Database database;
    private String[] hobbies;

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
        hobbies = getResources().getStringArray(R.array.hobbies);

        binding.register.setOnClickListener(v -> {
            String fullNameText = binding.inputFullName.getText().toString();
            String ageText = binding.inputAge.getText().toString();
            String genderText = binding.inputGender.getText().toString();
            String emailText = binding.inputEmail.getText().toString();
            String phoneNumberText = binding.inputPhoneNumber.getText().toString();
            String passwordText = binding.inputPassword.getText().toString();
            String confirmPasswordText = binding.inputConfirmPassword.getText().toString();
            String languageText = binding.language.getText().toString();
            String religionText = binding.religion.getText().toString();
            String countryText = binding.country.getText().toString();
            String hobbiesText = getHobbiesPreference();

            if (emailText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty() || fullNameText.isEmpty() || ageText.isEmpty() || genderText.isEmpty() ||
            languageText.isEmpty() || religionText.isEmpty() || countryText.isEmpty() || hobbiesText.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill out all information needed", Toast.LENGTH_SHORT).show();
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

            User user = new User();
            user.setFullName(fullNameText);
            user.setAge(Integer.parseInt(ageText));
            user.setGender(genderText);
            user.setLanguage(languageText);
            user.setCountry(countryText);
            user.setHobbies(hobbiesText);
            user.setReligion(religionText);
            user.setType(User.TYPE_FREE);
            user.setIsPrivate(User.NOT_PRIVATE);
            user.setEmail(emailText);
            user.setPassword(passwordText);
            user.setPhoneNumber(phoneNumberText);

            if (database.register(user)) {
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

        for (int index = 0; index < hobbies.length; index++) {
            Chip chip = new Chip(this);
            chip.setId(index);
            chip.setText(hobbies[index]);
            chip.setCheckable(true);
            chip.setFocusable(true);
            chip.setClickable(true);
            binding.hobbiesChipGroup.addView(chip);
        }
    }

    private String getHobbiesPreference() {
        List<Integer> selectedHobbiesIDs = binding.hobbiesChipGroup.getCheckedChipIds();
        StringBuilder stringBuilder = new StringBuilder();

        for (Integer selectedHobbyID : selectedHobbiesIDs)  {
            stringBuilder.append(getChipHobbyName(selectedHobbyID)).append(",");
        }
        return stringBuilder.toString();
    }

    private String getChipHobbyName(int hobbyChipID) {
        for (int i = 0; i < hobbies.length; i++) {
            if (i == hobbyChipID) {
                return hobbies[i];
            }
        }
        return null;
    }
}