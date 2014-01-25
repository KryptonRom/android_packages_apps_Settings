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

import java.util.List;


public class NavigationBar extends SettingsPreferenceFragment implements
         Preference.OnPreferenceChangeListener {
			 
 
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
 
         addPreferencesFromResource(R.xml.navigation_bar_settings);
         
       
        
	}
 
     @Override
     public boolean onPreferenceChange(Preference preference, Object newValue) {
		 return false;
 }
