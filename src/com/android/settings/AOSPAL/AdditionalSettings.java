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
import com.android.settings.Utils;

public class AdditionalSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String CATEGORY_HEADSETHOOK = "button_headsethook";
    private static final String BUTTON_HEADSETHOOK_LAUNCH_VOICE = "button_headsethook_launch_voice";
	private static final String RECENT_MENU_CLEAR_ALL = "recent_menu_clear_all";
    private static final String RECENT_MENU_CLEAR_ALL_LOCATION = "recent_menu_clear_all_location";

    private CheckBoxPreference mRecentClearAll;
    private ListPreference mRecentClearAllPosition;
    private CheckBoxPreference mHeadsetHookLaunchVoice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.additional_settings);
        
        PreferenceScreen prefs = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        final PreferenceCategory headsethookCategory =
                (PreferenceCategory) prefs.findPreference(CATEGORY_HEADSETHOOK);

        mHeadsetHookLaunchVoice = (CheckBoxPreference) findPreference(BUTTON_HEADSETHOOK_LAUNCH_VOICE);
        mHeadsetHookLaunchVoice.setChecked(Settings.System.getInt(resolver,
                Settings.System.HEADSETHOOK_LAUNCH_VOICE, 1) == 1);
        mRecentClearAll = (CheckBoxPreference) prefs.findPreference(RECENT_MENU_CLEAR_ALL);
        mRecentClearAll.setChecked(Settings.System.getInt(resolver,
            Settings.System.SHOW_CLEAR_RECENTS_BUTTON, 1) == 1);
        mRecentClearAll.setOnPreferenceChangeListener(this);
        mRecentClearAllPosition = (ListPreference) prefs.findPreference(RECENT_MENU_CLEAR_ALL_LOCATION);
        String recentClearAllPosition = Settings.System.getString(resolver, Settings.System.CLEAR_RECENTS_BUTTON_LOCATION);
        if (recentClearAllPosition != null) {
             mRecentClearAllPosition.setValue(recentClearAllPosition);
        }
        mRecentClearAllPosition.setOnPreferenceChangeListener(this);
    }        
           
    private boolean isToggled(Preference pref) {
        return ((CheckBoxPreference) pref).isChecked();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStatusBarBrightnessControl();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mRecentClearAll) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.SHOW_CLEAR_RECENTS_BUTTON, value ? 1 : 0);
        } else if (preference == mRecentClearAllPosition) {
            String value = (String) newValue;
            Settings.System.putString(resolver, Settings.System.CLEAR_RECENTS_BUTTON_LOCATION, value);
        } else {
            return false;
        }

        return true;
    }
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mHeadsetHookLaunchVoice) {
            boolean checked = ((CheckBoxPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.HEADSETHOOK_LAUNCH_VOICE, checked ? 1:0);
            return true;
        } 
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
