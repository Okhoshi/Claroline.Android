package fragments;

import java.util.List;

import model.Annonce;
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
import app.App;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;

public class AnnonceListFragment extends ListFragment {

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
					.where("Cours = ? AND label = ?", extras.get("coursID"),
							SupportedModules.CLANN.name()).executeSingle();
		}

		if (mCurrentList != null) {
			if (mCurrentList.isExpired()
					|| mCurrentList.resources().size() == 0
					&& ((AppActivity) getActivity())
							.mustUpdate(AppActivity.ONCE_PER_DAY)) {
				((AppActivity) getActivity()).setProgressIndicator(true);
				((AppActivity) getActivity()).getService().getResourcesForList(
						mCurrentList, new AsyncHttpResponseHandler() {
							@Override
							public void onFinish() {
								if (getActivity() != null) {
									((AppActivity) getActivity())
											.setProgressIndicator(false);
								}
							}

							@Override
							public void onSuccess(final String content) {
								refreshUI();
								if (getActivity() != null) {
									((AppActivity) getActivity()).updatesNow();
								}
							}
						});
			} else if (mCurrentList.isTimeToUpdate()) {
				((AppActivity) getActivity()).getService().getUpdates(
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(final String content) {
								if (!content.equals("[]")) {
									refreshUI();
								}
								if (getActivity() != null) {
									((AppActivity) getActivity()).updatesNow();
								}
							}
						});
			}
			refreshUI();
		}
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
		if (App.isTwoPane()) {
			Bundle data = new Bundle();
			data.putLong("resID", item.getId());
			data.putString("label", "CLANN");

			DetailFragment f = new AnnonceDetailFragment();
			f.setArguments(data);
			f.show(getActivity().getSupportFragmentManager(), "dialog");
		} else {
			Intent intent = new Intent(getActivity(),
					activity.DetailsActivity.class);
			intent.putExtra("resID", item.getId());
			intent.putExtra("label", "CLANN");
			startActivity(intent);
		}
	}

	public void refreshUI() {
		List<Annonce> liste = mCurrentList.resources();
		AnnonceAdapter adapter = new AnnonceAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}
}
