package fragments;

import java.util.List;

import dataStorage.CoursRepository;
import mobile.claroline.R;
import model.Annonce;
import model.AnnonceAdapter;
import model.Cours;
import model.CoursAdapter;
import activity.home;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


public class detailsAnnonceCoursFragment extends ListFragment 
{
	
	Cours currentCours = home.currentCours;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/*public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			int layoutID = R.layout.details_annonce;
			List<Annonce> Liste = currentCours.getAnnonces();
			AnnonceAdapter adapter = new AnnonceAdapter(getActivity().getApplicationContext(),
					layoutID, Liste);
			setListAdapter(adapter);
		}
	};*/
	

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Annonce item = (Annonce) getListAdapter().getItem(position);
		home.currentAnnonce=item;
		
		detailsAnnonceCoursFragment fragmentAnnonce = (detailsAnnonceCoursFragment) getFragmentManager().findFragmentById(R.id.details_frag);
		fragmentAnnonce.setText(item.getTitle()+"\n \n"+item.getContent());
		
	

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}


	public void setText(String item) {
		TextView view = (TextView) getView().findViewById(R.id.grid_item_label);
		view.setText(item);
	}
	
	
}
