package app;

import java.lang.reflect.Field;

import mobile.claroline.R;
import activity.Settings;
import activity.about_us;
import activity.home;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;
import dataStorage.IRepository.RepositoryRefreshListener;
import dataStorage.Repository;

public abstract class AppActivity extends Activity implements RepositoryRefreshListener { 


	public static Handler handler = new Handler(){
		public void handleMessage(Message mess){
			switch (mess.what) {
			case 0:
				GlobalApplication.setProgressIndicator(false);
				if(mess.arg1 == 1){
					Toast.makeText(GlobalApplication.getInstance().getApplicationContext(), (String) mess.obj, Toast.LENGTH_SHORT).show();
				}
				break;
			case 1: //Set the progress with downloading informations
				GlobalApplication.setProgressIndicator(null, true, (String) mess.obj, false, mess.arg1/mess.arg2, "%1d/%2d Ko");
				break;
			case 2: //Renew the progress status
				GlobalApplication.incrementProgression(mess.arg1/mess.arg2);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Repository.Open();

		super.onCreate(savedInstanceState);
		setActionBar();
		setOverflowMenu();
	}	

//	@Override
//	public void onRestart(){
//		Repository.Open();
//		super.onRestart();
//	}

	@Override
	public void onResume(){
		if(!Repository.isOpen()){
			Repository.Open();
		}
		Repository.addOnRepositoryRefreshListener(this);
		super.onResume();
	}

	@Override
	public void onPause(){
		Repository.remOnRepositoryRefreshListener(this);
		super.onPause();
	}

	@Override
	public void onDestroy(){
		if(Repository.isOpen()){
			Repository.Close();
		}
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);

		// Get the SearchView and set the searchable configuration
		/*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);     
		searchView.setSubmitButtonEnabled(true);
*/
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
		case android.R.id.home:
			// Comportement du bouton qui permet de retourner a l'activite precedente
			monIntent = new Intent(this,home.class);
			startActivity(monIntent);
			return true;
        case R.id.menu_refresh:
            // Comportement du bouton "Rafraichir"
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
