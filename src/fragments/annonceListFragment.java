package fragments;

import java.util.List;

import net.claroline.mobile.android.R;
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
import dataStorage.Repository;

public class annonceListFragment extends ListFragment {

	public static final int REFRESH = 0;
	public static final int UPDATECOURS = 1;

	private Cours currentCours;

	public Handler refreshList = new Handler(new Handler.Callback() {

		public boolean handleMessage(Message mess){
			switch(mess.what){
			case REFRESH:
				List<Annonce> liste = AnnonceRepository.GetAllAnnoncesByCoursId(currentCours.getId());
				AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
				setListAdapter(adapter);
				break;
			case UPDATECOURS:
				currentCours = CoursRepository.GetById(mess.arg1);
				mess.what = REFRESH;
				handleMessage(mess);
				break;
			}
			return true;
		}
	});

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.standard_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(!Repository.isOpen()){
			Repository.Open();
		}

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
