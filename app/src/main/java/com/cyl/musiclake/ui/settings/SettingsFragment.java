package com.cyl.musiclake.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.ui.main.MainActivity;
import com.cyl.musiclake.ui.theme.ThemeStore;
import com.cyl.musiclake.utils.DataClearManager;
import com.cyl.musiclake.utils.FileUtils;
import com.cyl.musiclake.utils.LogUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;

import java.util.Set;

/**
 * Author   : D22434
 * version  : 2018/3/8
 * function :
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Preference mPreferenceDownloadFile;
    private Preference mPreferenceCacheFile;
    private PreferenceScreen mPreferenceCache;
    public SwitchPreference mWifiSwitch, mSocketSwitch, mNightSwitch;
    public CheckBoxPreference mLyricCheckBox;
    public ListPreference mMusicQualityPreference;
    public MultiSelectListPreference mSearchFilterPreference;
    public EditTextPreference mMusicApiPreference;
    public EditTextPreference mNeteaseApiPreference;

    private Set<String> searchOptions;
    private String[] searchFilters;
    private String musicApi;// = SPUtils.getAnyByKey(SPUtils.SP_KEY_PLATER_API_URL, Constants.BASE_PLAYER_URL);
    private String neteaseApi;// = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_API_URL, Constants.BASE_NETEASE_URL);


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
        mNightSwitch = (SwitchPreference) findPreference("key_night_mode");
        mLyricCheckBox = (CheckBoxPreference) findPreference("key_lyric");
        mMusicQualityPreference = (ListPreference) findPreference("key_music_quality");
        mSearchFilterPreference = (MultiSelectListPreference) findPreference("key_search_filter");
        mMusicApiPreference = (EditTextPreference) findPreference("key_music_api");
        mNeteaseApiPreference = (EditTextPreference) findPreference("key_netease_api");

        mPreferenceCache.setOnPreferenceClickListener(this);
        mSocketSwitch.setOnPreferenceClickListener(this);
        mLyricCheckBox.setOnPreferenceClickListener(this);

        mPreferenceDownloadFile.setSummary(FileUtils.getMusicDir());
        mPreferenceCacheFile.setSummary(FileUtils.getMusicCacheDir());

        initSearchFilterSettings(true);
        mMusicQualityPreference.setSummary(mMusicQualityPreference.getEntry());
        mMusicQualityPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            //把preference这个Preference强制转化为ListPreference类型
            ListPreference listPreference = (ListPreference) preference;
            //获取ListPreference中的实体内容
            CharSequence[] entries = listPreference.getEntries();
            //获取ListPreference中的实体内容的下标值
            int index = listPreference.findIndexOfValue((String) newValue);
            //把listPreference中的摘要显示为当前ListPreference的实体内容中选择的那个项目
            listPreference.setSummary(entries[index]);
            ToastUtils.show("优先播放音质为：" + entries[index]);
            return false;
        });
        mNightSwitch.setChecked(ThemeStore.THEME_MODE == ThemeStore.NIGHT);
        mNightSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean isChecked = (boolean) newValue;
            mNightSwitch.setChecked(isChecked);
            if (isChecked && ThemeStore.THEME_MODE != ThemeStore.NIGHT) {
                ThemeStore.THEME_MODE = ThemeStore.NIGHT;
                ThemeStore.updateThemeMode();
                updateTheme();
            } else if (!isChecked && ThemeStore.THEME_MODE != ThemeStore.DAY) {
                ThemeStore.THEME_MODE = ThemeStore.DAY;
                ThemeStore.updateThemeMode();
                updateTheme();
            }
            return false;
        });

        mSearchFilterPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (searchOptions != newValue) {
                searchOptions = (Set<String>) newValue;
            }
            initSearchFilterSettings(false);
            return false;
        });
        initApiSettings();
    }

    /**
     * 更新主题配置
     */
    private void updateTheme(){
        for (int i = 0; i < MusicApp.activities.size(); i++) {
            if (MusicApp.activities.get(i) instanceof MainActivity) {
                MusicApp.activities.get(i).recreate();
            }
        }
        startActivity(new Intent(getActivity(), SettingsActivity.class));
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();
    }

    /**
     * 初始化搜索过滤
     */
    private void initSearchFilterSettings(boolean isInit) {
        if (isInit) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            searchOptions = prefs.getStringSet("key_search_filter", null);
            searchFilters = getResources().getStringArray(R.array.pref_search_filter_select);
        }

        if (searchOptions != null) {
            StringBuilder info = new StringBuilder();
            for (String t : searchOptions) {
                info.append(searchFilters[Integer.valueOf(t) - 1]);
                info.append("、");
            }
            info.deleteCharAt(info.length() - 1);
            mSearchFilterPreference.setSummary(info);
        }
    }

    /**
     * 初始化Api设置
     */
    private void initApiSettings() {
        //获取本地api地址
        musicApi = SPUtils.getAnyByKey(SPUtils.SP_KEY_PLATER_API_URL, Constants.BASE_PLAYER_URL);
        neteaseApi = SPUtils.getAnyByKey(SPUtils.SP_KEY_NETEASE_API_URL, Constants.BASE_NETEASE_URL);
//
        mMusicApiPreference.setSummary(musicApi);
        mMusicApiPreference.setText(musicApi);
        mNeteaseApiPreference.setSummary(neteaseApi);
        mNeteaseApiPreference.setText(neteaseApi);

        mMusicApiPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (!newValue.toString().equals(neteaseApi)) {
                musicApi = String.valueOf(newValue);
                preference.setSummary(musicApi);
                SPUtils.putAnyCommit(SPUtils.SP_KEY_PLATER_API_URL, musicApi);
                ToastUtils.show(getString(R.string.settings_restart_app));
            }
            return false;
        });
        mNeteaseApiPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (!newValue.toString().equals(neteaseApi)) {
                neteaseApi = String.valueOf(newValue);
                preference.setSummary(neteaseApi);
                SPUtils.putAnyCommit(SPUtils.SP_KEY_NETEASE_API_URL, neteaseApi);
                ToastUtils.show(getString(R.string.settings_restart_app));
            }
            return false;
        });
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
