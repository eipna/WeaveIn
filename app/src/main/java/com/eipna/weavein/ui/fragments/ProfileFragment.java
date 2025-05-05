package com.eipna.weavein.ui.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.Photo;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.FragmentProfileBinding;
import com.eipna.weavein.ui.activities.EditProfile;
import com.eipna.weavein.ui.activities.MainActivity;
import com.eipna.weavein.ui.activities.PreferencesActivity;
import com.eipna.weavein.ui.activities.SubscriptionActivity;
import com.eipna.weavein.ui.adapters.PhotoAdapter;
import com.eipna.weavein.util.PreferenceUtil;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private PreferenceUtil preferenceUtil;
    private Database database;
    private User currentUser;
    private PhotoAdapter photoAdapter;
    private ArrayList<Photo> photos;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        preferenceUtil = new PreferenceUtil(requireContext());
        database = new Database(requireContext());
        currentUser = database.getUser(preferenceUtil.getUserID());

        photos = new ArrayList<>(database.getPhotos(preferenceUtil.getUserID()));
        photoAdapter = new PhotoAdapter(requireContext(), photos);

        binding.name.setText(currentUser.getFullName());
        binding.genderAge.setText(String.format("%s | %s", currentUser.getGender(), currentUser.getAge()));

        binding.subscription.setVisibility(currentUser.getType().equals(User.TYPE_PREMIUM) ? View.GONE : View.VISIBLE);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.recyclerView.setAdapter(photoAdapter);

        binding.editProfile.setOnClickListener(v -> editProfileLauncher.launch(new Intent(requireContext(), EditProfile.class)));
        binding.editPreferences.setOnClickListener(v -> startActivity(new Intent(requireContext(), PreferencesActivity.class)));
        binding.subscription.setOnClickListener(v -> subscriptionLauncher.launch(new Intent(requireContext(), SubscriptionActivity.class)));
        binding.fab.setOnClickListener(v -> {
            Intent uploadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            uploadPhotoLauncher.launch(uploadIntent);
        });
    }

    private final ActivityResultLauncher<Intent> editProfileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    currentUser = database.getUser(preferenceUtil.getUserID());
                    binding.name.setText(currentUser.getFullName());
                    binding.genderAge.setText(String.format("%s | %s", currentUser.getGender(), currentUser.getAge()));
                }
            });

    private final ActivityResultLauncher<Intent> subscriptionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    binding.subscription.setVisibility(View.GONE);
                }
            });

    private final ActivityResultLauncher<Intent> uploadPhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        uploadPhoto(data.getData());
                    }
                }
            });

    @SuppressLint("NotifyDataSetChanged")
    private void uploadPhoto(Uri uri) {
        byte[] photo = getPhotoFromURI(uri);
        database.uploadPhoto(preferenceUtil.getUserID(), photo);

        photos.clear();
        photos.addAll(database.getPhotos(preferenceUtil.getUserID()));
        photoAdapter.notifyDataSetChanged();
    }

    private byte[] getPhotoFromURI(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.WEBP, 70, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}