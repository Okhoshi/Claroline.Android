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

import java.util.List;

import model.Cours;
import net.claroline.mobile.android.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import app.App;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.ClarolineClient;
import fragments.CoursListFragment;
import fragments.LoginDialog;
import fragments.NewsFragment;
import fragments.ToolViewPagerFragment;

/**
 * Claroline Mobile - Android
 * 
 * Home activity class.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class HomeActivity extends AppActivity {

	/**
	 * Numeric constant.
	 */
	private static final int SIX_HOURS = 6;

	@Override
	public void onAccountStateChange(final boolean validity) {
		super.onAccountStateChange(validity);

		if (!validity && App.isTwoPane()) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.content_fragment, new Fragment());
			ft.commit();
		} else if (validity && App.isTwoPane()) {
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.content_fragment, new NewsFragment());
			ft.commit();
		}
	}

	@Override
	public void onBackPressed() {
		if (App.isTwoPane()) {
			Fragment f = getSupportFragmentManager().findFragmentById(
					R.id.content_fragment);
			if (f instanceof ToolViewPagerFragment) {
				((CoursListFragment) getSupportFragmentManager()
						.findFragmentById(R.id.list_frag)).dismissSelection();
				return;
			}
		}
		super.onBackPressed();
	}

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the precedent saved state
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		setActionBar(false);
		refreshUI();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			refresh(true, false, false);
			return true;
		case R.id.menu_login:
			refresh(true, true, false);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh(false, false, false);
	}

	@Override
	public void refreshUI() {
		setTitle(
				App.getPrefs().getString(App.SETTINGS_PLATFORM_NAME,
						getString(R.string.app_name)),
				getString(R.string.actionbar_subtitle, App.getPrefs()
						.getString(App.SETTINGS_INSTITUTION_NAME, "Claroline")));
		CoursListFragment list = (CoursListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.list_frag);
		if (list != null) {
			list.refreshUI();
		}
	}

	/**
	 * Refreshes.
	 * 
	 * @param force
	 *            force the list refresh
	 * @param forceUser
	 *            force the user data refresh
	 * @param noDialog
	 *            cannot show the login dialog if the user is not logged in
	 */
	private void refresh(final boolean force, final boolean forceUser,
			final boolean noDialog) {
		if (App.getPrefs().getString(App.SETTINGS_PLATFORM_HOST, "").equals("")) {
			Intent i = new Intent(this, Settings.class);
			startActivity(i);
		}

		if (!noDialog && !ClarolineClient.isValidAccount()) {
			showLoginDialog();
		} else if (ClarolineClient.isValidAccount()) {
			if (!App.getPrefs().contains(App.SETTINGS_FIRST_NAME) || forceUser
					|| mustUpdate(SIX_HOURS) && !force) {
				getService().getUserData(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(final String response) {
						refresh(true, false, false);
					}
				});
			} else if (force || mustUpdate(1)) {
				setProgressIndicator(true);
				if (new Select().from(Cours.class).execute().size() == 0) {
					getService().getCourseList(new AsyncHttpResponseHandler() {
						@Override
						public void onFinish() {
							setProgressIndicator(false);
						}

						@Override
						public void onSuccess(final String response) {
							refreshUI();
							setProgressIndicator(false);
							updatesNow();

							List<Cours> list = new Select().from(Cours.class)
									.execute();
							for (Cours cours : list) {
								getService().getToolListForCours(cours,
										new AsyncHttpResponseHandler());
							}
						}
					});
				} else {
					getService().getUpdates(new AsyncHttpResponseHandler() {
						@Override
						public void onFinish() {
							setProgressIndicator(false);
						}

						@Override
						public void onSuccess(final String response) {
							if (!response.equals("[]")) {
								refreshUI();
							}
							updatesNow();
						}
					});
				}
			}
		}
	}

	/**
	 * Shows the login Dialog.
	 */
	private void showLoginDialog() {
		LoginDialog login = new LoginDialog(this);
		login.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(final DialogInterface dialog) {
				if (ClarolineClient.isValidAccount()) {
					HomeActivity.this.refresh(true, true, false);
				}
			}
		});
		login.show();
	}
}
