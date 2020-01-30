package com.crazyj36.preferencesusage;

import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.app.Fragment;

public class ExamplePreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new ExamplePreferenceFragment()).commit();

    }

    public static class ExamplePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
