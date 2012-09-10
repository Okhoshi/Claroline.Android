package activity;

import java.lang.reflect.Field;
import java.util.List;

import fragments.detailsAnnonceCoursFragment;
import fragments.detailsDocumentsCoursFragment;


import mobile.claroline.R;
import model.Annonce;
import model.AnnonceAdapter;
import model.Cours;
import model.Documents;
import model.DocumentsAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class ListeDocument extends Activity 
{
	Cours currentCours = home.currentCours;
	ListView list = (ListView) findViewById(R.id.ListViewDocuments);	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_documents);
		
			
		int layoutID3 = R.layout.details_annonce;		//TODO change ici	
		List<Documents> Liste = currentCours.getDocuments();
		DocumentsAdapter adapter = new DocumentsAdapter(this, layoutID3, Liste);
		list.setAdapter(adapter);

	}
	
	
	
		
		
		public void onListItemClick(ListView l, View v, int position, long id) {
			Documents item = (Documents) list.getAdapter().getItem(position);
			home.currentDocument=item;
			Intent intent = new Intent(this, activity.detailsAnnonce.class); //TODO changer ici
			startActivity(intent);
		}
}