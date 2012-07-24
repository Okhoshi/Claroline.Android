package fragments;

import java.util.ArrayList;
import java.util.List;

import mobile.claroline.R;
import model.Cours;
import model.CoursAdapter;
import activity.coursActivity;
import activity.home;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

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
		//List<Cours> Liste = CoursRepository.GetAllCours();
		// Test avec ca -->

		
		final List<Cours> Liste= new ArrayList<Cours>();
		Cours Cours1= new Cours(null, null, null, null,"ives.smeers@uclouvain.be" , null, "Economie d'entreprise", "Ives Smeers");
		Cours Cours2= new Cours(null, null, null, null, "peter.vanroy@uclouvain.be", null, "Informatique : Oz", "Peter Van Roy");
		Cours Cours3= new Cours(null, null, null, null, "francois.remacle@uclouvain.be", null, "Mathématique Q3", "Francois Remacle");
		Liste.add(Cours1);
		Liste.add(Cours2);
		Liste.add(Cours3);
		CoursAdapter adapter = new CoursAdapter(getActivity().getApplicationContext(),
				layoutID, Liste);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	
	/**
	 * 
	 *   OnListItemClick
	 * 
	 * 
	 */
	
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cours item = (Cours) getListAdapter().getItem(position);
		String titre = item.getTitle();
		
		detailsAnnonceCoursFragment fragment = (detailsAnnonceCoursFragment) getFragmentManager().findFragmentById(R.id.details_frag);
		if (fragment != null && fragment.isInLayout()) 
		{
			//fragment.setText(item);
		} 
		else 
		{
			Intent intent = new Intent(getActivity().getApplicationContext(), activity.coursActivity.class);
			intent.putExtra("value", titre);
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
