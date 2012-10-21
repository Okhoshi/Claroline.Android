package activity;


import mobile.claroline.R;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import dataStorage.CoursRepository;
import fragments.coursListFragment;


public class home extends AppActivity
{

	/**
	 * Used to know which Tab is selected
	 */
	public static String currentTag;
	static TextView view;


	/** 
	 * Called when the activity is first created. 
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);

		if(GlobalApplication.getPreferences().getString("user_login", "").isEmpty()){
			Intent settings_intent = new Intent(this, Settings.class);
			startActivity(settings_intent);
		} else {
			if(GlobalApplication.getPreferences().getString("firstName", "").isEmpty()){
				(new Thread(GlobalApplication.getClient(null, AllowedOperations.getUserData))).start();
			}
			GlobalApplication.setProgressIndicator(this, true);
			if(CoursRepository.GetAll().size() == 0){
				(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getCourseList))).start();
			} else {
				(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getUpdates))).start();
			}
		}
	}

	/*
	 * 
	 *   Menus,tabs,actionBar
	 * 
	 * 
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			GlobalApplication.setProgressIndicator(this, true);
			if(CoursRepository.GetAll().size() == 0){
				(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getCourseList))).start();
			} else {
				(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getUpdates))).start();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onRepositoryRefresh(String type) {
		if(type == CoursRepository.REPO_TYPE){
			coursListFragment list = (coursListFragment) getFragmentManager().findFragmentById(R.id.list_frag);
			if(list != null)
				list.refreshList.sendEmptyMessage(0);
		}
	}
}

