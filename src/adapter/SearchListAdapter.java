package adapter;

import java.util.List;
import java.util.Map;

import mobile.claroline.R;
import model.Annonce;
import model.Cours;
import model.Documents;
import activity.coursActivity;
import activity.detailsAnnonce;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import app.GlobalApplication;

public class SearchListAdapter extends BaseAdapter {

	private List<Map<String, List<?>>> searchList;
	private Context context;
	private String query;

	public SearchListAdapter(Context context, List<Map<String,List<?>>> searchList, String query) {
		this.context=context;
		this.searchList=searchList;
		this.query = query;
	}

	@Override
	public int getCount() {
		return searchList.size();
	}

	@Override
	public Object getItem(int position) {
		return searchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return searchList.indexOf(searchList.get(position));
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View view, ViewGroup parent) {

		int type = 0;
		List<?> list = null;
		Map<String,List<?>> result = (Map<String, List<?>>) getItem(position);
		LinearLayout v = (LinearLayout) view;

		if(v==null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(R.layout.search, parent, false);
		}

		TextView mTextView = (TextView) v.findViewById(R.id.searchCounter);
		TextView mTypeTextView = (TextView) v.findViewById(R.id.type_search);
		ListView mListView = (ListView) v.findViewById(R.id.searchList);

		if((list = result.get("Cours"))!= null){
			mTypeTextView.setText(R.string.onglet_cours);
			type = 1;
		}
		else if((list = result.get("Annonce")) != null){
			mTypeTextView.setText(R.string.onglet_annonces);
			type = 2;
		}
		else if((list = result.get("Documents"))!= null){
			mTypeTextView.setText(R.string.onglet_documents);
			type = 3;
		}
		else return v;

		if (result.isEmpty()) {
			// There are no results
			mTextView.setText(context.getString(R.string.no_results,
					new Object[] { query }));
		} else {
			// Display the number of results
			int count = list.size();
			String countString = context.getResources().getQuantityString(
					R.plurals.search_results, count,
					new Object[] { count, query });
			mTextView.setText(countString);

			BaseAdapter adapter = null;
			switch(type){
			case 1:
				adapter = new CoursAdapter(context, (List<Cours>) list);
				mListView.setAdapter(adapter);
				// Define the on-click listener for the list items
				mListView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent wordIntent = new Intent(context,	coursActivity.class);
						wordIntent.putExtra("coursID", ((Cours) parent.getItemAtPosition(position)).getId());
						context.startActivity(wordIntent);
					}
				});
				break;
			case 2:
				adapter = new AnnonceAdapter(context, (List<Annonce>) list);
				mListView.setAdapter(adapter);

				// Define the on-click listener for the list items
				mListView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent wordIntent = new Intent(context,	detailsAnnonce.class);
						wordIntent.putExtra("annID", ((Annonce) parent.getItemAtPosition(position)).getId());
						context.startActivity(wordIntent);
					}
				});
				break;
			case 3:
				adapter = new DocumentsAdapter(context, (List<Documents>) list);
				mListView.setAdapter(adapter);

				// Define the on-click listener for the list items
				mListView.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent wordIntent = new Intent(context,	coursActivity.class);
						wordIntent.putExtra("coursID", ((Documents) parent.getItemAtPosition(position)).getCours().getId());
						wordIntent.putExtra("tab", "Documents");
						wordIntent.putExtra("id", ((Documents) parent.getItemAtPosition(position)).getId());
						context.startActivity(wordIntent);
					}
				});
				break;
			}
			mListView.getLayoutParams().height = count*75;
		}

		return v;
	}

}
