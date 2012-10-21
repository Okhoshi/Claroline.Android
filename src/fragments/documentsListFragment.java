package fragments;

import java.util.List;

import mobile.claroline.R;
import model.Cours;
import model.Documents;
import adapter.DocumentsAdapter;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
import dataStorage.Repository;

public class documentsListFragment extends ListFragment {

	private Cours currentCours;
	private Documents currentRoot;

	private TextView currentPath;

	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			switch(mess.what){
			case 1:
				currentRoot = currentRoot.getRoot();
				currentPath.setText(currentRoot.getFullPath());
			case 0:
				List<Documents> liste = currentRoot.getContent();
				DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
				setListAdapter(adapter);
				break;
			}
		}
	};

	public boolean isOnRoot(){
		return currentRoot.equals(Documents.getEmptyRoot(currentCours));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.documents_list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(!Repository.isOpen()){
			Repository.Open();
		}

		currentPath = (TextView) getView().findViewById(R.id.currentPath);

		int id = -1;

		Bundle extras = getArguments();
		if (extras != null)
		{
			currentCours=CoursRepository.GetById(extras.getInt("coursID"));
			id = extras.getInt("docID", -1);
		}
		if(id != -1){
			currentRoot = DocumentsRepository.GetById(id).getRoot();
		} else if(currentRoot == null){
			currentRoot = Documents.getEmptyRoot(currentCours);
		}
		currentPath.setText(currentRoot.getFullPath());

		List<Documents> liste = currentRoot.getContent();
		DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		final Documents item = (Documents) getListAdapter().getItem(position);

		if(item.isFolder()){
			currentRoot = item;
			currentPath.setText(currentRoot.getFullPath());
			List<Documents> liste = currentRoot.getContent();
			DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
			setListAdapter(adapter);
		} else {
			MimeTypeMap map = MimeTypeMap.getSingleton();
			String mime = map.getMimeTypeFromExtension(item.getExtension());

			if(mime != null){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage(R.string.save_or_open_dialog)
				.setCancelable(true)
				.setPositiveButton(R.string.save_dialog, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GlobalApplication.setProgressIndicator(getActivity(), true);
						(new Thread(GlobalApplication.getClient(((AppActivity)getActivity()).handler, AllowedOperations.downloadFile, item.getCours(), item.getId()))).start();
						dialog.dismiss();
					}
				})
				.setNegativeButton(R.string.open_dialog, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse("http://" + item.getUrl() + 
								"&login=" + GlobalApplication.getPreferences().getString("user_login", "qdevos") + 
								"&password=" + GlobalApplication.getPreferences().getString("user_password", "elegie24")));
						try {
							startActivity(i);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(getActivity(), getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
						}
						dialog.dismiss();
					}
				})
				.show();
			} else {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://" + item.getUrl() + 
						"&login=" + GlobalApplication.getPreferences().getString("user_login", "qdevos") + 
						"&password=" + GlobalApplication.getPreferences().getString("user_password", "elegie24")));
					startActivity(i);
			}
		}
	}
}
