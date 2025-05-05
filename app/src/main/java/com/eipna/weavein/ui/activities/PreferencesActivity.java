package com.eipna.weavein.ui.activities;

import android.os.Bundle;
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
import com.eipna.weavein.data.Preferences;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivityPreferencesBinding;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private ActivityPreferencesBinding binding;
    private PreferenceUtil preferenceUtil;
    private Database database;
    private User currentUser;
    private Preferences currentPreferences;
    private String[] hobbies;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferenceUtil = new PreferenceUtil(this);
        database = new Database(this);

        hobbies = getResources().getStringArray(R.array.hobbies);
        currentUser = database.getUser(preferenceUtil.getUserID());
        currentPreferences = database.getPreferences(currentUser.getID());

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

        loadUserPreferences();

        binding.savePreferences.setOnClickListener(v -> savePreferences());
    }

    private void loadUserPreferences() {
        binding.language.setText(currentPreferences.getLanguage(), false);
        binding.religion.setText(currentPreferences.getReligion(), false);
        binding.preferencesGender.setText(currentPreferences.getGender(), false);
        binding.country.setText(currentPreferences.getCountry(), false);

        if (currentPreferences.getAge() != null) {
            binding.ageSlider.setValues(getAgeRange());
        }

        if (currentPreferences.getHobbies() != null) {
            List<Integer> selectedHobbiesIDs = getSelectedHobbiesIDs();
            for (Integer selectedHobbyId : selectedHobbiesIDs) {
                binding.hobbiesChipGroup.check(selectedHobbyId);
            }
        }
    }

    public List<Integer> getSelectedHobbiesIDs() {
        List<Integer> IDs = new ArrayList<>();
        String[] selectedHobbies = currentPreferences.getHobbies().split(",");
        for (String hobby : selectedHobbies) {
            for (int i = 0; i < hobbies.length; i++) {
                if (hobbies[i].equals(hobby)) {
                    IDs.add(i);
                }
            }
        }
        return IDs;
    }

    private List<Float> getAgeRange() {
        String[] ageRangeText = currentPreferences.getAge().split(",");

        List<Float> list = new ArrayList<>();
        list.add(Float.parseFloat(ageRangeText[0]));
        list.add(Float.parseFloat(ageRangeText[1]));
        return list;
    }

    private void savePreferences() {
        String languageText = binding.language.getText().toString();
        String religionText = binding.religion.getText().toString();
        String countryText = binding.country.getText().toString();
        String genderText = binding.preferencesGender.getText().toString();
        String ageRangeText = getAgePreference();
        String hobbiesText = getHobbiesPreference();

        Preferences preferences = new Preferences(-1, (int) preferenceUtil.getUserID(), languageText, countryText, religionText, genderText, ageRangeText, hobbiesText);
        database.updatePreferences(preferences);
        Toast.makeText(this, "Preferences has been updated", Toast.LENGTH_SHORT).show();
    }

    private String getAgePreference() {
        List<Float> ageRange = binding.ageSlider.getValues();
        return String.format("%s,%s", ageRange.get(0).intValue(), ageRange.get(1).intValue());
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return true;
    }
}