package activity;

import java.util.List;

import mobile.claroline.R;
import model.Annonce;
import model.AnnonceAdapter;
import model.Cours;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ListeAnnonce extends Activity 
{
	Cours currentCours = home.currentCours;
	ListView list;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_annonce);
		
		list = (ListView) findViewById(R.id.ListViewAnnonce);
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
