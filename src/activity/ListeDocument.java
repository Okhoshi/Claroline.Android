package activity;

import java.lang.reflect.Field;
import java.util.List;

import dataStorage.CoursRepository;

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
	Cours currentCours;
	ListView list;	
	List<Documents> liste_documents;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_documents);
		list = (ListView) findViewById(R.id.ListViewDocuments);

		int layoutID3 = R.layout.details_annonce;		//TODO change ici	

		Bundle extras = getIntent().getExtras();
		if (extras != null){
			int coursID = extras.getInt("coursID");
			currentCours=CoursRepository.GetById(coursID);	
			liste_documents = currentCours.getDocuments();
			DocumentsAdapter adapter = new DocumentsAdapter(this, layoutID3, liste_documents);
			list.setAdapter(adapter);
		}

	}
	
	public void onListItemClick(ListView l, View v, int position, long id) {
		Documents item = (Documents) list.getAdapter().getItem(position);
		Intent intent = new Intent(this, activity.detailsAnnonce.class); //TODO changer ici 
		intent.putExtra("docID", item.getId());
		startActivity(intent);
	}
}