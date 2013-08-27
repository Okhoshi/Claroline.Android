package fragments;

import java.util.List;

import model.OldCours;
import net.claroline.mobile.android.R;
import adapter.CoursAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import dataStorage.CoursRepository;
import dataStorage.Repository;

public class coursListFragment extends ListFragment {

	private static final int MAIL_ID = 0;

	public Handler refreshList = new Handler() {
		@Override
		public void handleMessage(final Message mess) {
			List<OldCours> liste = CoursRepository.GetAllCours();
			CoursAdapter adapter = new CoursAdapter(getActivity(), liste);
			setListAdapter(adapter);
		}
	};

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (!Repository.isOpen()) {
			Repository.Open();
		}

		List<OldCours> liste = CoursRepository.GetAllCours();
		CoursAdapter adapter = new CoursAdapter(getActivity(), liste);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case MAIL_ID:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.putExtra(
					Intent.EXTRA_EMAIL,
					new String[] { ((OldCours) getListAdapter().getItem(
							info.position)).getOfficialEmail() });
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_titulars));
			i.setType("message/rfc822");
			startActivity(Intent.createChooser(i,
					getString(R.string.choose_activity_mail)));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.contextual_header));
		menu.add(Menu.NONE, MAIL_ID, Menu.NONE,
				getString(R.string.contextual_mail));
	}

	/*
	 * All about the contextual menu
	 */

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.standard_list, container);
	}

	@Override
	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {

		OldCours item = (OldCours) getListAdapter().getItem(position);

		Intent intent = new Intent(getActivity(), activity.coursActivity.class);
		intent.putExtra("coursID", item.getId());
		startActivity(intent);
	}
}
