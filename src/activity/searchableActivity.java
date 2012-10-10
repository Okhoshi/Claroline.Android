package activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobile.claroline.R;
import model.Annonce;
import model.Cours;
import model.Documents;
import adapter.SearchListAdapter;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import app.AppActivity;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DBOpenHelper;
import dataStorage.DocumentsRepository;

//@SuppressLint("ParserError")
public class searchableActivity extends AppActivity {

	private static final String LIKE_SEL = " LIKE ? ";
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.standard_list);

		// permet de retourner sur la vue précédente
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		mListView = (ListView) findViewById(android.R.id.list);

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

		Map<String,List<?>> resultC = new HashMap<String,List<?>>();
		resultC.put("Cours",courses);

		List<Annonce> annonces = AnnonceRepository.QueryOnDB(DBOpenHelper.ANNONCE_COLUMN_CONTENT + LIKE_SEL + "OR "+
				DBOpenHelper.ANNONCE_COLUMN_TITLE + LIKE_SEL,
				new String[] {"%" + query + "%",
				"%" + query + "%"});

		Map<String,List<?>> resultA = new HashMap<String,List<?>>();
		resultA.put("Annonce",annonces);

		List<Documents> documents = DocumentsRepository.QueryOnDB(DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION + LIKE_SEL + "OR "+
				DBOpenHelper.DOCUMENTS_COLUMN_NAME + LIKE_SEL + "OR " +
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION + LIKE_SEL, new String[] {"%" + query + "%",
				"%" + query + "%",
				"%" + query + "%"});

		Map<String,List<?>> resultD = new HashMap<String,List<?>>();
		resultD.put("Documents",documents);

		List<Map<String,List<?>>> results = new ArrayList<Map<String,List<?>>>();
		results.add(resultC);
		results.add(resultA);
		results.add(resultD);

		SearchListAdapter adapter = new SearchListAdapter(this, results, query);
		mListView.setAdapter(adapter);

}

public void onRepositoryRefresh(String type) {
	// ignore
}

}
