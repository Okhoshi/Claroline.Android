package activity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;

import mobile.claroline.R;
import mobile.claroline.R.drawable;
import mobile.claroline.R.string;
import model.Cours;
import model.CoursAdapter;
import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;
import fragments.mainCoursFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class home extends Activity
{
	public static Cours currentCours;
	public static String currentTag;
	public static String annonce_id = "annonce_id";
	public static String documents_id = "documents_id";
	static TextView view ;
	
	
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		setActionBar();
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
		setActionBarTabs();
		setOverflowMenu();
		view = (TextView) findViewById(R.id.grid_item_label);
		
		

	}
	
	/**
	 * 
	 *   Listeners
	 * 
	 * 
	 */
	

			public static class MyTabListener<T extends Fragment> implements
					TabListener {
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
		       
		        
		        
		        SubMenu settings = menu.addSubMenu(getString(R.string.menu_settings));
		        //SubMenu skins = settings.addSubMenu(getString(R.string.skin));
		        SubMenu background_skin = settings.addSubMenu(getString(R.string.basic_skin));
		        settings.add(getString(R.string.upload_a_skin));
		        SubMenu your_skins = settings.addSubMenu(getString(R.string.your_skins));

		        
		        background_skin.add(0,R.id.white,0,getString(R.string.white));
		        background_skin.add(0,R.id.yellow,1,getString(R.string.yellow));
		        background_skin.add(0,R.id.green,2,getString(R.string.green));
		        background_skin.add(0,R.id.red,3,getString(R.string.red));
		        background_skin.add(0,R.id.black,4,getString(R.string.black));

		        // Permettre un upload ou un choose de skin serait pas mal
		        
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
		        	ClaroClient cli = new ClaroClient();
		        	Thread thread = new Thread(cli.makeOperation(AllowedOperations.getCourseList));
		        	thread.start();
		            return true;
		        case R.id.menu_search:
		            // Comportement du bouton "Recherche"
		        	//Repository.Open();
		        	onSearchRequested();
		        	//Repository.Close();
		        	//Intent monIntent1 = new Intent(this,searchableActivity.class);
		        	//startActivity(monIntent1);
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
