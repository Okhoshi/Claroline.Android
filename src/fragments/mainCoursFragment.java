package fragments;

import java.util.List;

import mobile.claroline.R;
import model.Annonce;
import model.AnnonceAdapter;
import model.Cours;
import model.CoursAdapter;
import model.Documents;
import model.DocumentsAdapter;
import activity.home;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import dataStorage.CoursRepository;

public class mainCoursFragment extends ListFragment 
{
	
	private static final int MAIL_ID = R.id.itemMails;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		int layoutID = R.layout.cours_view;
		
		List<Cours> Liste = CoursRepository.GetAllCours();
		CoursAdapter adapter = new CoursAdapter(getActivity(),layoutID, Liste);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			int layoutID = R.layout.cours_view;
			List<Cours> Liste = CoursRepository.GetAllCours();
			CoursAdapter adapter = new CoursAdapter(getActivity(),layoutID, Liste);
			setListAdapter(adapter);
		}
	};

	
	/**
	 * 
	 *   OnListItemClick
	 * 
	 * 
	 */
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Cours item = (Cours) getListAdapter().getItem(position);
		
		Intent intent = new Intent(getActivity().getApplicationContext(), activity.coursActivity.class);
		intent.putExtra("coursID", item.getId());
		startActivity(intent);
	}
	
	
	/**
	 * 
	 *   All about the contextual menu
	 * 
	 * 
	 */
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.contextual_header));
		menu.add(0, MAIL_ID, 0, getString(R.string.contextual_mail));
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) 
		{
		case MAIL_ID:
			//String officialEmail = currentCours.getOfficialEmail();
			// Pour test
			String officialEmail = "eldala07@hotmail.com";
			Intent i = new Intent(Intent.ACTION_SEND);
			i.putExtra(Intent.EXTRA_EMAIL, new String[] {officialEmail});
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_titulars));
			i.setType("message/rfc822");
			startActivity(Intent.createChooser(i, getString(R.string.choose_activity_mail)));
			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}
}
