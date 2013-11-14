package com.android.settings.AOSPAL;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.util.paranoid.DeviceUtils;

public class StatusBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String QUICK_PULLDOWN = "quick_pulldown";
    private static final String KEY_CATEGORY_QS_STATUSBAR = "qs_statusbar";
    private static final String SMART_PULLDOWN = "smart_pulldown";
    private static final String DOUBLE_TAP_SLEEP_GESTURE = "double_tap_sleep_gesture";


    ListPreference mQuickPulldown;
	ListPreference mSmartPulldown;
	private CheckBoxPreference mStatusBarDoubleTapSleepGesture;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.statusbar_settings);
        PreferenceScreen prefs = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        mQuickPulldown = (ListPreference) findPreference(QUICK_PULLDOWN);
        mSmartPulldown = (ListPreference) findPreference(SMART_PULLDOWN);
        
        mQuickPulldown.setOnPreferenceChangeListener(this);
        int statusQuickPulldown = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.QS_QUICK_PULLDOWN, 1, UserHandle.USER_CURRENT);
        mQuickPulldown.setValue(String.valueOf(statusQuickPulldown));
        updateQuickPulldownSummary(statusQuickPulldown);

        // Smart Pulldown
        mSmartPulldown.setOnPreferenceChangeListener(this);
        int smartPulldown = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.QS_SMART_PULLDOWN, 0, UserHandle.USER_CURRENT);
        mSmartPulldown.setValue(String.valueOf(smartPulldown));
        updateSmartPulldownSummary(smartPulldown);
        
        mStatusBarDoubleTapSleepGesture = (CheckBoxPreference) findPreference(DOUBLE_TAP_SLEEP_GESTURE);
        mStatusBarDoubleTapSleepGesture.setChecked(Settings.System.getInt(getContentResolver(),
            Settings.System.DOUBLE_TAP_SLEEP_GESTURE, 0) == 1);
        mStatusBarDoubleTapSleepGesture.setOnPreferenceChangeListener(this);

        PreferenceCategory qsStatusbar =
            (PreferenceCategory) findPreference(KEY_CATEGORY_QS_STATUSBAR);
        if (!DeviceUtils.isPhone(getActivity())) {
            qsStatusbar.removePreference(findPreference(QUICK_PULLDOWN));
            qsStatusbar.removePreference(findPreference(SMART_PULLDOWN));
        }
    }

    private boolean isToggled(Preference pref) {
        return ((CheckBoxPreference) pref).isChecked();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final String key = preference.getKey();
		ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mQuickPulldown) {
            int statusQuickPulldown = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getContentResolver(), Settings.System.QS_QUICK_PULLDOWN,
                    statusQuickPulldown, UserHandle.USER_CURRENT);
            updateQuickPulldownSummary(statusQuickPulldown);
            return true;
        } else if (preference == mSmartPulldown) {
            int smartPulldown = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.QS_SMART_PULLDOWN,
                    smartPulldown);
            updateSmartPulldownSummary(smartPulldown);
        } else if (preference == mStatusBarDoubleTapSleepGesture) {
			boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                Settings.System.DOUBLE_TAP_SLEEP_GESTURE, value ? 1 : 0);
		} else {
            return false;
        }
        return true;
     }

        private void updateQuickPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // quick pulldown deactivated
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_off));
        } else {
            String direction = res.getString(value == 2
                    ? R.string.quick_pulldown_left
                    : R.string.quick_pulldown_right);
            mQuickPulldown.setSummary(res.getString(R.string.summary_quick_pulldown, direction));
        }
    }
    
    private void updateSmartPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // Smart pulldown deactivated
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_off));
        } else {
            String type = res.getString(value == 2
                    ? R.string.smart_pulldown_persistent
                    : R.string.smart_pulldown_dismissable);
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_summary, type));
        }
    }
}

