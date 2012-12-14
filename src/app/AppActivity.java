package app;

import java.lang.reflect.Field;

import connectivity.ClaroClient;

import mobile.claroline.R;
import activity.Settings;
import activity.about_us;
import activity.HomeActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.SearchView;
import dataStorage.IRepository.RepositoryRefreshListener;
import dataStorage.Repository;

public abstract class AppActivity extends Activity implements RepositoryRefreshListener { 
	
	private boolean dbOpenHere = false;

	public Handler handler = new AppHandler(this);

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Log.d("DB", "DB Open in onCreate");
		Repository.Open();
		dbOpenHere = true;

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
		Repository.addOnRepositoryRefreshListener(this);
		super.onResume();
	}

	@Override
	public void onPause(){
		super.onPause();
		Repository.remOnRepositoryRefreshListener(this);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);

		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);     
		searchView.setSubmitButtonEnabled(true);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			// Comportement du bouton "A Propos"
			Intent monIntent = new Intent(this,about_us.class);
			startActivity(monIntent);
			return true;
		case R.id.menu_help:
			// Comportement du bouton "Aide"
			return true;
		case R.id.menu_search:
			onSearchRequested();
			return true;
		case R.id.menu_settings:
			Intent settings_intent = new Intent(this, Settings.class);
			startActivity(settings_intent);
			return true;
		case R.id.menu_logout:
			ClaroClient.invalidateClient();
			Repository.Reset(this);
			return true;
		case android.R.id.home:
			// Comportement du bouton qui permet de retourner a l'activite d'accueil
			monIntent = new Intent(this,HomeActivity.class);
			monIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
								Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(monIntent);
			return true;
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			// Doit être implémenter dans chaque activité
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Met les propriétés de l'action bar
	public void setActionBar()
	{
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true); 
	}

	public void setOverflowMenu()
	{
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
			// Ignore
		}

	}
}
