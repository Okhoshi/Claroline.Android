package fragments;

import java.util.List;

import mobile.claroline.R;
import model.Annonce;
import model.AnnonceAdapter;
import model.Cours;
import model.CoursAdapter;
import model.Documents;
import model.DocumentsAdapter;
import activity.home;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import dataStorage.CoursRepository;

public class mainCoursFragment extends ListFragment 
{
	
	private static final int MAIL_ID = R.id.itemMails;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int layoutID = R.layout.cours_view;
		
		// Normalement ca ! -->
		List<Cours> Liste = CoursRepository.GetAllCours();
		// Test avec ca 

		/*
		final List<Cours> Liste= new ArrayList<Cours>();
		Cours Cours1= new Cours(null, null, null, null,"ives.smeers@uclouvain.be" , null, "Economie d'entreprise", "Ives Smeers");
		Cours Cours2= new Cours(null, null, null, null, "peter.vanroy@uclouvain.be", null, "Informatique : Oz", "Peter Van Roy");
		Cours Cours3= new Cours(null, null, null, null, "francois.remacle@uclouvain.be", null, "Mathématique Q3", "Francois Remacle");
		Liste.add(Cours1);
		Liste.add(Cours2);
		Liste.add(Cours3);*/
		CoursAdapter adapter = new CoursAdapter(getActivity().getApplicationContext(),
				layoutID, Liste);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			int layoutID = R.layout.cours_view;
			List<Cours> Liste = CoursRepository.GetAllCours();
			CoursAdapter adapter = new CoursAdapter(getActivity().getApplicationContext(),
					layoutID, Liste);
			setListAdapter(adapter);
		}
	};

	
	/**
	 * 
	 *   OnListItemClick
	 * 
	 * 
	 */
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cours item = (Cours) getListAdapter().getItem(position);
		home.currentCours=item;
		String title = item.getTitle();
		String titular = item.getTitular();
		
		detailsAnnonceCoursFragment fragmentAnnonce = (detailsAnnonceCoursFragment) getFragmentManager().findFragmentById(R.id.details_frag);
		detailsDocumentsCoursFragment fragmentDocs = (detailsDocumentsCoursFragment) getFragmentManager().findFragmentById(R.id.details_frag_docs);
		
		//Log.v("MO", home.currentTag);
		
		if (fragmentAnnonce != null && fragmentAnnonce.isInLayout() && home.currentTag.equals(home.annonce_id)) 
		{
			//fragmentAnnonce.setText(title);
			int layoutID2 = R.layout.details_annonce;			
			// Normalement ca ! -->
			List<Annonce> Liste = item.getAnnonces();
			
			AnnonceAdapter adapter = new AnnonceAdapter(getActivity().getApplicationContext(),
					layoutID2, Liste);
			setListAdapter(adapter);
			registerForContextMenu(getListView());
		} 
		if (fragmentDocs != null && fragmentDocs.isInLayout() && home.currentTag.equals(home.documents_id))
		{
			//fragmentAnnonce.setText(titular);
			int layoutID4 = R.layout.details_annonce;		//TODO changer ici	
			List<Documents> Liste = item.getDocuments();
			
			DocumentsAdapter adapter = new DocumentsAdapter(getActivity().getApplicationContext(),
					layoutID4, Liste);
			setListAdapter(adapter);
			registerForContextMenu(getListView());
		} 
		else 
		{
			Intent intent = new Intent(getActivity().getApplicationContext(), activity.coursActivity.class);
			intent.putExtra("coursID", item.getId());
			startActivity(intent);
		}

	}
	
	
	/**
	 * 
	 *   All about the contextual menu
	 * 
	 * 
	 */
	
	
	
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
			//String officialEmail = currentCours.getOfficialEmail();
			// Pour test
			String officialEmail = "eldala07@hotmail.com";
			Intent i = new Intent(Intent.ACTION_SEND);
			i.putExtra(Intent.EXTRA_EMAIL, new String[] {officialEmail});
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_titulars));
			i.setType("message/rfc822");
			startActivity(Intent.createChooser(i, getString(R.string.choose_activity_mail)));
		return true;
		default:
		return super.onContextItemSelected(item);
	}
	
}
}
