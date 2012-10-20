package activity;

import java.util.ArrayList;
import java.util.List;

import mobile.claroline.R;
import model.Annonce;
import model.Cours;
import model.Documents;
import adapter.SearchListAdapter;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import app.AppActivity;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DBOpenHelper;
import dataStorage.DocumentsRepository;

public class searchableActivity extends AppActivity implements OnChildClickListener {

	private static final String LIKE_SEL = " LIKE ? ";
	private static final int COURS = 0;
	private static final int ANNONCE = 1;
	private static final int DOCUMENTS = 2;
	private ExpandableListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		// permet de retourner sur la vue précédente
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		mListView = (ExpandableListView) findViewById(android.R.id.list);

		handleIntent(getIntent());
	}

	@Override
	public void onNewIntent(Intent intent){
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	/**
	 * Searches the cours_table and displays results for the given query.
	 * 
	 * @param query
	 *            The search query
	 */
	public void doMySearch(String query) {

		List<Cours> courses = CoursRepository.QueryOnDB(DBOpenHelper.COURS_COLUMN_TITLE + LIKE_SEL +"OR " + 
				DBOpenHelper.COURS_COLUMN_SYSCODE + LIKE_SEL,
				new String[] {"%" + query + "%",
				"%" + query + "%"});

		SparseArray<List<?>> resultC = new SparseArray<List<?>>();
		resultC.put(COURS,courses);

		List<Annonce> annonces = AnnonceRepository.QueryOnDB(DBOpenHelper.ANNONCE_COLUMN_CONTENT + LIKE_SEL + "OR "+
				DBOpenHelper.ANNONCE_COLUMN_TITLE + LIKE_SEL,
				new String[] {"%" + query + "%",
				"%" + query + "%"},
				DBOpenHelper.ANNONCE_COLUMN_COURSID);

		SparseArray<List<?>> resultA = new SparseArray<List<?>>();
		resultA.put(ANNONCE,annonces);

		List<Documents> documents = DocumentsRepository.QueryOnDB(DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION + LIKE_SEL + "OR "+
				DBOpenHelper.DOCUMENTS_COLUMN_NAME + LIKE_SEL + "OR " +
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION + LIKE_SEL, new String[] {"%" + query + "%",
				"%" + query + "%",
				"%" + query + "%"},
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID);

		SparseArray<List<?>> resultD = new SparseArray<List<?>>();
		resultD.put(DOCUMENTS,documents);

		List<SparseArray<List<?>>> results = new ArrayList<SparseArray<List<?>>>();
		results.add(resultC);
		results.add(resultA);
		results.add(resultD);

		SearchListAdapter adapter = new SearchListAdapter(this, results, query);
		mListView.setAdapter(adapter);
		mListView.setOnChildClickListener(this);
	}

	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Intent i;
		switch (groupPosition) {
		case COURS:
			i = new Intent(this, coursActivity.class);
			i.putExtra("coursID", ((Cours) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).getId());
			startActivity(i);
			break;
		case ANNONCE:
			i = new Intent(this, detailsAnnonce.class);
			i.putExtra("annID", ((Annonce) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).getId());
			i.putExtra("tab", 0);
			startActivity(i);
			break;
		case DOCUMENTS:
			i = new Intent(this, coursActivity.class);
			i.putExtra("coursID", ((Documents) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).getCours().getId());
			i.putExtra("tab", 1);
			i.putExtra("id", ((Documents) parent.getExpandableListAdapter().getChild(groupPosition, childPosition)).getId());
			startActivity(i);
			break;
		default:
			return false;
		}
		return true;
	}

	public void onRepositoryRefresh(String type) {
		// ignore
	}

}
