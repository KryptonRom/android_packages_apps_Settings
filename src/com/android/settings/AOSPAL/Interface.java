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

public class Interface extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";
    private static final String KEY_DUAL_PANEL = "force_dualpanel";
    private static final String KEY_REVERSE_DEFAULT_APP_PICKER = "reverse_default_app_picker"; 

    private CheckBoxPreference mDualPanel;
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;
    private CheckBoxPreference mReverseDefaultAppPicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.interface_settings);
        PreferenceScreen prefs = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        //ListView Animations
        mListViewAnimation = (ListPreference) prefs.findPreference(KEY_LISTVIEW_ANIMATION);
        if (mListViewAnimation != null) {
           int listViewAnimation = Settings.System.getInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION, 1);
           mListViewAnimation.setValue(String.valueOf(listViewAnimation));
           mListViewAnimation.setSummary(mListViewAnimation.getEntry());
        }
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) prefs.findPreference(KEY_LISTVIEW_INTERPOLATOR);
        if (mListViewInterpolator != null) {
           int listViewInterpolator = Settings.System.getInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR, 1);
           mListViewInterpolator.setValue(String.valueOf(listViewInterpolator));
           mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
        }
        mListViewInterpolator.setOnPreferenceChangeListener(this);

        mDualPanel = (CheckBoxPreference) findPreference(KEY_DUAL_PANEL);
        mDualPanel.setChecked(Settings.System.getBoolean(getContentResolver(), Settings.System.FORCE_DUAL_PANEL, false));
        
        mReverseDefaultAppPicker = (CheckBoxPreference) findPreference(KEY_REVERSE_DEFAULT_APP_PICKER);
        mReverseDefaultAppPicker.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.REVERSE_DEFAULT_APP_PICKER, 0) != 0);
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

		if (KEY_LISTVIEW_ANIMATION.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewAnimation.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION,
                    value);
            mListViewAnimation.setValue(String.valueOf(value));
            mListViewAnimation.setSummary(mListViewAnimation.getEntry());
        } else if (KEY_LISTVIEW_INTERPOLATOR.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewInterpolator.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR,
                    value);
            mListViewInterpolator.setValue(String.valueOf(value));
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
        }
        return false;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		if (preference == mDualPanel) {
            Settings.System.putBoolean(getContentResolver(), Settings.System.FORCE_DUAL_PANEL, ((CheckBoxPreference) preference).isChecked());
            return true; 
        } else if (preference == mReverseDefaultAppPicker) {
            Settings.System.putInt(getContentResolver(), Settings.System.REVERSE_DEFAULT_APP_PICKER,
                    mReverseDefaultAppPicker.isChecked() ? 1 : 0);
		}
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}

