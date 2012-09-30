package fragments;

import java.util.List;

import model.Annonce;
import model.Cours;
import adapter.AnnonceAdapter;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;

public class annonceListFragment extends ListFragment {
	
	private Cours currentCours;

	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			List<Annonce> liste = AnnonceRepository.GetAllAnnoncesByCoursId(currentCours.getId());
			AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
			setListAdapter(adapter);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return super.onCreateView(inflater, container, savedInstanceState);
		//return inflater.inflate(R.layout.standard_list, container);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle extras = getArguments();
	    if (extras != null)
	    {
	        currentCours=CoursRepository.GetById(extras.getInt("coursID"));			
	    }

		List<Annonce> liste = AnnonceRepository.GetAllAnnoncesByCoursId(currentCours.getId());
		AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Annonce item = (Annonce) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity(), activity.detailsAnnonce.class);
		intent.putExtra("annID", item.getId());
		startActivity(intent);
	}
}
