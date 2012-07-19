package activity;

import java.util.ArrayList;
import java.util.List;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;
import dataStorage.CoursRepository;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;



public class home extends Activity implements OnItemClickListener, OnClickListener,OnTouchListener{
	
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.main);     
        setActionBar();
        setGridView();
            
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
            return true;
        case R.id.menu_search:
            // Comportement du bouton "Recherche"
        	onSearchRequested();
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
		// TODO Auto-generated method stub
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
		GridView gridview = (GridView) findViewById(R.id.gridview);
		// Normalement ca ! -->
		//List<Cours> Liste = CoursRepository.GetAllCours();
		// Test avec ca -->

		
		List<Cours> Liste= new ArrayList<Cours>();
		Cours Cours1= new Cours(null, null, null, null,"ives.smeers@uclouvain.be" , null, "Economie d'entreprise", "Ives Smeers");
		Cours Cours2= new Cours(null, null, null, null, "peter.vanroy@uclouvain.be", null, "Informatique : Oz", "Peter Van Roy");
		Cours Cours3= new Cours(null, null, null, null, "francois.remacle@uclouvain.be", null, "Mathématique Q3", "Francois Remacle");
		Liste.add(Cours1);
		Liste.add(Cours2);
		Liste.add(Cours3);
        gridview.setAdapter(new CoursAdapter(this,layoutID,Liste));
        

        gridview.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Toast.makeText(home.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        
        
	}
    
}