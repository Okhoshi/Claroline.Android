/**
 * Claroline Mobile - Android
 * 
 * @package     fragments
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package fragments;

import java.util.List;

import model.Document;
import model.ResourceList;
import net.claroline.mobile.android.R;
import activity.DetailsActivity;
import adapter.DocumentsAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;

/**
 * Claroline Mobile - Android
 * 
 * The {@link Document} {@link ListFragment}.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
public class DocumentsListFragment extends ListFragment {

	/**
	 * Current list viewed.
	 */
	private ResourceList mCurrentList;

	/**
	 * UI references.
	 */
	private TextView mCurrentPath;

	/**
	 * Current document on root.
	 */
	private Document mCurrentRoot;

	/**
	 * Shows the content of the root of the current document.
	 */
	public void goUp() {
		mCurrentRoot = mCurrentRoot.getRoot();
		mCurrentPath.setText(mCurrentRoot.getFullPath());
		refreshUI();
	}

	/**
	 * @return true if the current document is the root, false otherwise
	 */
	public boolean isOnRoot() {
		return mCurrentRoot.getFullPath().equals("/");
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mCurrentPath = (TextView) getView().findViewById(R.id.currentPath);

		long id = -1;

		Bundle extras = getArguments();
		if (extras != null) {
			mCurrentList = new Select()
					.from(ResourceList.class)
					.where("Cours = ? AND label = ?", extras.get("coursID"),
							SupportedModules.CLDOC.name()).executeSingle();
			id = extras.getLong("docID", -1);
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

			if (id != -1) {
				mCurrentRoot = ((Document) new Select().from(Document.class)
						.where("Id = ", id).executeSingle()).getRoot();
			} else if (mCurrentRoot == null) {
				mCurrentRoot = Document.getEmptyRoot(mCurrentList);
			}
			refreshUI();
		} else {
			getActivity().finish();
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.documents_list, null);
	}

	@Override
	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		final Document item = (Document) getListAdapter().getItem(position);

		if (item.getIsFolder()) {
			mCurrentRoot = item;
			mCurrentPath.setText(mCurrentRoot.getFullPath());
			refreshUI();
		} else {
			Intent intent = new Intent(getActivity(), DetailsActivity.class);
			intent.putExtra("resID", item.getId());
			intent.putExtra("label", mCurrentList.getLabel());
			startActivity(intent);
		}
	}

	/**
	 * Refresh the UI.
	 */
	public void refreshUI() {
		List<Document> liste = mCurrentRoot.getContent();
		DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
		setListAdapter(adapter);
		mCurrentPath.setText(mCurrentRoot.getFullPath());
	}
}
