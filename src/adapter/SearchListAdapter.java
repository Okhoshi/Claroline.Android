package adapter;

import java.util.List;

import model.Annonce;
import model.Cours;
import model.Document;
import net.claroline.mobile.android.R;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchListAdapter extends BaseExpandableListAdapter {

	private List<SparseArray<List<?>>> searchList;
	private Context context;
	private String query;

	private static final int COURS = 0;
	private static final int ANNONCE = 1;
	private static final int DOCUMENTS = 2;

	public SearchListAdapter(final Context context,
			final List<SparseArray<List<?>>> searchList, final String query) {
		this.context = context;
		this.searchList = searchList;
		this.query = query;
	}

	@Override
	public Object getChild(final int groupPosition, final int childPosition) {
		return searchList.get(groupPosition).get(groupPosition)
				.get(childPosition);
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(final int groupPosition) {
		return searchList.get(groupPosition).get(groupPosition).size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getChildView(final int groupPosition, final int childPosition,
			final boolean isLastChild, View view, final ViewGroup parent) {
		BaseAdapter adapter = null;
		int state = View.GONE;
		String SysCode = "";

		switch (groupPosition) {
		case COURS:
			adapter = new CoursAdapter(context,
					(List<Cours>) getGroup(groupPosition));
			break;
		case ANNONCE:
			adapter = new AnnonceAdapter(context,
					(List<Annonce>) getGroup(groupPosition));
			SysCode = ((Annonce) getChild(groupPosition, childPosition))
					.getList().getCours().getOfficialCode();
			state = View.VISIBLE;
			break;
		case DOCUMENTS:
			adapter = new DocumentsAdapter(context,
					(List<Document>) getGroup(groupPosition));
			SysCode = ((Document) getChild(groupPosition, childPosition))
					.getList().getCours().getOfficialCode();
			state = View.VISIBLE;
			break;
		default:
			return view;
		}
		// Log.d("SearchList","GP : " + groupPosition + " - View : " + (view ==
		// null?"null":((TextView)
		// view.findViewById(R.id.name_item)).getText()));
		view = adapter.getView(childPosition, null, parent);
		TextView syscode = (TextView) view.findViewById(R.id.syscode);
		if (syscode != null) {
			syscode.setText(SysCode);
			syscode.setVisibility(state);
		}
		return view;
	}

	@Override
	public Object getGroup(final int groupPosition) {
		return searchList.get(groupPosition).get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return searchList.size();
	}

	@Override
	public long getGroupId(final int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			final View view, final ViewGroup parent) {

		List<?> result = (List<?>) getGroup(groupPosition);
		LinearLayout v = (LinearLayout) view;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(final int groupPosition,
			final int childPosition) {
		return true;
	}
}
