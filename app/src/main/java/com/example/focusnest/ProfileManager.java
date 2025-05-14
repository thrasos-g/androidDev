package com.example.focusnest;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ProfileManager {

    private static ProfileManager instance;
    private final Context context;

    // Temporary hardcoded profiles (replace with DB later)
    private final List<Profile> mockProfiles;

    private ProfileManager(Context context) {
        this.context = context.getApplicationContext();

        // Initialize mock profiles
        mockProfiles = new ArrayList<>();
        mockProfiles.add(new Profile(1, "Default"));
        mockProfiles.add(new Profile(2, "Tryhard"));
        mockProfiles.add(new Profile(3, "Casual"));
        mockProfiles.add(new Profile(4, "Nigger"));
    }

    // Singleton pattern
    public static ProfileManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProfileManager(context);
        }
        return instance;
    }

    public List<Profile> getAllProfiles() {
        // In the future, call the database here
        return mockProfiles;
    }

    public Profile getProfileById(int id) {
        for (Profile profile : mockProfiles) {
            if (profile.getId() == id) {
                return profile;
            }
        }
        return null;
    }

    public void addMockProfile(String name) {
        int newId = mockProfiles.size() + 1; // simplistic
        mockProfiles.add(new Profile(newId, name));
    }

}
