package com.cyl.musiclake.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.DataClearmanager;
import com.cyl.musiclake.utils.PreferencesUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.cyl.musiclake.utils.UpdateUtils;

/**
 * Author   : D22434
 * version  : 2018/3/8
 * function :
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private PreferenceScreen preference_about, preference_cache, preference_update;
    public SwitchPreference mWifiSwitch;
    public CheckBoxPreference mLyricCheckBox;


    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);

        preference_about = (PreferenceScreen) findPreference("key_about");
        preference_update = (PreferenceScreen) findPreference("key_update");
        preference_cache = (PreferenceScreen) findPreference("key_cache");

        mWifiSwitch = (SwitchPreference) findPreference("wifi_mode");
        mLyricCheckBox = (CheckBoxPreference) findPreference("key_lyric");

        new Handler().post(() -> {
            try {
                String size = DataClearmanager.getTotalCacheSize(getActivity());
                preference_cache.setSummary(size);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        preference_about.setOnPreferenceClickListener(this);
        preference_update.setOnPreferenceClickListener(this);
        preference_cache.setOnPreferenceClickListener(this);

        mWifiSwitch.setChecked(PreferencesUtils.getWifiMode());

        mWifiSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            Log.e("sss", newValue.toString());
            boolean wifiMode = (boolean) newValue;
            mWifiSwitch.setChecked(wifiMode);
            PreferencesUtils.saveWifiMode(wifiMode);
            return false;
        });
        mLyricCheckBox.setOnPreferenceClickListener(preference -> {
            if (!mLyricCheckBox.isChecked())
                checkPermission();
            return true;
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
                new Handler().post(() -> {
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
                });
                break;
            case "key_update":
                UpdateUtils.checkUpdate(getActivity());
                break;

        }
        return true;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getContext())) {
                ToastUtils.show(getActivity(), "请手动打开显示悬浮窗权限");
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent, 100);
            } else {
                mLyricCheckBox.setChecked(true);
                mLyricCheckBox.setEnabled(false);
                ToastUtils.show(getActivity(), "显示悬浮窗权限已开通");
            }
        }
        if (SystemUtils.isLollipop()) {
            ToastUtils.show(getActivity(), "获取<有权查看使用权限的应用>权限");
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getActivity())) {
                    mLyricCheckBox.setChecked(true);
                    mLyricCheckBox.setEnabled(false);
                    ToastUtils.show(getContext(), "权限已打开");
                } else {
                    mLyricCheckBox.setChecked(false);
                    mLyricCheckBox.setEnabled(true);
                    ToastUtils.show(getContext(), "悬浮窗权限已被拒绝，请手动前往设置中设置");
                }
            }
        }
    }
}
