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
import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;
import fragments.mainCoursFragment;


public class home extends AppActivity
{
	/**
	 * 
	 *  
	 *  Used to know which course is the current course
	 *  
	 *   
	 */

	public static Cours currentCours;
	public static Annonce currentAnnonce;
	public static Documents currentDocument;
	public static String currentTag;
	public static String annonce_id = "annonce_id";
	public static String documents_id = "documents_id";
	static TextView view ;
	
	public static Handler handler = new Handler(){
		public void handleMessage(Message mess){
			switch (mess.what) {
			case 0:
				GlobalApplication.setProgressIndicator(false);
				break;
			default:
				break;
			}
		}
	};


	/** 
	 * 
	 * Called when the activity is first created. 
	 * 
	 * 
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setActionBarTabs();
		}
		view = (TextView) findViewById(R.id.grid_item_label);
	}


	/**
	 * 
	 *   Listeners
	 * 
	 * 
	 */


	public static class MyTabListener<T extends Fragment> implements TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;


		/** * Constructor used each time a new tab is created. * * @param activity * The host Activity, used to instantiate the fragment * @param tag * The identifier tag for the fragment * @param clz * The fragment's Class, used to instantiate the fragment */

		public MyTabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());


				if(mTag.equals(annonce_id))
					currentTag=annonce_id;
				if(mTag.equals(documents_id))
					currentTag=documents_id;


				if(currentCours!=null)
				{
					String title = currentCours.getTitle();
					String titular = currentCours.getTitular();

					Log.v("MO", home.currentTag);

					if (home.currentTag.equals(home.annonce_id)) 
					{
						view.setText(title);
					} 
					if (home.currentTag.equals(home.documents_id))
					{
						view.setText(titular);
					} 
				}

				ft.add(android.R.id.content, mFragment, mTag);					



			} else {
				// If it exists, simply attach it in order to show it
				ft.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);


				if(mTag.equals(annonce_id))
					currentTag=annonce_id;
				if(mTag.equals(documents_id))
					currentTag=documents_id;

				//if(currentCours==null)
				//	Log.v("BO", "Cours nulllll ici");

				if(currentCours!=null)
				{
					String title = currentCours.getTitle();
					String titular = currentCours.getTitular();

					Log.v("MO", home.currentTag);

					if (home.currentTag.equals(home.annonce_id)) 
					{
						view.setText(title);
					} 
					if (home.currentTag.equals(home.documents_id))
					{
						view.setText(titular);
					} 
				}

				ft.attach(mFragment);



			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				ft.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);

				if(mTag.equals(annonce_id))
					currentTag=documents_id;
				if(mTag.equals(documents_id))	
					currentTag=annonce_id;
				ft.detach(mFragment);


			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}


	/**
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

	public void setActionBarTabs()
	{
		// setup action bar for tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		if (getResources().getConfiguration().orientation == 
				Configuration.ORIENTATION_LANDSCAPE)
		{
			actionBar.setDisplayShowTitleEnabled(false);
		}
		else
		{
			actionBar.setDisplayShowTitleEnabled(true);
		}

		Tab tab = actionBar
				.newTab()
				.setText(getString(R.string.onglet_annonces))
				.setTabListener(new MyTabListener<detailsAnnonceCoursFragment>(this, annonce_id,
						detailsAnnonceCoursFragment.class));
		actionBar.addTab(tab);


		tab = actionBar
				.newTab()
				.setText(getString(R.string.onglet_documents))
				.setTabListener(new MyTabListener<detailsDocumentsCoursFragment>(this, documents_id,
						detailsDocumentsCoursFragment.class));
		actionBar.addTab(tab);
	}

	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		Cours item = (Cours) l.getAdapter().getItem(position);
		currentCours=item;
	}

	public void onRepositoryRefresh(String type) {
		if(type == "Cours"){
			mainCoursFragment list = (mainCoursFragment) getFragmentManager().findFragmentById(R.id.list_frag);
			if(list != null)
				list.refreshList.sendEmptyMessage(0);
		}
	}
}
	
