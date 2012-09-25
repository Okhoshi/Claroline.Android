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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import dataStorage.CoursRepository;

public class coursListFragment extends ListFragment 
{
	
	private static final int MAIL_ID = R.id.itemMails;
	private static final int LIST_ITEM_LAYOUT = R.layout.two_lines_details_list_item;
	
	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			liste = CoursRepository.GetAllCours();
			CoursAdapter adapter = new CoursAdapter(getActivity(),LIST_ITEM_LAYOUT, liste);
			setListAdapter(adapter);
		}
	};
	
	private List<Cours> liste;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		liste = CoursRepository.GetAllCours();
		CoursAdapter adapter = new CoursAdapter(getActivity(),LIST_ITEM_LAYOUT, liste);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	/*
	 *   All about the contextual menu 
	 */
	
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(getString(R.string.contextual_header));
		menu.add(0, MAIL_ID, 0, getString(R.string.contextual_mail));
	}

	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()){
		case MAIL_ID:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.putExtra(Intent.EXTRA_EMAIL, new String[] {liste.get(info.position).getOfficialEmail()});
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_titulars));
			i.setType("message/rfc822");
			startActivity(Intent.createChooser(i, getString(R.string.choose_activity_mail)));
			return true;
		default:
			return super.onContextItemSelected(item);
		}

	}
}
