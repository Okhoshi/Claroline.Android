package app;

import activity.HomeActivity;
import net.claroline.mobile.android.R;
import activity.Settings;
import activity.about_us;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.SearchView;
import connectivity.ClaroClient;
import connectivity.ClaroClient.onAccountStateChangedListener;
import dataStorage.IRepository.RepositoryRefreshListener;
import dataStorage.Repository;
import net.claroline.mobile.android.R;

import java.lang.reflect.Field;
import java.util.GregorianCalendar;

public abstract class AppActivity extends Activity implements RepositoryRefreshListener, onAccountStateChangedListener { 

	private boolean dbOpenHere = false;
	public Handler handler = new AppHandler(this);
	private Menu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.d("DB", "DB Open in onCreate");
		Repository.Open();
		dbOpenHere = true;

		lastUpdate = new GregorianCalendar();
		if(savedInstanceState != null && savedInstanceState.containsKey("lastUpdate")){
			lastUpdate.setTimeInMillis(savedInstanceState.getLong("lastUpdate"));
		} else {
			lastUpdate.setTimeInMillis(0);
		}
		super.onCreate(savedInstanceState);
		setActionBar();
		setOverflowMenu();
	}

	@Override
	public void onResume(){
		Log.d("DB", "DB Test Open in onResume");
		if(!Repository.isOpen() && !dbOpenHere){
			Log.d("DB", "DB Open in onResume");
			Repository.Open();
			dbOpenHere = true;
		}
		Repository.registerOnRepositoryRefreshListener(this);
		ClaroClient.registerOnAccountStateChangedListener(this);
		super.onResume();
	}

	@Override
	public void onPause(){
		super.onPause();
		ClaroClient.unregisterOnAccountStateChangedListener(this);
		Repository.unregisterOnRepositoryRefreshListener(this);
		Log.d("DB", "DB Test Close in onPause");
		if(Repository.isOpen() && dbOpenHere){
			Log.d("DB", "DB Close in onPause");
			Repository.Close();
			dbOpenHere = false;
		}
	}

	@Override
	public void onDestroy(){
		Log.d("DB", "DB Test Close in onDestroy");
		if(Repository.isOpen() && dbOpenHere){
			Log.d("DB", "DB Close in onDestroy");
			Repository.Close();
			dbOpenHere = false;
		}
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong("lastUpdate", lastUpdate.getTimeInMillis());
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);

		if(ClaroClient.isValidAccount()){
			menu.findItem(R.id.menu_login).setVisible(false).setEnabled(false);
		} else {
			menu.findItem(R.id.menu_logout).setVisible(false).setEnabled(false);
			menu.findItem(R.id.menu_refresh).setVisible(false).setEnabled(false);
		}

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);     
		searchView.setSubmitButtonEnabled(true);

		this.menu = menu;

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
			ClaroClient.invalidateClient();
			Repository.Reset(this);
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
	}

	/**
	 * Sets up the {@link ActionBar}.
	 *
	 * @param displayHomeAsUp
	 *            displays the Up action
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setActionBar(final boolean displayHomeAsUp) {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUp);

		onAccountStateChange(ClaroClient.isValidAccount());
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

	@Override
	public void onAccountStateChange(boolean validity) {
		menuHandler.sendEmptyMessage(validity?1:0);
	}
	
	private Handler menuHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(menu != null){
				if(msg.what == 1){
					menu.findItem(R.id.menu_login).setVisible(false).setEnabled(false);
					menu.findItem(R.id.menu_logout).setVisible(true).setEnabled(true);
					menu.findItem(R.id.menu_refresh).setVisible(true).setEnabled(true);
				} else {
					menu.findItem(R.id.menu_login).setVisible(true).setEnabled(true);
					menu.findItem(R.id.menu_logout).setVisible(false).setEnabled(false);
					menu.findItem(R.id.menu_refresh).setVisible(false).setEnabled(false);
				}
			}
			return true;
		}
	});

	private GregorianCalendar lastUpdate;

	public boolean mustUpdate(int delay){
		GregorianCalendar temp = new GregorianCalendar();
		temp.add(GregorianCalendar.HOUR_OF_DAY, -delay);
		return lastUpdate.before(temp);
	}

	public void updatesNow(){
		lastUpdate = new GregorianCalendar();
	}

}
