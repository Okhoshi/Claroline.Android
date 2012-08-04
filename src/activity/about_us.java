package activity;

import mobile.claroline.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.SearchView;

public class about_us extends Activity implements OnClickListener, OnTouchListener {

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        
        // permet de retourner sur la vue précédente
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        	Intent settings_intent = new Intent(this, Preference.class);
        	startActivity(settings_intent);
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
	
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
