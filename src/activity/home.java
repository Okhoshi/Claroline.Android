package activity;


import mobile.claroline.R;
import model.Annonce;
import model.Cours;
import model.Documents;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import dataStorage.CoursRepository;
import fragments.coursListFragment;
import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;


public class home extends AppActivity
{
	/**
	 *  Used to know which course is the current course
	 */
	public static final String ANNONCE_ID = "annonce_id";
	public static final String DOCUMENTS_ID = "documents_id";

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
			(new Thread(GlobalApplication.getClient().makeOperation(handler, AllowedOperations.getCourseList))).start();
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

