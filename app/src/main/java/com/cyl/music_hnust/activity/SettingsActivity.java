package com.cyl.music_hnust.activity;


import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.DataClearmanager;
import com.cyl.music_hnust.utils.Preferences;
import com.cyl.music_hnust.utils.ToastUtils;
import com.cyl.music_hnust.utils.UpdateUtils;

import butterknife.Bind;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new GeneralPreferenceFragment().newInstance()).commit();
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

    public static class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private PreferenceScreen preference_about, preference_cache, preference_update;
        public SwitchPreference wifi_mode;
        public EditTextPreference feedback;
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

            //反馈
            feedback = (EditTextPreference) findPreference("key_feedback");

            preference_about = (PreferenceScreen) findPreference("key_about");
            preference_update = (PreferenceScreen) findPreference("key_update");
            preference_cache = (PreferenceScreen) findPreference("key_cache");

            wifi_mode = (SwitchPreference) findPreference("wifi_mode");
            night_mode = (CheckBoxPreference) findPreference("night_mode");

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        String size = DataClearmanager.getTotalCacheSize(getActivity());
                        preference_cache.setSummary(size);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            preference_about.setOnPreferenceClickListener(this);
            preference_update.setOnPreferenceClickListener(this);
            preference_cache.setOnPreferenceClickListener(this);

            feedback.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    ToastUtils.show(getActivity(), String.valueOf(newValue));
                    return false;
                }
            });
            night_mode.setChecked(Preferences.isNightMode());
            night_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.e("sss", newValue.toString());

                    final boolean on = !Preferences.isNightMode();
                    final ProgressDialog dialog = new ProgressDialog(getActivity());

                    night_mode.setChecked(Preferences.isNightMode());
                    dialog.setCancelable(false);
                    dialog.show();

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

            wifi_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.e("sss", newValue.toString());

                    return false;
                }
            });

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "key_about":
                    try {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("关于")
                                .setMessage("湖科音乐湖\n当前版本号" + UpdateUtils.getVersion())
                                .show();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "key_cache":
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //清除缓存
                                DataClearmanager.cleanApplicationData(getActivity());
                                ToastUtils.show(getActivity(), "清除成功");
                                String size = DataClearmanager.getTotalCacheSize(getActivity());
                                preference_cache.setSummary(size);
                            } catch (Exception e) {
                                //清除失败
                                ToastUtils.show(getActivity(), "清除失败");
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case "key_update":
                    UpdateUtils.checkUpdate(getActivity());
                    break;

            }
            return false;
        }


    }

}
