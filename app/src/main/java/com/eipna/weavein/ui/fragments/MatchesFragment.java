package com.eipna.weavein.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.eipna.weavein.R;
import com.eipna.weavein.data.Database;
import com.eipna.weavein.data.Preferences;
import com.eipna.weavein.data.User;
import com.eipna.weavein.databinding.FragmentMatchesBinding;
import com.eipna.weavein.ui.adapters.MatchesAdapter;
import com.eipna.weavein.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchesFragment extends Fragment {

    private FragmentMatchesBinding binding;
    private Database database;
    private PreferenceUtil preferenceUtil;
    private ArrayList<User> users;
    private ArrayList<Preferences> preferences;
    private MatchesAdapter matchesAdapter;
    private Preferences userPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMatchesBinding.inflate(getLayoutInflater());
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = new Database(requireContext());
        preferenceUtil = new PreferenceUtil(requireContext());
        users = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        preferences = new ArrayList<>(database.getAllPreference());
        userPreferences = getPreferences(preferenceUtil.getUserID());

        matchesAdapter = new MatchesAdapter(requireContext(), users);

        binding.filterLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                filterMatches("language");
            }
        });

        binding.filterAge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                filterMatches("age");
            }
        });

        binding.filterCountry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                filterMatches("country");
            }
        });

        binding.filterGender.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                filterMatches("gender");
            }
        });

        binding.filterHobbies.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                filterMatches("hobbies");
            }
        });

        binding.filterReligion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                filterMatches("Religion");
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(matchesAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterMatches(String filter) {
        users.clear();
        switch (filter) {
            case "age":
                users.addAll(filterByAge());
                break;
            case "country":
                users.addAll(filterByCountry());
                break;
            case "religion":
                users.addAll(filterByReligion());
                break;
            case "gender":
                users.addAll(filterByGender());
                break;
            case "hobbies":
                users.addAll(filterByHobbies());
                break;
            case "language":
                users.addAll(filterByLanguage());
                break;
        }
        matchesAdapter.notifyDataSetChanged();
    }

    public ArrayList<User> filterByAge() {
        ArrayList<User> usersList = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        ArrayList<User> filteredList = new ArrayList<>();

        for (User user : usersList) {
            int minAge = Integer.parseInt(userPreferences.getAge().split(",")[0]);
            int maxAge = Integer.parseInt(userPreferences.getAge().split(",")[1]);

            if (user.getAge() >= minAge && user.getAge() <= maxAge) {
                filteredList.add(user);
            }
        }
        return filteredList;
    }

    public ArrayList<User> filterByReligion() {
        ArrayList<User> usersList = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        ArrayList<User> filteredList = new ArrayList<>();

        for (User user : usersList) {
            if (userPreferences.getReligion().equals(user.getReligion())) {
                filteredList.add(user);
            }
        }

        return filteredList;
    }

    public ArrayList<User> filterByHobbies() {
        ArrayList<User> usersList = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        ArrayList<User> filteredList = new ArrayList<>();

        String[] preferencesHobbies = userPreferences.getHobbies().split(",");
        Log.d("MatchesFragment", "User preferences hobbies: " + Arrays.toString(preferencesHobbies));

        for (User user : usersList) {
            String[] userHobbies = user.getHobbies().split(",");
            Log.d("MatchesFragment", "Checking user: " + user.getFullName() + " with hobbies: " + Arrays.toString(userHobbies));
            
            boolean hasMatchingHobby = false;

            // Check if any of the user's hobbies match the preferences
            for (String userHobby : userHobbies) {
                String trimmedUserHobby = userHobby.trim();
                for (String preferenceHobby : preferencesHobbies) {
                    String trimmedPreferenceHobby = preferenceHobby.trim();
                    Log.d("MatchesFragment", "Comparing: '" + trimmedUserHobby + "' with '" + trimmedPreferenceHobby + "'");
                    
                    if (trimmedUserHobby.equalsIgnoreCase(trimmedPreferenceHobby)) {
                        Log.d("MatchesFragment", "Match found for user: " + user.getFullName());
                        hasMatchingHobby = true;
                        break;
                    }
                }
                if (hasMatchingHobby) break;
            }

            if (hasMatchingHobby) {
                Log.d("MatchesFragment", "Adding user to filtered list: " + user.getFullName());
                filteredList.add(user);
            }
        }

        Log.d("MatchesFragment", "Total users in filtered list: " + filteredList.size());
        return filteredList;
    }

    public ArrayList<User> filterByCountry() {
        ArrayList<User> usersList = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        ArrayList<User> filteredList = new ArrayList<>();

        for (User user : usersList) {
            if (user.getCountry().equals(userPreferences.getCountry())) {
                filteredList.add(user);
            }
        }

        return filteredList;
    }

    public ArrayList<User> filterByLanguage() {
        ArrayList<User> usersList = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        ArrayList<User> filteredList = new ArrayList<>();

        for (User user : usersList) {
            if (userPreferences.getLanguage().equals(user.getLanguage())) {
                filteredList.add(user);
            }
        }

        return filteredList;
    }

    public ArrayList<User> filterByGender() {
        ArrayList<User> usersList = new ArrayList<>(database.getOtherUsers(preferenceUtil.getUserID()));
        ArrayList<User> filteredList = new ArrayList<>();

        for (User user : usersList) {
            if (userPreferences.getGender().equals(user.getGender())) {
                filteredList.add(user);
            }
        }

        return filteredList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private Preferences getPreferences(int userID) {
        for (Preferences prefs : preferences) {
            if (prefs.getUserID() == userID) {
                return prefs;
            }
        }
        return null;
    }
}