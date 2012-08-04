/**
 * 
 */
package activity;

import mobile.claroline.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Quentin
 *
 */
public class Preference extends PreferenceActivity {

	/**
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

}
