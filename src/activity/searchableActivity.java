package activity;

import dataStorage.CoursProvider;
import dataStorage.DBOpenHelper;
import mobile.claroline.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//@SuppressLint("ParserError")
public class searchableActivity extends Activity implements OnClickListener {

	private TextView mTextView;
	private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		// permet de retourner sur la vue précédente
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		mTextView = (TextView) findViewById(R.id.searchCounter);
		mListView = (ListView) findViewById(R.id.searchList);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar, menu);
		// Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setSubmitButtonEnabled(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			// Comportement du bouton "A Propos"
			Intent monIntent = new Intent(this, about_us.class);
			startActivity(monIntent);
			return true;
		case R.id.menu_help:
			// Comportement du bouton "Aide"
			return true;
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			return true;
		case R.id.menu_search:
			// Comportement du bouton "Recherche"
			onSearchRequested();
			return true;
		case R.id.menu_settings:
			Intent settings_intent = new Intent(this, Settings.class);
			startActivity(settings_intent);
			return true;
		case android.R.id.home:
			// Comportement du bouton qui permet de retourner a l'activite
			// Home
			monIntent = new Intent(this, home.class);
			startActivity(monIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	/**
	 * Searches the cours_table and displays results for the given query.
	 * 
	 * @param query
	 *            The search query
	 */
	public void doMySearch(String query) {

		// Cursor cursor = Repository.maBDD.query(DBOpenHelper.COURS_TABLE, new
		// String[] {"_id", "title", "title_raw"},
		// "title_raw like " + "'%Smith%'", null, null, null, null);

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(CoursProvider.CONTENT_URI, null, null,
				new String[] { query }, null);

		if (cursor == null) {
			// There are no results
			mTextView.setText(getString(R.string.no_results,
					new Object[] { query }));
		} else {
			// Display the number of results
			int count = cursor.getCount();
			String countString = getResources().getQuantityString(
					R.plurals.search_results, count,
					new Object[] { count, query });
			mTextView.setText(countString);

			// Specify the columns we want to display in the result
			String[] from = new String[] { DBOpenHelper.COURS_COLUMN_TITLE,
					DBOpenHelper.COURS_COLUMN_TITULAR };

			// Specify the corresponding layout elements where we want the
			// columns to go
			int[] to = new int[] { R.id.theTitle, R.id.theTitular };

			// Create a simple cursor adapter for the definitions and apply them
			// to the ListView
			@SuppressWarnings("deprecation")
			SimpleCursorAdapter words = new SimpleCursorAdapter(this,
					R.layout.result, cursor, from, to);
			mListView.setAdapter(words);

			// Define the on-click listener for the list items
			mListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Build the Intent used to open coursActivity with a
					// specific word Uri
					Intent wordIntent = new Intent(getApplicationContext(),
							coursActivity.class);
					Uri data = Uri.withAppendedPath(CoursProvider.CONTENT_URI,
							String.valueOf(id));
					wordIntent.setData(data);
					startActivity(wordIntent);
				}
			});
		}

	}

}
