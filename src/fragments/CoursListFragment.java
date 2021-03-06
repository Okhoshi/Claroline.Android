package fragments;

import java.util.List;

import model.Cours;
import net.claroline.mobile.android.R;
import activity.CoursActivity;
import adapter.CoursAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import app.App;

import com.activeandroid.query.Select;

public class CoursListFragment extends ListFragment {

	/**
	 * Contextual Menu ID.
	 */
	private static final int MAIL_ID = 0;

	/**
	 * Currently selected position.
	 */
	private int mPosition = ListView.INVALID_POSITION;

	/**
	 * Dismiss the TwoPane selection.
	 */
	public void dismissSelection() {
		if (App.isTwoPane()) {
			getListView().setItemChecked(mPosition, false);

			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.content_fragment, new NewsFragment());
			ft.commit();
			mPosition = AdapterView.INVALID_POSITION;
		}
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (App.isTwoPane()) {
			getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		} else {
			getListView().setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		}

		refreshUI();
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
					new String[] { ((Cours) getListAdapter().getItem(
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
		return inflater.inflate(R.layout.cours_list, container);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {

		Cours item = (Cours) getListAdapter().getItem(position);

		if (App.isTwoPane()) {
			if (position != mPosition) {
				Bundle data = new Bundle();
				data.putLong("coursID", item.getId());

				Fragment tvpf = new ToolViewPagerFragment();
				tvpf.setArguments(data);

				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.content_fragment, tvpf);
				ft.commit();
				mPosition = position;
			} else {
				dismissSelection();
			}

		} else {
			Intent intent = new Intent(getActivity(), CoursActivity.class);
			intent.putExtra("coursID", item.getId());
			startActivity(intent);
		}

	}

	/**
	 * Refreshes the UI.
	 */
	public void refreshUI() {
		List<Cours> liste = new Select().from(Cours.class).execute();
		CoursAdapter adapter = new CoursAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}
}
