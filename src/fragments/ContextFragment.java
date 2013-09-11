/**
 * Claroline Mobile - Android
 * 
 * @package     fragments
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
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
import app.AppActivity;

/**
 * Claroline Mobile - Android
 * 
 * Fragment showing user related infos.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
public class ContextFragment extends Fragment implements
		OnSharedPreferenceChangeListener {

	/**
	 * UI reference.
	 */
	private TextView mFirstLastView;

	/**
	 * UI reference.
	 */
	private TextView mNomaView;

	/**
	 * UI reference.
	 */
	private ImageView mUserPicture;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.context_frag, container, false);

		mFirstLastView = (TextView) view.findViewById(R.id.firstLastName);
		mNomaView = (TextView) view.findViewById(R.id.NOMA);
		mUserPicture = (ImageView) view.findViewById(R.id.user_image);

		mFirstLastView.setText(getString(R.string.welcome, App.getPrefs()
				.getString(App.SETTINGS_FIRST_NAME, ""), App.getPrefs()
				.getString(App.SETTINGS_LAST_NAME, "")));
		mNomaView.setText(App.getPrefs().getString(App.SETTINGS_OFFICIAL_CODE,
				""));

		String pic64 = App.getPrefs().getString(App.SETTINGS_USER_IMAGE, "");
		if (!pic64.equals("")) {
			byte[] pic = Base64.decode(pic64, Base64.DEFAULT);
			mUserPicture.setImageBitmap(BitmapFactory.decodeByteArray(pic, 0,
					pic.length));
		} else {
			mUserPicture.setImageResource(R.drawable.nopicture);
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
		if (key.equals(App.SETTINGS_FIRST_NAME)
				|| key.equals(App.SETTINGS_LAST_NAME)) {
			mFirstLastView.setText(getString(R.string.welcome, App.getPrefs()
					.getString(App.SETTINGS_FIRST_NAME, ""), App.getPrefs()
					.getString(App.SETTINGS_LAST_NAME, "")));
		} else if (key.equals(App.SETTINGS_PLATFORM_NAME)) {
			((AppActivity) getActivity()).refreshUI();
		} else if (key.equals(App.SETTINGS_OFFICIAL_CODE)) {
			mNomaView.setText(App.getPrefs().getString(key, ""));
		} else if (key.equals(App.SETTINGS_INSTITUTION_NAME)) {
			((AppActivity) getActivity()).refreshUI();
		} else if (key.equals(App.SETTINGS_USER_IMAGE)) {
			String pic64 = App.getPrefs()
					.getString(App.SETTINGS_USER_IMAGE, "");
			if (!pic64.equals("")) {
				byte[] pic = Base64.decode(pic64, Base64.DEFAULT);
				mUserPicture.setImageBitmap(BitmapFactory.decodeByteArray(pic,
						0, pic.length));
			} else {
				mUserPicture.setImageResource(R.drawable.nopicture);
			}
		}
	}
}
