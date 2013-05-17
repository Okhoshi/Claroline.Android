package activity;


import model.Cours;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import app.AppActivity;
import app.GlobalApplication;
import app.TabListener;
import connectivity.AllowedOperations;
import connectivity.ClaroClient;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
import dataStorage.Repository;
import fragments.LoginDialog;
import fragments.annonceListFragment;
import fragments.coursListFragment;
import fragments.documentsListFragment;
import net.claroline.mobile.android.R;


public class HomeActivity extends AppActivity
{

	/**
	 * Used to know which Tab is selected
	 */
	public static String currentTag;
	static TextView view;

	private boolean xLargeEnabled = false;
	private Cours currentCours = null;

	protected Handler updatePanes = null;
	private boolean tabsSetted;

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

		//Tablet Layout Detection
		if(findViewById(R.id.tab_content) != null){
			xLargeEnabled = true;
			Log.d("LargeLayout", "LargeLayout detected!");

			//TODO Add newsfeed tab here

			Bundle extras = getIntent().getExtras();
			int id = -1;
			int item = -1;
			if (extras != null)
			{
				currentCours=CoursRepository.GetById(extras.getInt("coursID"));
				id  = extras.getInt("id", -1);
				item  = extras.getInt("tab", -1);
			}

			if(currentCours != null){
				setTabs(id);
			}

			if(item > -1){
				getActionBar().setSelectedNavigationItem(item);
			}

			if (savedInstanceState != null && savedInstanceState.containsKey("coursID")){
				currentCours=CoursRepository.GetById(savedInstanceState.getInt("coursID"));
				setTabs(id);
				getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
			}

			updatePanes = new Handler(new Handler.Callback() {

				public boolean handleMessage(Message msg) {
					currentCours=CoursRepository.GetById(msg.arg1);
					if(!tabsSetted){
						setTabs(-1);
					} else {
						updateTabs();
					}
					return true;
				}
			});
		}

		coursListFragment list = (coursListFragment) getFragmentManager().findFragmentById(R.id.list_frag);
		if(list != null){
			list.setEnv(xLargeEnabled, updatePanes);
		}

		//Refresh content
		if(GlobalApplication.getPreferences().getString(Settings.PLATFORM_HOST, "").equals("")){
			Intent i = new Intent(this, Settings.class);
			startActivity(i);
		} else if(GlobalApplication.getPreferences().getString(Settings.USER_LOGIN, "").equals("")){
			showLoginDialog();
		} else {
			refresh(false, false, true);
		}
	}

	/* 
	 *   Menus,tabs,actionBar
	 */

	protected void updateTabs() {
		if(tabsSetted && currentCours != null){
			annonceListFragment list = (annonceListFragment) getFragmentManager().findFragmentByTag("announce");
			if(list != null){
				Message msg = new Message();
				msg.what = annonceListFragment.UPDATECOURS;
				msg.arg1 = currentCours.getId();
				list.refreshList.sendMessage(msg);
			}

			documentsListFragment list2 = (documentsListFragment) getFragmentManager().findFragmentByTag("documents");
			if(list2 != null){
				Message msg = new Message();
				msg.what = documentsListFragment.UPDATECOURS;
				msg.arg1 = currentCours.getId();
				list2.refreshList.sendMessage(msg);
			}
		}
	}

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
				} else if(xLargeEnabled && currentCours != null && currentCours.isExpired()){
					(new Thread(GlobalApplication.getClient(handler, AllowedOperations.updateCompleteCourse, currentCours))).start();
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
	public void onBackPressed(){
		documentsListFragment frag = (documentsListFragment) getFragmentManager().findFragmentByTag("documents");
		if(frag != null && !frag.isOnRoot()){
			frag.refreshList.sendEmptyMessage(documentsListFragment.GOUP);
		} else {
			super.onBackPressed();
		}
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

	/**
	 * CALL ONLY IF IN XLARGE LAYOUT
	 * @param id Root id for the Documents tab
	 */
	public void setTabs(int id){    	
		tabsSetted = true;

		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		if (getResources().getConfiguration().orientation != 
				Configuration.ORIENTATION_LANDSCAPE)
		{
			bar.setDisplayShowTitleEnabled(false);
		}
		else
		{
			bar.setDisplayShowTitleEnabled(true);
		}

		Bundle args = new Bundle();
		args.putInt("coursID", currentCours.getId());

		bar.addTab(bar.newTab().setText(getString(R.string.onglet_annonces)).setTag("annTab")
				.setTabListener(new TabListener<annonceListFragment>(this, "announce", annonceListFragment.class, R.id.tab_content, args)));

		args.putInt("docID", id);
		bar.addTab(bar.newTab().setText(getString(R.string.onglet_documents)).setTag("docTab")
				.setTabListener(new TabListener<documentsListFragment>(this, "documents", documentsListFragment.class, R.id.tab_content, args)));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(xLargeEnabled){
			outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
			if(currentCours != null) {
				outState.putInt("coursID", currentCours.getId());
			}
		}
	}

	public void onRepositoryRefresh(String type) {
		if(type.equals(CoursRepository.REPO_TYPE) || type.equals(Repository.ALL)){
			coursListFragment list = (coursListFragment) getFragmentManager().findFragmentById(R.id.list_frag);
			if(list != null)
				list.refreshList.sendEmptyMessage(0);
		}
		if(xLargeEnabled){
			if(type.equals(AnnonceRepository.REPO_TYPE) || type.equals(Repository.ALL)){
				annonceListFragment list = (annonceListFragment) getFragmentManager().findFragmentByTag("announce");
				if(list != null)
					list.refreshList.sendEmptyMessage(0);
			}
			if(type.equals(DocumentsRepository.REPO_TYPE) || type.equals(Repository.ALL)){
				documentsListFragment list = (documentsListFragment) getFragmentManager().findFragmentByTag("documents");
				if(list != null)
					list.refreshList.sendEmptyMessage(0);
			}
		}
	}
}

