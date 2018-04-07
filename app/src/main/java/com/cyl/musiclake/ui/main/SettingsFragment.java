package com.cyl.musiclake.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;
import com.cyl.musiclake.RxBus;
import com.cyl.musiclake.event.ScheduleTaskEvent;
import com.cyl.musiclake.service.MusicPlayerService;
import com.cyl.musiclake.utils.DataClearmanager;
import com.cyl.musiclake.utils.FormatUtil;
import com.cyl.musiclake.utils.SPUtils;
import com.cyl.musiclake.utils.SystemUtils;
import com.cyl.musiclake.utils.ToastUtils;
import com.tencent.bugly.beta.Beta;

import static com.cyl.musiclake.service.MusicPlayerService.SCHEDULE_CHANGED;
import static com.cyl.musiclake.service.MusicPlayerService.mShutdownScheduled;
import static com.cyl.musiclake.service.MusicPlayerService.totalTime;

/**
 * Author   : D22434
 * version  : 2018/3/8
 * function :
 */

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private PreferenceScreen mPreferenceAbout, mPreferenceCache, mPreferenceUpdate;
    public SwitchPreference mWifiSwitch, mTimingSwitch;
    public CheckBoxPreference mLyricCheckBox;
    private int time = 0;
    private int[] times = new int[]{0, 15, 30, 45, 60};


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
            try {
                String size = DataClearmanager.getTotalCacheSize(getActivity());
                mPreferenceCache.setSummary(size);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        mWifiSwitch.setChecked(SPUtils.getWifiMode());
        mWifiSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            Log.e("sss", newValue.toString());
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
        updateTimeSwitch(MusicPlayerService.mShutdownScheduled);
        RxBus.getInstance().register(ScheduleTaskEvent.class)
                .subscribe(metaChangedEvent -> {
                    if (mTimingSwitch != null) {
                        mTimingSwitch.setSummary(FormatUtil.formatTime(MusicPlayerService.time));
                    }
                });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mPreferenceAbout = (PreferenceScreen) findPreference("key_about");
        mPreferenceUpdate = (PreferenceScreen) findPreference("key_update");
        mPreferenceCache = (PreferenceScreen) findPreference("key_cache");
        mWifiSwitch = (SwitchPreference) findPreference("wifi_mode");
        mTimingSwitch = (SwitchPreference) findPreference("key_timing");
        mLyricCheckBox = (CheckBoxPreference) findPreference("key_lyric");

        mPreferenceAbout.setOnPreferenceClickListener(this);
        mPreferenceUpdate.setOnPreferenceClickListener(this);
        mPreferenceCache.setOnPreferenceClickListener(this);
        mTimingSwitch.setOnPreferenceClickListener(this);
        mLyricCheckBox.setOnPreferenceClickListener(this);
    }

    /**
     * 开启对话框
     */
    private void openDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("定时关闭")
                .items("不开启", "15分钟", "30分钟", "45分钟", "60分钟", "自定义")
                .itemsCallbackSingleChoice(getSelectTime(), (dialog, itemView, which, text) -> {
                    if (which == 0) {
                        updateTimeSwitch(false);
                    } else if (which == 5) {
                        dialog.cancel();
                        new MaterialDialog.Builder(getActivity())
                                .title("自定义时间")
                                .inputType(InputType.TYPE_CLASS_NUMBER)//可以输入的类型-电话号码
                                .input("分钟（不能大于24小时）", "", (dialog1, input) -> Log.i("yqy", "输入的是：" + input))
                                .inputRange(1, 3)
                                .onPositive((dialog12, which1) -> {
                                    if (dialog12.getInputEditText().length() > 4) {
                                        dialog12.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                                    } else {
                                        dialog12.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                                        time = Integer.parseInt(dialog12.getInputEditText().getText().toString());
                                        updateTimeSwitch(true);
                                        startTimerService();
                                    }
                                }).show();
                    } else {
                        time = times[which];
                        updateTimeSwitch(true);
                        startTimerService();
                    }
                    return false;
                }).build()
                .show();
    }

    /**
     * 更新定时器开关
     *
     * @param mOpen
     */
    private void updateTimeSwitch(boolean mOpen) {
        MusicPlayerService.mShutdownScheduled = mOpen;
        mTimingSwitch.setChecked(mOpen);
    }

    /**
     * 开启service中定时任务
     */
    private void startTimerService() {
        Intent intent = new Intent(getActivity(), MusicPlayerService.class);
        intent.setAction(SCHEDULE_CHANGED);
        intent.putExtra("time", time);
        getActivity().startService(intent);
    }

    private int getSelectTime() {
        for (int i = 0; i < times.length; i++) {
            if (totalTime == times[i]) return i;
        }
        return 0;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "key_about":
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case "key_cache":
                new Handler().post(() -> {
                    try {
                        //清除缓存
                        DataClearmanager.cleanApplicationData(getActivity());
                        ToastUtils.show(getActivity(), "清除成功");
                        String size = DataClearmanager.getTotalCacheSize(getActivity());
                        mPreferenceCache.setSummary(size);
                    } catch (Exception e) {
                        //清除失败
                        ToastUtils.show(getActivity(), "清除失败");
                        e.printStackTrace();
                    }
                });
                break;
            case "key_update":
                Beta.checkUpgrade();
                break;
            case "key_timing":
                if (mShutdownScheduled) {
                    updateTimeSwitch(false);
                } else {
                    mTimingSwitch.setChecked(false);
                    openDialog();
                }
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
        if (!SystemUtils.isOpenSystemWindow() && SystemUtils.isMarshmallow()) {
            ToastUtils.show(getActivity(), "请手动打开显示悬浮窗权限");
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, 100);
        } else if (!SystemUtils.isOpenUsageAccess() && SystemUtils.isMarshmallow()) {
            ToastUtils.show(getActivity(), "获取<有权查看使用权限的应用>权限");
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, 101);
        } else {
            mLyricCheckBox.setChecked(true);
            ToastUtils.show(getActivity(), "显示悬浮窗权限已开通");
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
        if (requestCode == 100) {
            if (SystemUtils.isOpenSystemWindow()) {
                checkLyricPermission();
            } else {
                ToastUtils.show(MusicApp.getAppContext(), "悬浮窗权限已被拒绝！");
            }
        } else if (requestCode == 101) {
            if (SystemUtils.isOpenUsageAccess()) {
                checkLyricPermission();
            } else {
                ToastUtils.show(MusicApp.getAppContext(), "有权查看使用权限的应用权限已被拒绝！");
            }
        }
    }
}
