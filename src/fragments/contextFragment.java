package fragments;

import net.claroline.mobile.android.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import app.App;

public class contextFragment extends Fragment implements
		OnSharedPreferenceChangeListener {

	// private static final String AUTH_PLATFORM_TEXT = "platformTextAuth";
	// private static final String ANON_PLATFORM_TEXT = "platformTextAnonym";

	private static final String PLATFORM_NAME = "platformName";
	private static final String INSTITUTION_NAME = "institutionName";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String NOMA = "officialCode";
	private static final String USER_IMAGE = "userImage";

	private TextView platformView;
	private TextView institutionView;
	private TextView firstLastView;
	private TextView nomaView;
	private ImageView userPicture;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.context_frag, container, false);

		platformView = (TextView) view.findViewById(R.id.platformName);
		institutionView = (TextView) view.findViewById(R.id.institutionName);
		firstLastView = (TextView) view.findViewById(R.id.firstLastName);
		nomaView = (TextView) view.findViewById(R.id.NOMA);
		userPicture = (ImageView) view.findViewById(R.id.user_image);

		platformView.setText(App.getPrefs().getString(PLATFORM_NAME, ""));
		institutionView.setText(App.getPrefs().getString(
				INSTITUTION_NAME, ""));
		firstLastView.setText(App.getPrefs().getString(FIRST_NAME, "")
				+ " " + App.getPrefs().getString(LAST_NAME, ""));
		nomaView.setText(App.getPrefs().getString(NOMA, "") + "");
		String pic64;
		if (!(pic64 = App.getPrefs().getString(USER_IMAGE, ""))
				.equals("")) {
			byte[] pic = Base64.decode(pic64, Base64.DEFAULT);
			userPicture.setImageBitmap(BitmapFactory.decodeByteArray(pic, 0,
					pic.length));
		} else {
			userPicture.setImageResource(R.drawable.nopicture);
		}

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		App.getPrefs().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		App.getPrefs().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(
			final SharedPreferences sharedPreference, final String key) {
		if (key.equals(FIRST_NAME) || key.equals(LAST_NAME)) {
			firstLastView.setText(App.getPrefs()
					.getString(FIRST_NAME, "")
					+ " "
					+ App.getPrefs().getString(LAST_NAME, ""));
		} else if (key.equals(PLATFORM_NAME)) {
			platformView.setText(App.getPrefs().getString(key, ""));
		} else if (key.equals(NOMA)) {
			nomaView.setText(App.getPrefs().getString(key, ""));
		} else if (key.equals(INSTITUTION_NAME)) {
			institutionView.setText(App.getPrefs().getString(key, ""));
		} else if (key.equals(USER_IMAGE)) {
			String pic64;
			if (!(pic64 = App.getPrefs().getString(USER_IMAGE, ""))
					.equals("")) {
				byte[] pic = Base64.decode(pic64, Base64.DEFAULT);
				userPicture.setImageBitmap(BitmapFactory.decodeByteArray(pic,
						0, pic.length));
			} else {
				userPicture.setImageResource(R.drawable.nopicture);
			}
		}
	}
}
