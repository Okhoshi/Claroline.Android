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
import android.util.Log;
import android.view.MenuItem;
import app.GlobalApplication;

/**
 * @author Quentin
 *
 */
public class Settings extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	public static final String PLATFORM_HOST = "platform_host";
	public static final String PLATFORM_MODULE = "platform_module";
	
	private ArrayList<String> onScreenSettings = new ArrayList<String>(Arrays.asList(new String[]
								{PLATFORM_HOST, PLATFORM_MODULE}));
	
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
		 	if (!key.contains("password")){
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
		        	Intent monIntent = new Intent(this,HomeActivity.class);
					monIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
							Intent.FLAG_ACTIVITY_NEW_TASK);
		        	startActivity(monIntent);
		        	return true;
		        default:
		            return super.onOptionsItemSelected(item);
		        }
		 }

}
