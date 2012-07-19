package activity;


import mobile.claroline.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class searchableActivity extends Activity implements OnClickListener
{
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.search);
	    
	 
	    
	 // permet de retourner sur la vue précédente
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      doMySearch(query);
	    }
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
        case android.R.id.home:
        	// Comportement du bouton qui permet de retourner a l'activite precedente
        	monIntent = new Intent(this,home.class);
        	startActivity(monIntent);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public String doMySearch(String query)
	{
		return null;
	}

}
