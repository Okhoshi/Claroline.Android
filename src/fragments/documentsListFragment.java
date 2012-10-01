package fragments;

import java.util.List;

import connectivity.AllowedOperations;

import mobile.claroline.R;
import model.Cours;
import model.Documents;
import adapter.DocumentsAdapter;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import android.widget.Toast;
import app.AppActivity;
import app.GlobalApplication;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;

public class documentsListFragment extends ListFragment {

	private Cours currentCours;
	private Documents currentDoc;
	
	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			List<Documents> liste = DocumentsRepository.GetDocListByCoursId(currentCours.getId());
			DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
			setListAdapter(adapter);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return super.onCreateView(inflater, container, savedInstanceState);
		//return inflater.inflate(R.layout.standard_list, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle extras = getArguments();
		if (extras != null)
	    {
	        currentCours=CoursRepository.GetById(extras.getInt("coursID"));			
	    }

		List<Documents> liste = DocumentsRepository.GetDocListByCoursId(currentCours.getId());
		DocumentsAdapter adapter = new DocumentsAdapter(getActivity(), liste);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		final Documents item = (Documents) getListAdapter().getItem(position);
		
		MimeTypeMap map = MimeTypeMap.getSingleton();
		final String mimeType = map.getMimeTypeFromExtension(item.getExtension());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Do you want to save or just open this document ?")
		       .setCancelable(true)
		       .setPositiveButton("Save", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		       		GlobalApplication.setProgressIndicator(getActivity(), true);
		       		(new Thread(GlobalApplication.getClient().makeOperation(AppActivity.handler, AllowedOperations.downloadFile, item.getId()))).start();
		       		dialog.dismiss();
		           }
		       })
		       .setNegativeButton("Open", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		       		Intent i = new Intent(Intent.ACTION_VIEW);
		       		i.setDataAndType(Uri.parse("http://" + item.getUrl() + 
		       						"&login=" + GlobalApplication.getPreferences().getString("user_login", "qdevos") + 
		       						"&password=" + GlobalApplication.getPreferences().getString("user_password", "elegie24")), mimeType);
		       		if(getActivity().getPackageManager().resolveActivity(i, PackageManager.GET_INTENT_FILTERS) != null){
		       			startActivity(i);
		       		}
		       		dialog.dismiss();
		           }
		       })
		       .show();
	}
}
