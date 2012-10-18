/**
 * 
 */
package activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import mobile.claroline.R;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import app.GlobalApplication;

/**
 * @author Quentin
 *
 */
public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	private ArrayList<String> onScreenSettings = new ArrayList<String>(Arrays.asList(new String[]
								{"platform_host", "platform_module", "user_login"}));
	
	/**
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setActionBar();
		
		Map<String,?> keys = GlobalApplication.getPreferences().getAll();

		for(Map.Entry<String,?> entry : keys.entrySet()){
			if(onScreenSettings.contains(entry.getKey())){
		            Log.d("map values",entry.getKey() + ": " + 
		                                   entry.getValue().toString());
		            Preference Pref = findPreference(entry.getKey());
		            // Set summary to be the user-description for the selected value
		            Pref.setSummary(GlobalApplication.getPreferences().getString(entry.getKey(), ""));
			}
		 }

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
		
		 @Override
		    public boolean onOptionsItemSelected(MenuItem item) {
		        switch (item.getItemId()) {
		        case android.R.id.home:
		        	// Comportement du bouton qui permet de retourner a l'activite precedente
		        	Intent monIntent = new Intent(this,home.class);
		        	startActivity(monIntent);
		        	return true;
		        default:
		            return super.onOptionsItemSelected(item);
		        }
		 }

}
