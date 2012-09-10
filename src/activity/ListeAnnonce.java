package activity;

import java.lang.reflect.Field;
import java.util.List;

import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;


import mobile.claroline.R;
import model.Annonce;
import model.AnnonceAdapter;
import model.Cours;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class ListeAnnonce extends Activity 
{
	Cours currentCours;
	ListView list = (ListView) findViewById(R.id.ListViewAnnonce);	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_annonce);
		
			
		int layoutID2 = R.layout.details_annonce;			
		List<Annonce> Liste = currentCours.getAnnonces();
		AnnonceAdapter adapter = new AnnonceAdapter(this, layoutID2, Liste);
		list.setAdapter(adapter);
		
		setActionBar();
		setOverflowMenu();
		
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
        	Intent settings_intent = new Intent(this, Settings.class);
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
	
	
	// Met les propriétés de l'action bar
		public void setActionBar()
		{
			ActionBar actionBar = getActionBar();
	        actionBar.setDisplayHomeAsUpEnabled(true); 
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
		
		
		public void onListItemClick(ListView l, View v, int position, long id) {
			Annonce item = (Annonce) list.getAdapter().getItem(position);
			home.currentAnnonce=item;
			Intent intent = new Intent(this, activity.detailsAnnonce.class);
			intent.putExtra("annID", item.getId());
			startActivity(intent);
		}
}
