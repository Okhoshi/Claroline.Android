/**
 * 
 */
package activity;

import mobile.claroline.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

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

}
