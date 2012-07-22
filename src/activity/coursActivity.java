package activity;

import mobile.claroline.R;
import model.Cours;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressLint("ParserError")
public class coursActivity extends Activity 
{
	
	// Permit to update the data of a certain course
	private Cours currentCours = home.currentCours;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.cours_activity);
	    setActionBar();
	    setTabs();
	    
        
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
        	Intent monIntent1 = new Intent(this,searchableActivity.class);
        	startActivity(monIntent1);
        	//onSearchRequested();
            return true;
        case R.id.menu_settings:
            // Comportement du bouton "Paramètres"
            return true;
        case android.R.id.home:
        	// Comportement du bouton qui permet de retourner a l'activite precedente
        	monIntent = new Intent(this,home.class);
        	startActivity(monIntent);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	
    
	
	public void setTabs()
	{
	
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec(getString(R.string.onglet_annonces));
		spec1.setContent(R.id.tab1);
		spec1.setIndicator(getString(R.string.onglet_annonces));


		TabSpec spec2=tabHost.newTabSpec(getString(R.string.onglet_documents));
		spec2.setIndicator(getString(R.string.onglet_documents));
		spec2.setContent(R.id.tab2);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		
	}
	

	// Met les propriétés de l'action bar
	public void setActionBar()
	{
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); 
	}
		
}
