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

import java.io.File;
import java.util.List;
import java.util.Locale;

import model.Document;
import model.ResourceList;
import net.claroline.mobile.android.R;

import org.joda.time.DateTime;

import adapter.DocumentsAdapter;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
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
	 * Current document on root.
	 */
	private Document mCurrentRoot;

	/**
	 * UI references.
	 */
	private TextView mCurrentPath;

	/**
	 * Handler for the {@link Document} download with token.
	 */
	private final AsyncHttpResponseHandler mTokenizedURLHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(final String content) {
			System.out.println("Call with Tokenized : " + content);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(content));
			try {
				startActivity(i);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(getActivity(),
						getString(R.string.app_not_found), Toast.LENGTH_LONG)
						.show();
			}
		}
	};

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
		MimeTypeMap map = MimeTypeMap.getSingleton();
		final String mime = map.getMimeTypeFromExtension(item.getExtension());

		if (item.getIsFolder()) {
			mCurrentRoot = item;
			mCurrentPath.setText(mCurrentRoot.getFullPath());
			refreshUI();
		} else {
			if (mime != null) {
				openFileInMemory(item, mime);
			} else {
				((AppActivity) getActivity()).getService()
						.getDownloadTokenizedUrl(
								mCurrentList.getCours().getSysCode(),
								item.getResourceString(), mTokenizedURLHandler);
			}
		}
	}

	/**
	 * Opens the document if it is on storage, else downloads it before open it.
	 * 
	 * @param item
	 *            the document to open
	 * @param mime
	 *            the mime type of the document
	 */
	public void openFileInMemory(final Document item, final String mime) {
		if (item.isOnMemory()) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
					+ "/"
					+ getString(R.string.app_name)
					+ "/"
					+ mCurrentList.getCours().getOfficialCode()
					+ "/"
					+ item.getTitle() + "." + item.getExtension())), mime
					.toLowerCase(Locale.US));
			startActivity(Intent.createChooser(i,
					getString(R.string.dialog_choose_app)));
		} else {
			((AppActivity) getActivity()).getService().getDownloadTokenizedUrl(
					mCurrentList.getCours().getSysCode(),
					item.getResourceString(), new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(final Throwable error,
								final String content) {
							System.out.println(error.getLocalizedMessage()
									+ " : " + content);
							super.onFailure(error, content);
						}

						@TargetApi(Build.VERSION_CODES.HONEYCOMB)
						@Override
						public void onSuccess(final String content) {
							System.out.println("Call with Tokenized : "
									+ content);
							Request request = new Request(Uri.parse(content));

							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								request.allowScanningByMediaScanner();
								request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
							}
							request.setDestinationInExternalPublicDir(
									Environment.DIRECTORY_DOWNLOADS
											+ "/"
											+ getString(R.string.app_name)
											+ "/"
											+ mCurrentList.getCours()
													.getOfficialCode() + "/",
									item.getTitle() + "." + item.getExtension());

							final DownloadManager manager = (DownloadManager) getActivity()
									.getSystemService(Context.DOWNLOAD_SERVICE);
							final long id = manager.enqueue(request);

							BroadcastReceiver bcr = new BroadcastReceiver() {
								@Override
								public void onReceive(final Context context,
										final Intent intent) {
									String action = intent.getAction();
									if (DownloadManager.ACTION_DOWNLOAD_COMPLETE
											.equals(action)) {
										Bundle extras = intent.getExtras();
										if (id == extras
												.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)) {
											openFileInMemory(item, mime);
											getActivity().unregisterReceiver(
													this);
										}
									}
								}
							};

							getActivity()
									.registerReceiver(
											bcr,
											new IntentFilter(
													DownloadManager.ACTION_DOWNLOAD_COMPLETE));
							item.setLoadedDate(DateTime.now());
						}
					});
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
