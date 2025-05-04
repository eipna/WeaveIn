package com.eipna.weavein.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.FragmentProfileBinding;
import com.eipna.weavein.ui.activities.EditProfile;
import com.eipna.weavein.ui.activities.MainActivity;
import com.eipna.weavein.ui.activities.PreferencesActivity;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private PreferenceUtil preferenceUtil;
    private Database database;
    private User currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferenceUtil = new PreferenceUtil(requireContext());
        database = new Database(requireContext());
        currentUser = database.getUser(preferenceUtil.getUserID());

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        binding.name.setText(currentUser.getFullName());
        binding.genderAge.setText(String.format("%s | %s", currentUser.getGender(), currentUser.getAge()));

        binding.editProfile.setOnClickListener(v -> editProfileLauncher.launch(new Intent(requireContext(), EditProfile.class)));
        binding.editPreferences.setOnClickListener(v -> startActivity(new Intent(requireContext(), PreferencesActivity.class)));
    }

    private final ActivityResultLauncher<Intent> editProfileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    currentUser = database.getUser(preferenceUtil.getUserID());
                    binding.name.setText(currentUser.getFullName());
                    binding.genderAge.setText(String.format("%s | %s", currentUser.getGender(), currentUser.getAge()));
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}