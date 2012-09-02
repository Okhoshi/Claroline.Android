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
		setOverflowMenu();
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
	 
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.menu_about:
	            // Comportement du bouton "A Propos"
	        	Intent monIntent = new Intent(this,about_us.class);
	        	startActivity(monIntent);
	            return true;
	        case R.id.menu_help:
	            // Comportement du bouton "Aide"
	            return true;
	        case R.id.menu_refresh:
	            // Comportement du bouton "Rafraichir"
	            return true;
	        case R.id.menu_search:
	            // Comportement du bouton "Recherche"
	        	Intent monIntent1 = new Intent(this,searchableActivity.class);
	        	startActivity(monIntent1);
	        	//onSearchRequested();
	            return true;
	        case R.id.menu_settings:
	        	Intent settings_intent = new Intent(this, Settings.class);
	        	startActivity(settings_intent);
	        	return true;
	        case android.R.id.home:
	        	// Comportement du bouton qui permet de retourner a l'activite precedente
	        	monIntent = new Intent(this,home.class);
	        	startActivity(monIntent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
	 
	 
	// Met les propriétés de l'action bar
		public void setActionBar()
		{
			ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true); 
		}
		
		public void setOverflowMenu()
	    {
	    	try {
	            ViewConfiguration config = ViewConfiguration.get(this);
	            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	            if(menuKeyField != null) {
	                menuKeyField.setAccessible(true);
	                menuKeyField.setBoolean(config, false);
	            }
	        } catch (Exception ex) {
	            // Ignore
	        }

	    }

}
