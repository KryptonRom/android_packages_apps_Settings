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

public class Lockscreen extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String LOCKSCREEN_POWER_MENU = "lockscreen_power_menu";
    
    private CheckBoxPreference mLockScreenPowerMenu;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lockscreen_settings);
        PreferenceScreen prefs = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();
       
        mLockScreenPowerMenu = (CheckBoxPreference) prefs.findPreference(LOCKSCREEN_POWER_MENU);
        if (mLockScreenPowerMenu != null) {
            mLockScreenPowerMenu.setChecked(Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.LOCK_SCREEN_POWER_MENU, 1) == 1);
        }
        
    }


    private boolean isToggled(Preference pref) {
        return ((CheckBoxPreference) pref).isChecked();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		if (preference == mLockScreenPowerMenu) {
            Settings.Secure.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.Secure.LOCK_SCREEN_POWER_MENU, isToggled(preference) ? 1 : 0);                  
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
    
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}

