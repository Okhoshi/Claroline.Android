package fragments;

import java.io.File;
import java.util.List;
import java.util.Locale;

import model.OldCours;
import model.Document;
import net.claroline.mobile.android.R;
import adapter.DocumentsAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.TextView;
import app.App;
import app.AppActivity;
import connectivity.SupportedMethods;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
import dataStorage.Repository;

public class documentsListFragment extends ListFragment {

	private OldCours currentCours;
	private Document currentRoot;

	private TextView currentPath;

	public Handler refreshList = new Handler() {
		@Override
		public void handleMessage(final Message mess) {
			switch (mess.what) {
			case 1:
				currentRoot = currentRoot.getRoot();
				currentPath.setText(currentRoot.getFullPath());
			case 0:
				List<Document> liste = currentRoot.getContent();
				DocumentsAdapter adapter = new DocumentsAdapter(getActivity(),
						liste);
				setListAdapter(adapter);
				break;
			}
		}
	};

	public boolean isOnRoot() {
		return currentRoot.equals(Document.getEmptyRoot(currentCours));
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (!Repository.isOpen()) {
			Repository.Open();
		}

		currentPath = (TextView) getView().findViewById(R.id.currentPath);

		int id = -1;

		Bundle extras = getArguments();
		if (extras != null) {
			currentCours = CoursRepository.GetById(extras.getInt("coursID"));
			id = extras.getInt("docID", -1);
		}
		if (id != -1) {
			currentRoot = DocumentsRepository.GetById(id).getRoot();
		} else if (currentRoot == null) {
			currentRoot = Document.getEmptyRoot(currentCours);
		}
		currentPath.setText(currentRoot.getFullPath());

		List<Document> liste = currentRoot.getContent();
		DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
		setListAdapter(adapter);
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

		if (item.isFolder()) {
			currentRoot = item;
			currentPath.setText(currentRoot.getFullPath());
			List<Document> liste = currentRoot.getContent();
			DocumentsAdapter adapter = new DocumentsAdapter(getActivity(),
					liste);
			setListAdapter(adapter);
		} else {
			if (mime != null) {
				/*
				 * Actually unusable because of a bugfix-bug into the Claroline
				 * Kernel AlertDialog.Builder builder = new
				 * AlertDialog.Builder(getActivity());
				 * builder.setMessage(R.string.save_or_open_dialog)
				 * .setCancelable(true) .setPositiveButton(R.string.save_dialog,
				 * new DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) {
				 * if(item.isOnMemory()){ Intent i = new
				 * Intent(Intent.ACTION_VIEW); i.setDataAndType(Uri.fromFile(new
				 * File
				 * (Environment.getExternalStoragePublicDirectory(Environment
				 * .DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" +
				 * getString(R.string.app_name) + "/" + item.getName() + "." +
				 * item.getExtension())), mime.toLowerCase());
				 * startActivity(Intent
				 * .createChooser(i,getString(R.string.dialog_choose_app))); }
				 * else { GlobalApplication.setProgressIndicator(getActivity(),
				 * true); (new
				 * Thread(GlobalApplication.getClient(((AppActivity)getActivity
				 * ()).handler, AllowedOperations.downloadFile, item.getCours(),
				 * item.getId()))).start(); dialog.dismiss(); } } })
				 * .setNegativeButton(R.string.open_dialog, new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { Intent i = new
				 * Intent(Intent.ACTION_VIEW); i.setData(Uri.parse("http://" +
				 * item.getUrl() + "&login=" +
				 * GlobalApplication.getPreferences().getString("user_login",
				 * "qdevos") + "&password=" +
				 * GlobalApplication.getPreferences().getString("user_password",
				 * "elegie24"))); try { startActivity(i); } catch
				 * (ActivityNotFoundException e) { Toast.makeText(getActivity(),
				 * getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
				 * } dialog.dismiss(); } }) .show();
				 */
				if (item.isOnMemory()) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setDataAndType(Uri.fromFile(new File(Environment
							.getExternalStoragePublicDirectory(
									Environment.DIRECTORY_DOWNLOADS)
							.getAbsolutePath()
							+ "/"
							+ getString(R.string.app_name)
							+ "/"
							+ item.getName() + "." + item.getExtension())),
							mime.toLowerCase(Locale.US));
					startActivity(Intent.createChooser(i,
							getString(R.string.dialog_choose_app)));
				} else {
					((AppActivity) getActivity()).setProgressIndicator(true);
					new Thread(App.getClient(
							((AppActivity) getActivity()).mAppHandler,
							SupportedMethods.downloadFile, item.getCours(),
							item.getId())).start();
				}
			} else {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://"
						+ item.getUrl()
						+ "&login="
						+ App.getPrefs()
								.getString("user_login", "qdevos")
						+ "&password="
						+ App.getPrefs().getString("user_password",
								"elegie24")));
				startActivity(i);
			}
		}
	}
}
