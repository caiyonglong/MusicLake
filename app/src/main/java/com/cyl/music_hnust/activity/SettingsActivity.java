package com.cyl.music_hnust.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.application.MyApplication;
import com.cyl.music_hnust.model.User;
import com.cyl.music_hnust.model.UserStatus;
import com.cyl.music_hnust.utils.Preferences;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container,new  GeneralPreferenceFragment().newInstance()).commit();
    }

    @Override
    protected void listener() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AboutPreferenceFragment extends PreferenceFragment {

        public final AboutPreferenceFragment newInstance() {
            
            Bundle args = new Bundle();
            
            final AboutPreferenceFragment fragment =  new AboutPreferenceFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            setHasOptionsMenu(true);

        }
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new GeneralPreferenceFragment().newInstance())
                        .commit();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


    }

    public static class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private Preference preference_about;
        public Preference preference_cache;
        public Preference preference_info;
        public SwitchPreference secret_switch;
        public CheckBoxPreference night_mode;

        public GeneralPreferenceFragment() {
        }

        public static GeneralPreferenceFragment newInstance() {
            
            Bundle args = new Bundle();
            
            GeneralPreferenceFragment fragment = new GeneralPreferenceFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            preference_about = findPreference("key_about");
            preference_cache = findPreference("key_cache");
            preference_info = findPreference("complete_info");
            secret_switch = (SwitchPreference) findPreference("secret_switch");
            night_mode = (CheckBoxPreference) findPreference("night_mode");


            User user = UserStatus.getUserInfo(getActivity());

            preference_about.setOnPreferenceClickListener(this);
            preference_cache.setOnPreferenceClickListener(this);
            preference_info.setOnPreferenceClickListener(this);

            night_mode.setChecked(Preferences.isNightMode());
            night_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.e("sss",newValue.toString());

                    final boolean on = !Preferences.isNightMode();
                    final ProgressDialog dialog = new ProgressDialog(getActivity());

                    night_mode.setChecked(Preferences.isNightMode());
                    dialog.setCancelable(false);
                    dialog.show();
//                    MyApplication.updateNightMode(getActivity(),on);

                    MyApplication.updateNightMode(on);

                    Handler handler = new Handler(getActivity().getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                            getActivity().recreate();
                            Preferences.saveNightMode(on);
                        }
                    }, 500);
                    return false;
                }
            });

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "key_about":
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new AboutPreferenceFragment().newInstance())
                            .commit();
                    break;
                case "key_cache":
                    Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_LONG).show();
                    break;
                case "complete_info":
                    boolean status = UserStatus.getstatus(getActivity());
                    if (status) {
                        Intent intent = new Intent(getActivity(), UserCenterAcivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
                    break;

            }
            return false;
        }


    }
    public void modify(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("关于湖科音乐")
                .setIcon(R.mipmap.icon)
                .setMessage(msg);

        setPositiveButton(builder)
                .create()
                .show();
    }

    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


}
