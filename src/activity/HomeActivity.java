package activity;

import java.util.List;

import model.Cours;
import net.claroline.mobile.android.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import app.App;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.ClarolineClient;
import fragments.LoginDialog;
import fragments.CoursListFragment;

public class HomeActivity extends AppActivity {

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
		refresh(false, false, false);
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

	/*
	 * 
	 * Menus,tabs,actionBar
	 */

	public void onRepositoryRefresh() {
		CoursListFragment list = (CoursListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.list_frag);
		if (list != null) {
			list.refreshUI();
		}
	}

	private void refresh(final boolean force, final boolean forceUser,
			final boolean noDialog) {
		if (App.getPrefs().getString(App.SETTINGS_PLATFORM_HOST, "").equals("")) {
			Intent i = new Intent(this, Settings.class);
			startActivity(i);
		}

		if (!noDialog && !ClarolineClient.isValidAccount()) {
			showLoginDialog();
		} else {
			if (forceUser || mustUpdate(6) && !force) {
				getService().getUserData(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(final String response) {
						refresh(true, false, false);
					}
				});
			} else if (force || mustUpdate(1)) {
				setProgressIndicator(true);
				if (new Select("Id").from(Cours.class).execute().size() == 0) {
					getService().getCourseList(new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(final String response) {
							onRepositoryRefresh();
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
						public void onSuccess(final String response) {
							if (!response.equals("[]")) {
								onRepositoryRefresh();
							}
							setProgressIndicator(false);
							updatesNow();
						}
					});
				}
			}
		}
	}

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
