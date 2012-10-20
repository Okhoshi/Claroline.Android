package adapter;

import java.util.List;

import mobile.claroline.R;
import model.Annonce;
import model.Cours;
import model.Documents;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchListAdapter extends BaseExpandableListAdapter{

	private List<SparseArray< List<?>>> searchList;
	private Context context;
	private String query;

	private static final int COURS = 0;
	private static final int ANNONCE = 1;
	private static final int DOCUMENTS = 2;

	public SearchListAdapter(Context context, List<SparseArray<List<?>>> searchList, String query) {
		this.context=context;
		this.searchList=searchList;
		this.query = query;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return searchList.get(groupPosition).get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@SuppressWarnings("unchecked")
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		BaseAdapter adapter = null;
		int state = View.GONE;
		String SysCode = "";
		
		switch (groupPosition) {
		case COURS:
			adapter = new CoursAdapter(context, (List<Cours>) getGroup(groupPosition));
			break;
		case ANNONCE:
			adapter = new AnnonceAdapter(context, (List<Annonce>) getGroup(groupPosition));
			SysCode = ((Annonce) getChild(groupPosition, childPosition)).getCours().getSysCode();
			state = View.VISIBLE;
			break;
		case DOCUMENTS:
			adapter = new DocumentsAdapter(context, (List<Documents>) getGroup(groupPosition));
			SysCode = ((Documents) getChild(groupPosition, childPosition)).getCours().getSysCode();
			state = View.VISIBLE;
			break;
		default:
			return view;
		}
		//Log.d("SearchList","GP : " + groupPosition + " - View : " + (view == null?"null":((TextView) view.findViewById(R.id.name_item)).getText()));
		view = adapter.getView(childPosition, null, parent);
		TextView syscode = (TextView) view.findViewById(R.id.syscode);
		if(syscode != null){
			syscode.setText(SysCode);
			syscode.setVisibility(state);
		}
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		return searchList.get(groupPosition).get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return searchList.get(groupPosition).get(groupPosition);
	}

	public int getGroupCount() {
		return searchList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,	View view, ViewGroup parent) {

		List<?> result = (List<?>) getGroup(groupPosition);
		LinearLayout v = (LinearLayout) view;

		if(v==null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(R.layout.search, parent, false);
		}

		TextView mTextView = (TextView) v.findViewById(R.id.searchCounter);
		TextView mTypeTextView = (TextView) v.findViewById(R.id.type_search);

		switch (groupPosition) {
		case COURS:			
			mTypeTextView.setText(R.string.onglet_cours);
			break;
		case ANNONCE:
			mTypeTextView.setText(R.string.onglet_annonces);
			break;
		case DOCUMENTS:
			mTypeTextView.setText(R.string.onglet_documents);
			break;
		default:
			return v;
		}

		if (result.isEmpty()) {
			// There are no results
			mTextView.setText(context.getString(R.string.no_results,
					new Object[] { query }));
		} else {
			// Display the number of results
			int count = getChildrenCount(groupPosition);
			String countString = context.getResources().getQuantityString(
					R.plurals.search_results, count,
					new Object[] { count, query });
			mTextView.setText(countString);
		}

		return v;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
