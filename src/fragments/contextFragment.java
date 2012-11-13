package fragments;

import mobile.claroline.R;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.GlobalApplication;

public class contextFragment extends Fragment implements OnSharedPreferenceChangeListener {

	//private static final String AUTH_PLATFORM_TEXT = "platformTextAuth";
	//private static final String ANON_PLATFORM_TEXT = "platformTextAnonym";
	
	private static final String PLATFORM_NAME = "platformName";
	private static final String INSTITUTION_NAME = "institutionName";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String NOMA = "officialCode";

	private TextView platformView;
	private TextView institutionView;
	private TextView firstLastView;
	private TextView nomaView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.context_frag, container, false);

		platformView = (TextView) view.findViewById(R.id.platformName);
		institutionView = (TextView) view.findViewById(R.id.institutionName);
		firstLastView = (TextView) view.findViewById(R.id.firstLastName);
		nomaView = (TextView) view.findViewById(R.id.NOMA);
		
		platformView.setText(GlobalApplication.getPreferences().getString(PLATFORM_NAME,""));
		institutionView.setText(GlobalApplication.getPreferences().getString(INSTITUTION_NAME,""));
		firstLastView.setText(GlobalApplication.getPreferences().getString(FIRST_NAME,"")+" "+GlobalApplication.getPreferences().getString(LAST_NAME,""));	
		nomaView.setText(GlobalApplication.getPreferences().getString(NOMA,""));
		
		return view;
	}

	@Override
	public void onResume() {
	    super.onResume();
	    GlobalApplication.getPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    GlobalApplication.getPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreference, String key) {
		if(key.equals(FIRST_NAME)||key.equals(LAST_NAME)){
			firstLastView.setText(GlobalApplication.getPreferences().getString(key,""));
		} else if(key.equals(PLATFORM_NAME)){
			platformView.setText(GlobalApplication.getPreferences().getString(key, ""));
		} else if(key.equals(NOMA)){
			nomaView.setText(GlobalApplication.getPreferences().getString(key, ""));
		}else if(key.equals(NOMA)){
			institutionView.setText(GlobalApplication.getPreferences().getString(key, ""));
		}
	}
}
