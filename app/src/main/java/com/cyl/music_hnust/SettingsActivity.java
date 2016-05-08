package com.cyl.music_hnust;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.cyl.music_hnust.bean.Location;
import com.cyl.music_hnust.bean.User;
import com.cyl.music_hnust.bean.UserStatus;
import com.cyl.music_hnust.http.HttpUtil;
import com.cyl.music_hnust.utils.Constants;
import com.cyl.music_hnust.utils.DataClearmanager;
import com.cyl.music_hnust.utils.ToastUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

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

    public void UpdateUserinfo(String issecret) {


        User user = UserStatus.getUserInfo(getApplicationContext());
        if (user.getUser_id() == null) {
            Toast.makeText(SettingsActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        String updateurl =  Constants.DEFAULT_URL +
                "updateUser&nick&user_email&phone&secret="
                +issecret+"&user_id=" +user.getUser_id();


        HttpUtil.get(updateurl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String str = new String(responseBody, "utf-8");
                    if (str.endsWith(":10000}")) {
                        Toast.makeText(SettingsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(SettingsActivity.this, "修改失败，网络异常", Toast.LENGTH_SHORT).show();
            }
        });


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
                if (switchPreference.getKey().equals("secret_switch")) {
                    if (switchPreference.isChecked()){
                        UpdateUserinfo("2");
                        Log.e("check+++++++++++++",switchPreference.isChecked()+"");
                    }else {
                        Log.e("check+++++++++++++",switchPreference.isChecked()+"");
                        UpdateUserinfo("1");
                    }
                }


            } else if (preference instanceof CheckBoxPreference) {

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

    //    /**
//     * 关闭设置
//     */
    public void end() {
        finish();
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("ValidFragment")
    public class GeneralPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        private Preference preference_about;
        public Preference preference_cache;
        public Preference preference_info;
        public SwitchPreference secret_switch;
        public SwitchPreference wifi_switch;
        public ListPreference mode_list;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            preference_about = findPreference("key_about");
            preference_cache = findPreference("key_cache");
            preference_info = findPreference("complete_info");
            secret_switch = (SwitchPreference) findPreference("secret_switch");

            wifi_switch = (SwitchPreference) findPreference("wifi_switch");
            mode_list = (ListPreference) findPreference("mode_list");
            String stringValue = mode_list.getValue().toString();
            int index = mode_list.findIndexOfValue(stringValue);

            User user = UserStatus.getUserInfo(getActivity());
            if (user.getUser_name()!=null&&user.isSecret()){
                secret_switch.setDefaultValue(false);
            }else {
                secret_switch.setDefaultValue(true);
            }

            mode_list.setSummary(
                    index >= 0
                            ? mode_list.getEntries()[index]
                            : null);
            preference_about.setOnPreferenceClickListener(this);
            preference_cache.setOnPreferenceClickListener(this);
            preference_info.setOnPreferenceClickListener(this);

            bindPreferenceSummaryToValue(wifi_switch);
            bindPreferenceSummaryToValue(secret_switch);
            bindPreferenceSummaryToValue(findPreference("mode_list"));
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
                    try {
                        modify("当前版本: V"+getVersionName()+
                                "\n联系与反馈: 请联系作者\n" +
                                "QQ: 643872807");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "key_cache":
                    Toast.makeText(getActivity(), "清除成功", Toast.LENGTH_LONG).show();
                    break;
                case "complete_info":
                    User user =UserStatus.getUserInfo(getApplicationContext());
                    if (user.getUser_name()!=null) {
                        Intent intent = new Intent(getApplicationContext(), UserCenterMainAcivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    break;

            }
            return false;
        }


    }
    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
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
