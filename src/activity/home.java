package activity;


import java.io.IOException;
import java.lang.reflect.Field;

import connectivity.AllowedOperations;
import dataStorage.IRepository.RepositoryRefreshListener;
import dataStorage.*;
import fragments.*;
import mobile.claroline.R;
import model.Cours;
import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import app.GlobalApplication;


public class home extends Activity
{
	/**
	 * 
	 *  
	 *  Used to know which course is the current course
	 *  
	 *   
	 */

	public static Cours currentCours;
	public static String currentTag;
	public static String annonce_id = "annonce_id";
	public static String documents_id = "documents_id";
	static TextView view ;
	
	protected ProgressDialog mProgressDialog;
	private Context mContext;
	enum ErrorStatus {
	    NO_ERROR, ERROR_1, ERROR_2
	};
	private ErrorStatus status;
	 
	public static final int MSG_ERR = 0;
	public static final int MSG_CNF = 1;
	public static final int MSG_IND = 2;
	 
	public static final String TAG = "ProgressBarActivity";
	
	
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
	
	private RepositoryRefreshListener listener = new RepositoryRefreshListener(){
		public void onRepositoryRefresh(String type) {
			if(type == "Cours"){
				mainCoursFragment list = (mainCoursFragment) getFragmentManager().findFragmentById(R.id.list_frag);
				if(list != null)
					list.refreshList.sendEmptyMessage(0);
			}
		}
	};

	/**
	 * 
	 *  
	 *  Used to choose an image
	 *  
	 *   
	 */

	//variable for selection intent
	private final int PICKER = 1;
	String imgPath;


	/** 
	 * 
	 * Called when the activity is first created. 
	 * 
	 * 
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		Repository.Open();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setActionBar();
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setActionBarTabs();
		setOverflowMenu();
		view = (TextView) findViewById(R.id.grid_item_label);
	}
	
	@Override
	public void onRestart(){
		Repository.Open();
		super.onRestart();
	}
	
	@Override
	public void onResume(){
		Repository.addOnRepositoryRefreshListener(listener);
		super.onResume();
	}
	
	@Override
	public void onPause(){
		Repository.remOnRepositoryRefreshListener(listener);
		super.onPause();
	}
	
	@Override
	public void onStop(){
		Repository.Close();
		super.onStop();
	}
	


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) 
		{
			//check if we are returning from picture selection
			if (requestCode == PICKER) 
			{
				//import the image
				//the returned picture URI
				Uri pickedUri = data.getData();
				//declare the bitmap
				Bitmap pic = null;    	 
				//declare the path string
				String imgPath = "";

				//retrieve the string using media data
				String[] medData = { MediaStore.Images.Media.DATA };
				//query the data
				Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
				if(picCursor!=null)
				{
					//get the path string
					int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					picCursor.moveToFirst();
					imgPath = picCursor.getString(index);
				}
				else
					imgPath = pickedUri.getPath();
			}
		}
		//superclass method
		super.onActivityResult(requestCode, resultCode, data);
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

				if(currentCours==null)
					Log.v("BO", "Cours nulllll ici");

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
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			GlobalApplication.setProgressIndicator(this, true);
			(new Thread(GlobalApplication.getClient().makeOperation(handler, AllowedOperations.getCourseList))).start();
			return true;
		case R.id.menu_search:
			// Comportement du bouton "Recherche"
			//Repository.Open();
			onSearchRequested();
			//Repository.Close();
			//Intent monIntent1 = new Intent(this,searchableActivity.class);
			//startActivity(monIntent1);
			return true;
		case R.id.menu_settings:
			Intent settings_intent = new Intent(this, Settings.class);
			startActivity(settings_intent);
			return true;
		case R.id.white:
			LinearLayout linLay = (LinearLayout) findViewById(R.id.frags);
			linLay.setBackgroundColor(Color.WHITE);
			// checker portrait ou land et mettre en fonction + pour toutes les activités
			return true;
		case R.id.yellow:
			LinearLayout linLay1 = (LinearLayout) findViewById(R.id.frags);
			linLay1.setBackgroundColor(Color.YELLOW);
			return true;
		case R.id.green:
			LinearLayout linLay2 = (LinearLayout) findViewById(R.id.frags);
			linLay2.setBackgroundColor(Color.GREEN);
			return true;
		case R.id.red:
			LinearLayout linLay3 = (LinearLayout) findViewById(R.id.frags);
			linLay3.setBackgroundColor(Color.RED);
			return true;
		case R.id.black:
			LinearLayout linLay4 = (LinearLayout) findViewById(R.id.frags);
			linLay4.setBackgroundColor(Color.BLACK);
			return true;
		case R.string.upload_a_skin:

			LinearLayout linLay5 = (LinearLayout) findViewById(R.id.frags);
			//take the user to their chosen image selection app (gallery or file manager)
			Intent pickIntent = new Intent();
			pickIntent.setType("image/*");
			pickIntent.setAction(Intent.ACTION_GET_CONTENT);

			//we will handle the returned data in onActivityResult
			startActivityForResult(Intent.createChooser(pickIntent, getString(R.string.select_a_picture)), PICKER);
			try {

				Bitmap bm = BitmapFactory.decodeStream(getResources().getAssets().open(imgPath + ".gif"));
				BitmapDrawable bmd= new BitmapDrawable(bm);
				linLay5.setBackgroundDrawable(bmd);

				// ajouter le stockage de cette image dans une base de donnée pour que celle-ci soit mise par défaut
				// lors du nouveau lancement de l'application
				// aussi, permettre de supprimer les images de la base de donnée

			} catch (IOException e) {
				e.printStackTrace();
			}


			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}




	// Met les propriétés de l'action bar
	public void setActionBar()
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false); 
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

	public void onListItemClick(ListView l, View v, int position, long id) 
	{
		Cours item = (Cours) l.getAdapter().getItem(position);
		currentCours=item;
	}
}
	
