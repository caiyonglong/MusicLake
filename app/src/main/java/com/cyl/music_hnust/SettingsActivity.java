package com.cyl.music_hnust;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.cyl.music_hnust.bean.Location;
import com.cyl.music_hnust.utils.DataClearmanager;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    MyHandler handler;
    static class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:

                        //.
                        break;
                    case 1:

                    case 2:
                        break;
                }
            }
        }
    }

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof SwitchPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                SwitchPreference switchPreference = (SwitchPreference) preference;
                if (switchPreference.isChecked()) {
                    Toast.makeText(SettingsActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                }


            } else if (preference instanceof CheckBoxPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
//                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
//                if (checkBoxPreference.isChecked()){
//                    Toast.makeText(SettingsActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
//                }


            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);


            }
            return true;
        }
    };


    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference instanceof EditTextPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new GeneralPreferenceFragment())
                .commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 关闭设置
     */
    public void end() {
        finish();
    }

    ;


    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public  class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private Preference preference_about;
        public Preference preference_cache;
        public CheckBoxPreference secret_check;
        public EditTextPreference nikname;
        public SwitchPreference wifi_switch;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            preference_about = findPreference("key_about");
            preference_cache = findPreference("key_cache");
            secret_check = (CheckBoxPreference) findPreference("secret_check");
            nikname = (EditTextPreference) findPreference("nickname");
            wifi_switch = (SwitchPreference) findPreference("wifi_switch");
            preference_about.setOnPreferenceClickListener(this);
//            editTextPreference.setOnPreferenceChangeListener(this);
            String size = "";
            try {
                size = DataClearmanager.getTotalCacheSize(getActivity());
            } catch (Exception e) {
                size = "0";
                e.printStackTrace();
            }
            preference_cache.setSummary("当前缓存 " + size);
            bindPreferenceSummaryToValue(nikname);
            bindPreferenceSummaryToValue(wifi_switch);
            bindPreferenceSummaryToValue(secret_check);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                end();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "key_about":
                    Toast.makeText(getActivity(), "湖科音乐 1.0", Toast.LENGTH_LONG).show();
                    break;

            }
            return false;
        }

    }

}
