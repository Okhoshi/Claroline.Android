package fragments;

import java.io.File;
import java.util.List;
import java.util.Locale;

import model.Cours;
import model.Document;
import model.ResourceList;
import net.claroline.mobile.android.R;
import adapter.DocumentsAdapter;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import connectivity.SupportedModules;

public class documentsListFragment extends ListFragment {

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
	private TextView currentPath;

	public void goUp() {
		mCurrentRoot = mCurrentRoot.getRoot();
		currentPath.setText(mCurrentRoot.getFullPath());
		refreshUI();
	}

	public boolean isOnRoot() {
		return mCurrentRoot.equals(Document.getEmptyRoot(mCurrentList));
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		currentPath = (TextView) getView().findViewById(R.id.currentPath);

		int id = -1;

		Bundle extras = getArguments();
		if (extras != null) {
			mCurrentList = new Select()
					.from(ResourceList.class)
					.innerJoin(Cours.class)
					.on("Cours.Id = ResourceList.Cours")
					.where("Cours.Id = ? AND ResourceList.label = ?",
							extras.get("coursID"),
							SupportedModules.CLDOC.name()).executeSingle();
			id = extras.getInt("docID", -1);
		}

		if (mCurrentList == null) {
			return;
		}

		if (id != -1) {
			mCurrentRoot = ((Document) new Select().from(Document.class)
					.where("Id = ", id).executeSingle()).getRoot();
		} else if (mCurrentRoot == null) {
			mCurrentRoot = Document.getEmptyRoot(mCurrentList);
		}
		currentPath.setText(mCurrentRoot.getFullPath());

		refreshUI();
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
			currentPath.setText(mCurrentRoot.getFullPath());
			refreshUI();
		} else {
			if (mime != null) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage(R.string.save_or_open_dialog)
						.setCancelable(true)
						.setPositiveButton(R.string.save_dialog,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										openFileInMemory(item, mime);
									}
								})
						.setNegativeButton(R.string.open_dialog,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										Intent i = new Intent(
												Intent.ACTION_VIEW);
										// TODO Get the url tokenized for this
										// document
										i.setData(Uri.parse(""));
										try {
											startActivity(i);
										} catch (ActivityNotFoundException e) {
											Toast.makeText(
													getActivity(),
													getString(R.string.app_not_found),
													Toast.LENGTH_LONG).show();
										}
										dialog.dismiss();
									}
								}).show();
			} else {
				Intent i = new Intent(Intent.ACTION_VIEW);
				// TODO Get the url tokenized
				i.setData(Uri.parse(""));
				startActivity(i);
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
					+ item.getTitle() + "." + item.getExtension())), mime
					.toLowerCase(Locale.US));
			startActivity(Intent.createChooser(i,
					getString(R.string.dialog_choose_app)));
		} else {
			((AppActivity) getActivity()).setProgressIndicator(true);
			// TODO Download the doc
		}
	}

	public void refreshUI() {
		List<Document> liste = mCurrentRoot.getContent();
		DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}
}
