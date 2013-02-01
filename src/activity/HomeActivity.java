package activity;


import mobile.claroline.R;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import connectivity.ClaroClient;
import dataStorage.CoursRepository;
import dataStorage.Repository;
import fragments.LoginDialog;
import fragments.coursListFragment;


public class HomeActivity extends AppActivity
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

		if(GlobalApplication.getPreferences().getString(Settings.USER_LOGIN, "").equals("")){
			showLoginDialog();
		} else {
			refresh(false, false, true);
		}
	}

	/*
	 * 
	 *   Menus,tabs,actionBar
	 * 
	 * 
	 */

	private void refresh(boolean force, boolean forceUser, boolean noDialog) {
		if(GlobalApplication.getPreferences().getString(Settings.PLATFORM_HOST, "").equals("")){
			Intent i = new Intent(this, Settings.class);
			startActivity(i);
		}
		if(!noDialog && !ClaroClient.isValidAccount()){
			showLoginDialog();
		} else {
			if(forceUser || mustUpdate(6)){
				(new Thread(GlobalApplication.getClient(null, AllowedOperations.getUserData))).start();
			}

			if(force || mustUpdate(1)){
				GlobalApplication.setProgressIndicator(this, true);
				if(CoursRepository.GetAll().size() == 0){
					(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getCourseList))).start();
				} else {
					(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getUpdates))).start();
				}
				updatesNow();
			}
		}
	}

	private void showLoginDialog() {
		LoginDialog login = new LoginDialog(this);
		login.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if(ClaroClient.isValidAccount()){
					HomeActivity.this.refresh(true, true, false);
				}	
			}
		});
		login.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	public void onRepositoryRefresh(String type) {
		if(type.equals(CoursRepository.REPO_TYPE) || type.equals(Repository.ALL)){
			coursListFragment list = (coursListFragment) getFragmentManager().findFragmentById(R.id.list_frag);
			if(list != null)
				list.refreshList.sendEmptyMessage(0);
		}
	}
}

