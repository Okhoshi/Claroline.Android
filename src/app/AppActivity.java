package app;

import java.lang.reflect.Field;

import net.claroline.mobile.android.R;

import org.joda.time.DateTime;

import activity.Settings;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.SearchView;
import connectivity.ClarolineClient;
import connectivity.ClarolineClient.OnAccountStateChangedListener;
import connectivity.ClarolineService;
import fragments.AboutDialog;

/**
 * Claroline Mobile - Android
 * 
 * Global Activity overrode by all others (if possible).
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public abstract class AppActivity extends FragmentActivity implements
		OnAccountStateChangedListener {

	/**
	 * Maximum bound of Activity ProgressBar.
	 */
	private static final int MAX_PROGRESS_BAR_ACTIVITY = 10000;

	/**
	 * SavedInstanceState key.
	 */
	private static final String SIS_LAST_UPDATE = "lastUpdate";

	/**
	 * The time of last update of the data.
	 */
	private DateTime mLastUpdate;
	/**
	 * Menu instance.
	 */
	private Menu mMenu;

	/**
	 * The progress dialog always present on these activity.
	 */
	private ProgressDialog mProgress;

	/**
	 * Web Service Client instance.
	 */
	private ClarolineService mService;

	/**
	 * Current max for the ProgressBar. Only used when on API level > 14.
	 */
	private int mMax;

	public static final int ONCE_PER_DAY = 24;

	/**
	 * @return the mService
	 */
	public ClarolineService getService() {
		return mService;
	}

	/**
	 * @param value
	 *            the value to set to the ProgressBar.
	 */
	public void incrementProgress(final int value) {
		if (App.isNewerAPI(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
			setProgress(value / mMax * MAX_PROGRESS_BAR_ACTIVITY);
		} else {
			if (mProgress != null && mProgress.isShowing()
					&& !mProgress.isIndeterminate()) {
				mProgress.incrementProgressBy(value - mProgress.getProgress());
			}
		}
	}

	/**
	 * @param delay
	 *            the validity of data in hours
	 * @return if the data have to be refreshed
	 */
	public boolean mustUpdate(final int delay) {
		return mLastUpdate.plusHours(delay).isBeforeNow();
	}

	@Override
	public void onAccountStateChange(final boolean validity) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mMenu != null) {
					mMenu.findItem(R.id.menu_login).setVisible(!validity)
							.setEnabled(!validity);
					mMenu.findItem(R.id.menu_logout).setVisible(validity)
							.setEnabled(validity);
					mMenu.findItem(R.id.menu_refresh).setVisible(validity)
							.setEnabled(validity);
				}
			}
		});
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
		mService = new ClarolineService();

		if (savedInstanceState != null
				&& savedInstanceState.containsKey(SIS_LAST_UPDATE)) {
			mLastUpdate = new DateTime(
					savedInstanceState.getLong(SIS_LAST_UPDATE));
		} else {
			mLastUpdate = new DateTime(0);
		}
		super.onCreate(savedInstanceState);

		if (App.isNewerAPI(Build.VERSION_CODES.HONEYCOMB)) {
			setActionBar(true);
		}
		setOverflowMenu();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);

		if (ClarolineClient.isValidAccount()) {
			menu.findItem(R.id.menu_login).setVisible(false).setEnabled(false);
		} else {
			menu.findItem(R.id.menu_logout).setVisible(false).setEnabled(false);
			menu.findItem(R.id.menu_refresh).setVisible(false)
					.setEnabled(false);
		}

		if (App.isNewerAPI(Build.VERSION_CODES.HONEYCOMB)) {
			// Get the SearchView and set the searchable configuration
			SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu
					.findItem(R.id.menu_search).getActionView();
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(false);
			searchView.setSubmitButtonEnabled(true);
		}
		mMenu = menu;

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			AboutDialog about = new AboutDialog();
			about.show(getSupportFragmentManager(), "about");
			break;
		case R.id.menu_search:
			onSearchRequested();
			break;
		case R.id.menu_settings:
			Intent si = new Intent(this, Settings.class);
			startActivity(si);
			break;
		case R.id.menu_logout:
			ClarolineClient.getInstance().invalidateClient();
			// TODO Reset DB.
			break;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			break;
		case R.id.menu_refresh:
		case R.id.menu_login:
			// Comportement du bouton "Rafraichir" et du bouton "Se connecter"
			// Doit �tre impl�menter dans chaque activit� si besoin
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onPause() {
		super.onPause();
		ClarolineClient.unregisterOnAccountStateChangedListener(this);
	}

	@Override
	public void onResume() {
		ClarolineClient.registerOnAccountStateChangedListener(this);
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		outState.putLong(SIS_LAST_UPDATE, mLastUpdate.getMillis());
		super.onSaveInstanceState(outState);
	}

	public abstract void refreshUI();

	/**
	 * Sets up the {@link ActionBar}.
	 * 
	 * @param displayHomeAsUp
	 *            displays the Up action
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setActionBar(final boolean displayHomeAsUp) {
		if (App.isNewerAPI(Build.VERSION_CODES.HONEYCOMB)) {
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUp);
		}
		onAccountStateChange(ClarolineClient.isValidAccount());
	}

	public void setOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

	}

	public void setProgressIndicator(final boolean visible) {
		setProgressIndicator(visible,
				getResources().getString(R.string.loading_default), true, 0);
	}

	public void setProgressIndicator(final boolean visible,
			final boolean isIndeterminate, final int max) {
		setProgressIndicator(visible,
				getResources().getString(R.string.loading_default),
				isIndeterminate, max);
	}

	public void setProgressIndicator(final boolean visible,
			final String message, final boolean isIndeterminate, final int max) {
		if (App.isNewerAPI(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
			setProgressBarVisibility(visible);
			setProgressBarIndeterminate(isIndeterminate);
			mMax = max;
		} else {
			if (visible) {
				if (mProgress == null) {
					mProgress = new ProgressDialog(this);
					mProgress.setCancelable(true);
					mProgress.setIndeterminate(isIndeterminate);
				} else if (mProgress.isIndeterminate() != isIndeterminate) {
					mProgress.dismiss();
					mProgress = new ProgressDialog(mProgress.getContext());
					mProgress.setCancelable(true);
					mProgress.setIndeterminate(isIndeterminate);
				}
				if (!isIndeterminate) {
					mProgress.setMax(max);
					mProgress.setProgress(0);
					mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				}
				mProgress.setMessage(message);
				if (!mProgress.isShowing()) {
					mProgress.show();
				}
			} else if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setProgressIndicator(final boolean visible,
			final String message, final boolean isIndeterminate, final int max,
			final String format) {
		setProgressIndicator(visible, message, isIndeterminate, max);
		if (App.isNewerAPI(Build.VERSION_CODES.HONEYCOMB)) {
			mProgress.setProgressNumberFormat(format);
		}
	}

	/**
	 * Refresh the LastUpdate counter.
	 */
	public void updatesNow() {
		mLastUpdate = DateTime.now();
	}
}
