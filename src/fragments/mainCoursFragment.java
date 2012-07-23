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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class mainCoursFragment extends ListFragment 
{
	
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
				R.layout.cours_view, Liste);
		setListAdapter(adapter);
	}

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
}
