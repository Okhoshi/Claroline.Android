package fragments;

import java.util.List;

import model.Annonce;
import model.Cours;
import model.ResourceList;
import net.claroline.mobile.android.R;
import adapter.AnnonceAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;

public class annonceListFragment extends ListFragment {

	/**
	 * The current viewed list.
	 */
	private ResourceList mCurrentList;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle extras = getArguments();
		if (extras != null) {
			mCurrentList = new Select()
					.from(ResourceList.class)
					.innerJoin(Cours.class)
					.on("Cours.Id = ResourceList.Cours")
					.where("Cours.Id = ? AND ResourceList.label = CLANN",
							extras.get("coursID")).executeSingle();
		}

		refreshUI();
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
		Intent intent = new Intent(getActivity(), activity.DetailsAnnonce.class);
		intent.putExtra("annID", item.getId());
		startActivity(intent);
	}

	public void refreshUI() {
		List<Annonce> liste = mCurrentList.resources();
		AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}
}
