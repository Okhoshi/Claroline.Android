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

	private static final String AUTH_PLATFORM_TEXT = "platformTextAuth";
	private static final String ANON_PLATFORM_TEXT = "platformTextAnonym";
	private TextView authView;
	private TextView anonView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.context_frag, container, false);

		anonView = (TextView) view.findViewById(R.id.anonymous_platform_text);
		authView = (TextView) view.findViewById(R.id.auth_platform_text);
		
		authView.setText(GlobalApplication.getPreferences().getString(AUTH_PLATFORM_TEXT,""));
		anonView.setText(GlobalApplication.getPreferences().getString(ANON_PLATFORM_TEXT, ""));
		
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
		if(key.equals(AUTH_PLATFORM_TEXT)){
			authView.setText(GlobalApplication.getPreferences().getString(key,""));
		} else if(key.equals(ANON_PLATFORM_TEXT)){
			anonView.setText(GlobalApplication.getPreferences().getString(key, ""));
		}
	}
}
