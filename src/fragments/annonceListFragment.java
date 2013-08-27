package fragments;

import java.util.List;

import model.Annonce;
import model.OldCours;
import net.claroline.mobile.android.R;
import adapter.AnnonceAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.Repository;

public class annonceListFragment extends ListFragment {

	private OldCours currentCours;

	public Handler refreshList = new Handler() {
		@Override
		public void handleMessage(final Message mess) {
			List<Annonce> liste = AnnonceRepository
					.GetAllAnnoncesByCoursId(currentCours.getId());
			AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
			setListAdapter(adapter);
		}
	};

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (!Repository.isOpen()) {
			Repository.Open();
		}

		Bundle extras = getArguments();
		if (extras != null) {
			currentCours = CoursRepository.GetById(extras.getInt("coursID"));
		}

		List<Annonce> liste = AnnonceRepository
				.GetAllAnnoncesByCoursId(currentCours.getId());
		AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.standard_list, null);
	}

	@Override
	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		Annonce item = (Annonce) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity(), activity.detailsAnnonce.class);
		intent.putExtra("annID", item.getId());
		startActivity(intent);
	}
}
