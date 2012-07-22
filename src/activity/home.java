package activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;
import dataStorage.CoursRepository;
import dataStorage.Repository;
import mobile.claroline.R;
import model.Cours;
import model.CoursAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;



public class home extends Activity implements OnItemClickListener, OnClickListener,OnTouchListener{
	
	
	private static final int MAIL_ID = R.string.contextual_mail;
	public static Cours currentCours;
	GridView gridview = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.main);     
        setActionBar();
        setOverflowMenu();
        setGridView();
        registerForContextMenu(getGridView());
        
            
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
	public void setGridView()
	{
		int layoutID = R.layout.cours_view;
		gridview = (GridView) findViewById(R.id.gridview);
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
        gridview.setAdapter(new CoursAdapter(this,layoutID,Liste));
        

        
        gridview.setOnItemLongClickListener(new OnItemLongClickListener() 
        {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id)
            {
            	int n = Liste.size();
            	for(int i = 0;i<n;++i)
            	{
            		currentCours=(Cours) gridview.getItemAtPosition(i);
            		if(Liste.get(position).equals(currentCours))
            		{		
            			openContextMenu(gridview);
            		}
            		
            		
            	}
				return true;
                
            }
        });
        
        
        gridview.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
            	int n = Liste.size();
            	for(int i = 0;i<n;++i)
            	{
            		currentCours=(Cours) gridview.getItemAtPosition(i);
            		if(Liste.get(position).equals(currentCours))
            		{		
            			
            			Intent coursIntent = new Intent(getApplicationContext(), coursActivity.class);
                    	startActivity(coursIntent);
            		}
            		
            		
            	}
                
            }
        });
        
        
        
        
	}
	
	
	
	public View getGridView()
	{
		return this.gridview;
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
				//TODO send a mail
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
	
	
}
