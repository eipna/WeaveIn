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

        for (int index = 0; index < hobbies.length; index++) {
            Chip chip = new Chip(this);
            chip.setId(index);
            chip.setText(hobbies[index]);
            chip.setCheckable(true);
            chip.setFocusable(true);
            chip.setClickable(true);
            binding.hobbiesChipGroup.addView(chip);
        }

        binding.privateSwitch.setVisibility(currentUser.getType().equals(User.TYPE_PREMIUM) ? View.VISIBLE : View.GONE);

        binding.fullName.setText(currentUser.getFullName());
        binding.age.setText(String.valueOf(currentUser.getAge()));
        binding.gender.setText(currentUser.getGender(), false);
        binding.emailAddress.setText(currentUser.getEmail());
        binding.phoneNumber.setText(currentUser.getPhoneNumber());
        binding.password.setText(currentUser.getPassword());
        binding.privateSwitch.setChecked(currentUser.getIsPrivate() == User.IS_PRIVATE);
        binding.language.setText(currentUser.getLanguage(), false);
        binding.country.setText(currentUser.getCountry(), false);
        binding.religion.setText(currentUser.getReligion(), false);

        if (currentUser.getHobbies() != null) {
            List<Integer> selectedHobbiesIDs = getSelectedHobbiesIDs();
            for (Integer selectedHobbyId : selectedHobbiesIDs) {
                binding.hobbiesChipGroup.check(selectedHobbyId);
            }
        }

        binding.saveProfile.setOnClickListener(v -> saveProfileDetails());
    }

    public List<Integer> getSelectedHobbiesIDs() {
        List<Integer> IDs = new ArrayList<>();
        String[] selectedHobbies = currentUser.getHobbies().split(",");
        for (String hobby : selectedHobbies) {
            for (int i = 0; i < hobbies.length; i++) {
                if (hobbies[i].equals(hobby)) {
                    IDs.add(i);
                }
            }
        }
        return IDs;
    }

    private void saveProfileDetails() {
        String fullNameText = binding.fullName.getText().toString();
        String ageText = binding.age.getText().toString();
        String genderText = binding.gender.getText().toString();
        String emailText = binding.emailAddress.getText().toString();
        String phoneNumberText = binding.phoneNumber.getText().toString();
        String passwordText = binding.password.getText().toString();
        String languageText = binding.language.getText().toString();
        String religionText = binding.religion.getText().toString();
        String countryText = binding.country.getText().toString();
        String hobbiesText = getHobbiesPreference();

        if (emailText.isEmpty() || phoneNumberText.isEmpty() || passwordText.isEmpty() || fullNameText.isEmpty() || ageText.isEmpty() || genderText.isEmpty() ||
                languageText.isEmpty() || religionText.isEmpty() || countryText.isEmpty() || hobbiesText.isEmpty()) {
            Toast.makeText(EditProfile.this, "Please fill out all information needed", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Integer.parseInt(ageText) < 18 || Integer.parseInt(ageText) > 80) {
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
            return;
        }

        int isPrivateEnabled = (binding.privateSwitch.isChecked()) ? User.IS_PRIVATE : User.NOT_PRIVATE;

        User user = new User();
        user.setID(currentUser.getID());
        user.setFullName(fullNameText);
        user.setAge(Integer.parseInt(ageText));
        user.setGender(genderText);
        user.setLanguage(languageText);
        user.setReligion(religionText);
        user.setCountry(countryText);
        user.setHobbies(hobbiesText);
        user.setType(user.getType());
        user.setIsPrivate(isPrivateEnabled);
        user.setEmail(emailText);
        user.setPassword(passwordText);
        user.setPhoneNumber(phoneNumberText);

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