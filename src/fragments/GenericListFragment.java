package fragments;

import java.util.List;

import model.ResourceList;
import model.ResourceModel;
import net.claroline.mobile.android.R;
import adapter.GenericAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
			// FIXME wrong request... Id replace with the course one
			mCurrentList = new Select()
					.from(ResourceList.class)
					.where("Cours = ? AND label = ?", extras.get("coursID"),
							extras.getString("type")).executeSingle();
		}

		if (mCurrentList != null) {
			if (mCurrentList.isExpired()
					|| mCurrentList.resources().size() == 0) {
				((AppActivity) getActivity()).setProgressIndicator(true);
				((AppActivity) getActivity()).getService().getResourcesForList(
						mCurrentList, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(final String content) {
								((AppActivity) getActivity())
										.setProgressIndicator(false);
								refreshUI();
							}
						});
			} else if (mCurrentList.isTimeToUpdate()) {
				((AppActivity) getActivity()).getService().getUpdates(
						new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(final String content) {
								((AppActivity) getActivity())
										.setProgressIndicator(false);
								if (!content.equals("[]")) {
									refreshUI();
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

	public void refreshUI() {
		List<ResourceModel> liste = mCurrentList.resources();
		GenericAdapter<ResourceModel> adapter = new GenericAdapter<ResourceModel>(
				getActivity(), liste);
		setListAdapter(adapter);
	}
}
