/**
 * 
 */
package activity;

import java.lang.reflect.Field;

import mobile.claroline.R;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.view.ViewConfiguration;

/**
 * @author Quentin
 *
 */
public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	/**
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setActionBar();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}
	
	 public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		 	if (!key.equals("user_password")){
	            Preference Pref = findPreference(key);
	            // Set summary to be the user-description for the selected value
	            Pref.setSummary(sharedPreferences.getString(key, ""));
		 	}
	    }
	 
	 
	 
	// Met les propriétés de l'action bar
		public void setActionBar()
		{
			ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true); 
		}
		

}
