/**
 * Claroline Mobile - Android
 * 
 * @package     activity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
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

/**
 * Claroline Mobile - Android
 * 
 * Search Results Activity class.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class SearchableActivity extends AppActivity implements
		OnChildClickListener {

	/**
	 * ID.
	 */
	private static final int COURS = 0;
	/**
	 * ID.
	 */
	private static final int ANNONCE = 1;
	/**
	 * ID.
	 */
	private static final int DOCUMENTS = 2;

	/**
	 * UI reference.
	 */
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
				.where("Name LIKE ? OR OfficialCode LIKE ?", '%' + query + '%',
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
				.where("IsFolder = 0 AND Title LIKE ? OR Description LIKE ? OR Extension LIKE ?",
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

	/**
	 * @param intent
	 *            the {@link Intent} to handle
	 */
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
			i = new Intent(this, CoursActivity.class);
			i.putExtra("coursID", ((Cours) parent.getExpandableListAdapter()
					.getChild(groupPosition, childPosition)).getId());
			startActivity(i);
			break;
		case ANNONCE:
			i = new Intent(this, DetailsActivity.class);
			i.putExtra("resID", ((Annonce) parent.getExpandableListAdapter()
					.getChild(groupPosition, childPosition)).getId());
			i.putExtra("label", "CLANN");
			startActivity(i);
			break;
		case DOCUMENTS:
			i = new Intent(this, DetailsActivity.class);
			i.putExtra("resID", ((Document) parent.getExpandableListAdapter()
					.getChild(groupPosition, childPosition)).getId());
			i.putExtra("label", "CLDOC");
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

	@Override
	public void refreshUI() {

	}
}
