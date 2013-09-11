/**
 * Claroline Mobile - Android
 * 
 * @package     activity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package activity;

import java.util.Arrays;

import net.claroline.mobile.android.R;
import util.Tools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import app.AppActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;
import fragments.AnnonceDetailFragment;
import fragments.DetailFragment;
import fragments.GenericDetailFragment;

/**
 * Claroline Mobile - Android
 * 
 * Details activity.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
public class DetailsActivity extends AppActivity {

	/**
	 * @param label
	 *            the requested module label
	 * @return the fragment class name for the label module
	 */
	private static String getDetailFragmentName(final String label) {
		if (Arrays.asList(Tools.enumValuesToStrings(SupportedModules.values()))
				.contains(label)) {
			switch (SupportedModules.valueOf(label)) {
			case CLANN:
				return AnnonceDetailFragment.class.getName();
			default:
				return GenericDetailFragment.class.getName();
			}
		} else {
			return GenericDetailFragment.class.getName();
		}
	}

	/**
	 * Content fragment.
	 */
	private DetailFragment mFragment;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			Bundle args = new Bundle(extras);

			mFragment = (DetailFragment) Fragment.instantiate(this,
					getDetailFragmentName(extras.getString("label")), args);
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mFragment, "resource_detail")
					.commit();
		} else {
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			if (mFragment.isExpired()) {
				setProgressIndicator(true);
				mFragment.refreshResource(new AsyncHttpResponseHandler() {
					@Override
					public void onFinish() {
						setProgressIndicator(false);
					}

					@Override
					public void onSuccess(final String content) {
						refreshUI();
						updatesNow();
					}
				});
			} else {
				getService().getUpdates(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(final String content) {
						if (!content.equals("[]")) {
							refreshUI();
						}
						updatesNow();
					}
				});
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void refreshUI() {
		mFragment.refreshUI();
	}
}
