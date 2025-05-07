package com.eipna.weavein.ui.activities;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.Photo;
import com.eipna.weavein.data.Preferences;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.ActivityProfileBinding;
import com.eipna.weavein.ui.adapters.PhotoAdapter;
import com.eipna.weavein.util.PreferenceUtil;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private Database database;
    private PreferenceUtil preferenceUtil;
    private ArrayList<Photo> photos;
    private PhotoAdapter photoAdapter;

    int userID;
    User user;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userID = getIntent().getIntExtra("user_id", -1);

        database = new Database(this);
        preferenceUtil = new PreferenceUtil(this);
        photos = new ArrayList<>(database.getPhotos(userID));
        photoAdapter = new PhotoAdapter(this, photos);
        user = database.getUser(userID);

        if (user.getIsPrivate() == User.IS_PRIVATE) {
            binding.containerIsPrivate.setVisibility(VISIBLE);
            binding.containerNotPrivate.setVisibility(GONE);
        } else {
            binding.containerIsPrivate.setVisibility(GONE);
            binding.containerNotPrivate.setVisibility(VISIBLE);
        }

        binding.name.setText(user.getFullName());
        binding.genderAge.setText(String.format("%s | %s", user.getGender(), user.getAge()));

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerView.setAdapter(photoAdapter);

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
}