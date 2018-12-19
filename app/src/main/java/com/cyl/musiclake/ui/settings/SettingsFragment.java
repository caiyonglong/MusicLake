package com.cyl.musiclake.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.utils.DataClearManager;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

/**
 * Author   : D22434
 * version  : 2018/3/8
 * function :
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Preference mPreferenceDownloadFile;
    private Preference mPreferenceCacheFile;
    private PreferenceScreen mPreferenceCache;
    public SwitchPreference mWifiSwitch, mSocketSwitch;
    public CheckBoxPreference mLyricCheckBox;
    public MultiSelectListPreference multiSelectListPreference;

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

        initView();

        new Handler().post(() -> {
            String size = DataClearManager.getTotalCacheSize(MusicApp.getAppContext());
            mPreferenceCache.setSummary(size);

        });

        mWifiSwitch.setChecked(SPUtils.getWifiMode());
        mWifiSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            LogUtil.e("sss", newValue.toString());
            boolean wifiMode = (boolean) newValue;
            mWifiSwitch.setChecked(wifiMode);
            SPUtils.saveWifiMode(wifiMode);
            return false;
        });
        if (SystemUtils.isOpenFloatWindow()) {
            mLyricCheckBox.setChecked(true);
        } else {
            mLyricCheckBox.setChecked(false);
        }
        mSocketSwitch.setChecked(MusicApp.isOpenSocket);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPreferenceCache = (PreferenceScreen) findPreference("key_cache");
        mPreferenceDownloadFile = findPreference("key_download_file");
        mPreferenceCacheFile = findPreference("key_cache_file");
        mWifiSwitch = (SwitchPreference) findPreference("wifi_mode");
        mSocketSwitch = (SwitchPreference) findPreference("key_socket");
        mLyricCheckBox = (CheckBoxPreference) findPreference("key_lyric");
        multiSelectListPreference = (MultiSelectListPreference) findPreference("key_search_filter");

        mPreferenceCache.setOnPreferenceClickListener(this);
        mSocketSwitch.setOnPreferenceClickListener(this);
        mLyricCheckBox.setOnPreferenceClickListener(this);

        mPreferenceDownloadFile.setSummary(FileUtils.getMusicDir());
        mPreferenceCacheFile.setSummary(FileUtils.getMusicCacheDir());
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "key_about":
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case "key_cache":
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.title_warning)
                        .content(R.string.setting_clear_cache)
                        .positiveText(R.string.sure)
                        .onPositive((materialDialog, dialogAction) -> {
                            new Handler().post(() -> {
                                try {
                                    //清除缓存
                                    DataClearManager.cleanApplicationData(MusicApp.getAppContext());
                                    ToastUtils.show(getActivity(), "清除成功");
                                    String size = DataClearManager.getTotalCacheSize(MusicApp.getAppContext());
                                    mPreferenceCache.setSummary(size);
                                } catch (Exception e) {
                                    //清除失败
                                    ToastUtils.show(getActivity(), "清除失败");
                                    e.printStackTrace();
                                }
                            });
                        }).show();
                break;
            case "key_socket":
                MusicApp.isOpenSocket = !MusicApp.isOpenSocket;
                mSocketSwitch.setChecked(MusicApp.isOpenSocket);
                MusicApp.socketManager.toggleSocket(MusicApp.isOpenSocket);
                break;
            case "key_lyric":
                checkLyricPermission();
                break;
        }
        return true;
    }

    /**
     * 检查桌面歌词所需的权限
     */
    private void checkLyricPermission() {
        try {
            if (!SystemUtils.isOpenFloatWindow()) {
                ToastUtils.show(getString(R.string.float_window_manual_open));
                SystemUtils.applySystemWindow();
                mLyricCheckBox.setChecked(true);
            } else {
                mLyricCheckBox.setChecked(true);
                ToastUtils.show(getString(R.string.float_window_is_ready));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SystemUtils.isOpenFloatWindow()) {
            mLyricCheckBox.setChecked(true);
        } else {
            mLyricCheckBox.setChecked(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_FLOAT_WINDOW) {
            if (SystemUtils.isOpenFloatWindow()) {
                checkLyricPermission();
            } else {
                ToastUtils.show(MusicApp.getAppContext(), getString(R.string.float_window_is_refused));
            }
        }
    }
}
