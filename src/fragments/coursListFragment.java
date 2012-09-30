package fragments;

import java.util.List;

import mobile.claroline.R;
import model.Cours;
import adapter.CoursAdapter;
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
	
	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
			List<Cours> liste = CoursRepository.GetAllCours();
			CoursAdapter adapter = new CoursAdapter(getActivity(), liste);
			setListAdapter(adapter);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.standard_list, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		List<Cours> liste = CoursRepository.GetAllCours();
		CoursAdapter adapter = new CoursAdapter(getActivity(), liste);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		Cours item = (Cours) getListAdapter().getItem(position);
		Intent intent = new Intent(getActivity(), activity.coursActivity.class);
		intent.putExtra("coursID", item.getId());
		startActivity(intent);
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
			i.putExtra(Intent.EXTRA_EMAIL, new String[] {((Cours) getListAdapter().getItem(info.position)).getOfficialEmail()});
			i.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_titulars));
			i.setType("message/rfc822");
			startActivity(Intent.createChooser(i, getString(R.string.choose_activity_mail)));
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
}