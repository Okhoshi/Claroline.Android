package activity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;

import mobile.claroline.R;
import model.Cours;
import model.CoursAdapter;
import fragments.detailsCoursFragment;
import fragments.mainCoursFragment.OnItemSelectedListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
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


public class home extends Activity implements OnItemSelectedListener,  OnItemClickListener
{

	private static final int MAIL_ID = R.id.itemMails;
	public static Cours currentCours;
	ListView listview = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.main);     
     //   setActionBar();
     //   setOverflowMenu();
     //   setListView();
     //   registerForContextMenu(getListView());
        
            
        Log.e("MO", AllowedOperations.authenticate.name());
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
    
    public void onClick(View v) 
    {	
    		 
    }
    

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// Pas a faire puisque déjà faite dans la creation des Listener
	}
	
	
	// Met les propriétés de l'action bar
	public void setActionBar()
	{
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false); 
	}
	
	// Place la GridView
	public void setListView()
	{
		int layoutID = R.layout.cours_view;
		listview = (ListView) findViewById(R.id.list_frag);
		// Normalement ca ! -->
		//List<Cours> Liste = CoursRepository.GetAllCours();
		// Test avec ca -->

		
		final List<Cours> Liste= new ArrayList<Cours>();
		Cours Cours1= new Cours(null, null, null, null,"ives.smeers@uclouvain.be" , null, "Economie d'entreprise", "Ives Smeers");
		Cours Cours2= new Cours(null, null, null, null, "peter.vanroy@uclouvain.be", null, "Informatique : Oz", "Peter Van Roy");
		Cours Cours3= new Cours(null, null, null, null, "francois.remacle@uclouvain.be", null, "Mathématique Q3", "Francois Remacle");
		Liste.add(Cours1);
		Liste.add(Cours2);
		Liste.add(Cours3);
        listview.setAdapter(new CoursAdapter(this,layoutID,Liste));
        

        
        listview.setOnItemLongClickListener(new OnItemLongClickListener() 
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {
            	int n = Liste.size();
            	for(int i = 0;i<n;++i)
            	{
            		currentCours=(Cours) listview.getItemAtPosition(i);
            		if(Liste.get(position).equals(currentCours))
            		{		
            			openContextMenu(listview);
            		}
            		
            		
            	}
				return true;
                
            }
        });
           
        
        
	}
	
	
	
	public View getListView()
	{
		return this.listview;
	}
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.setHeaderTitle(getString(R.string.contextual_header));
			menu.add(0, MAIL_ID, 0, getString(R.string.contextual_mail));
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) 
		{
			case MAIL_ID:
				//String officialEmail = currentCours.getOfficialEmail();
				// Pour test
				String officialEmail = "eldala07@hotmail.com";
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_EMAIL, new String[] {officialEmail});
				i.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_titulars));
				i.setType("message/rfc822");
				startActivity(Intent.createChooser(i, getString(R.string.choose_activity_mail)));
			return true;
			default:
			return super.onContextItemSelected(item);
		}
		
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
	
	
	
	
	/** This is a callback that the list fragment (Fragment A)
    calls when a list item is selected */
public void onItemSelected(int position) 
{
    detailsCoursFragment displayFrag = (detailsCoursFragment) getFragmentManager()
                                .findFragmentById(R.id.details_frag);
    if (displayFrag == null) {
        // DisplayFragment (Fragment B) is not in the layout (handset layout),
        // so start DisplayActivity (Activity B)
        // and pass it the info about the selected item
        Intent intent = new Intent(this, coursActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    } else 
    {
        // DisplayFragment (Fragment B) is in the layout (tablet layout),
        // so tell the fragment to update
         displayFrag.updateContent(position);
    }
}





}
