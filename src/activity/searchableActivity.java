package activity;

import java.util.ArrayList;
import java.util.List;

import model.Annonce;
import model.Cours;
import model.Document;
import net.claroline.mobile.android.R;
import adapter.SearchListAdapter;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import app.AppActivity;

import com.activeandroid.query.Select;

public class searchableActivity extends AppActivity implements
		OnChildClickListener {

	private static final int COURS = 0;
	private static final int ANNONCE = 1;
	private static final int DOCUMENTS = 2;
	private ExpandableListView mListView;

	/**
	 * Searches the cours_table and displays results for the given query.
	 * 
	 * @param query
	 *            The search query
	 */
	public void doMySearch(final String query) {

		List<Cours> courses = new Select()
				.from(Cours.class)
				.where("Name LIKE ? OR SysCode LIKE ?", '%' + query + '%',
						'%' + query + '%').execute();

		SparseArray<List<?>> resultC = new SparseArray<List<?>>();
		resultC.put(COURS, courses);

		List<Annonce> annonces = new Select()
				.from(Annonce.class)
				.where("Title LIKE ? OR Content LIKE ?", '%' + query + '%',
						'%' + query + '%').execute();

		SparseArray<List<?>> resultA = new SparseArray<List<?>>();
		resultA.put(ANNONCE, annonces);

		List<Document> documents = new Select()
				.from(Document.class)
				.where("Title LIKE ? OR Description LIKE ? OR Extension LIKE ?",
						'%' + query + '%', '%' + query + '%', '%' + query + '%')
				.execute();

		SparseArray<List<?>> resultD = new SparseArray<List<?>>();
		resultD.put(DOCUMENTS, documents);

		List<SparseArray<List<?>>> results = new ArrayList<SparseArray<List<?>>>();
		results.add(resultC);
		results.add(resultA);
		results.add(resultD);

		SearchListAdapter adapter = new SearchListAdapter(this, results, query);
		mListView.setAdapter(adapter);
		mListView.setOnChildClickListener(this);
	}

	private void handleIntent(final Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	@Override
	public boolean onChildClick(final ExpandableListView parent, final View v,
			final int groupPosition, final int childPosition, final long id) {
		Intent i;
		switch (groupPosition) {
		case COURS:
			i = new Intent(this, coursActivity.class);
			i.putExtra("coursID", ((Cours) parent.getExpandableListAdapter()
					.getChild(groupPosition, childPosition)).getId());
			startActivity(i);
			break;
		case ANNONCE:
			i = new Intent(this, detailsAnnonce.class);
			i.putExtra("annID", ((Annonce) parent.getExpandableListAdapter()
					.getChild(groupPosition, childPosition)).getId());
			i.putExtra("tab", 0);
			startActivity(i);
			break;
		case DOCUMENTS:
			i = new Intent(this, coursActivity.class);
			i.putExtra("tab", 1);
			i.putExtra("docID", ((Document) parent.getExpandableListAdapter()
					.getChild(groupPosition, childPosition)).getId());
			startActivity(i);
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		mListView = (ExpandableListView) findViewById(android.R.id.list);

		handleIntent(getIntent());
	}

	@Override
	public void onNewIntent(final Intent intent) {
		handleIntent(intent);
	}
}
