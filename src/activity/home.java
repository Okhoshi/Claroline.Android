package activity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;

import mobile.claroline.R;
import model.Cours;
import model.CoursAdapter;
import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;
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
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;


public class home extends Activity
{

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
		

	}

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
						ft.add(android.R.id.content, mFragment, mTag);
					} else {
						// If it exists, simply attach it in order to show it
						ft.setCustomAnimations(android.R.animator.fade_in,
								android.R.animator.fade_out);
						ft.attach(mFragment);
					}
				}

				public void onTabUnselected(Tab tab, FragmentTransaction ft) {
					if (mFragment != null) {
						ft.setCustomAnimations(android.R.animator.fade_in,
								android.R.animator.fade_out);
						ft.detach(mFragment);
					}
				}

				public void onTabReselected(Tab tab, FragmentTransaction ft) {
				}

				

				
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
		        case R.id.menu_settings:
		            // Comportement du bouton "Paramètres"
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
						.setTabListener(new MyTabListener<detailsAnnonceCoursFragment>(this, getString(R.string.onglet_annonces),
										detailsAnnonceCoursFragment.class));
				actionBar.addTab(tab);

				tab = actionBar
						.newTab()
						.setText(getString(R.string.onglet_documents))
						.setTabListener(new MyTabListener<detailsAnnonceCoursFragment>(this, getString(R.string.onglet_documents),
								detailsAnnonceCoursFragment.class));
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

}
