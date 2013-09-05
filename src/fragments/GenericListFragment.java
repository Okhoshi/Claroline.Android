package fragments;

import java.util.List;

import model.ModelBase;
import model.ResourceList;
import model.ResourceModel;
import net.claroline.mobile.android.R;
import adapter.GenericAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GenericListFragment extends ListFragment {

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
							extras.getString("type")).executeSingle();
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
								((AppActivity) getActivity())
										.setProgressIndicator(false);
							}

							@Override
							public void onSuccess(final String content) {
								refreshUI();
								((AppActivity) getActivity()).updatesNow();
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
								((AppActivity) getActivity()).updatesNow();
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
		ModelBase item = (ModelBase) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity(),
				activity.DetailsActivity.class);
		intent.putExtra("resID", item.getId());
		intent.putExtra("label", mCurrentList.getLabel());
		startActivity(intent);
	}

	public void refreshUI() {
		List<ResourceModel> liste = mCurrentList.resources();
		GenericAdapter<ResourceModel> adapter = new GenericAdapter<ResourceModel>(
				getActivity(), liste);
		setListAdapter(adapter);
	}
}
