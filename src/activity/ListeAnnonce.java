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
		
	}
	
		
		public void onListItemClick(ListView l, View v, int position, long id) {
			Annonce item = (Annonce) list.getAdapter().getItem(position);
			home.currentAnnonce=item;
			Intent intent = new Intent(this, activity.detailsAnnonce.class);
			intent.putExtra("annID", item.getId());
			startActivity(intent);
		}
}
