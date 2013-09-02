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

	private List<SparseArray<List<?>>> mSearchList;
	private Context mContext;
	private String mQuery;

	private static final int COURS = 0;
	private static final int ANNONCE = 1;
	private static final int DOCUMENTS = 2;

	public SearchListAdapter(final Context context,
			final List<SparseArray<List<?>>> searchList, final String query) {
		mContext = context;
		mSearchList = searchList;
		mQuery = query;
	}

	@Override
	public Object getChild(final int groupPosition, final int childPosition) {
		return mSearchList.get(groupPosition).get(groupPosition)
				.get(childPosition);
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(final int groupPosition) {
		return mSearchList.get(groupPosition).get(groupPosition).size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public View getChildView(final int groupPosition, final int childPosition,
			final boolean isLastChild, final View view, final ViewGroup parent) {
		BaseAdapter adapter = null;
		int state = View.GONE;
		String sysCode = "";
		View v = view;

		switch (groupPosition) {
		case COURS:
			adapter = new CoursAdapter(mContext,
					(List<Cours>) getGroup(groupPosition));
			break;
		case ANNONCE:
			adapter = new AnnonceAdapter(mContext,
					(List<Annonce>) getGroup(groupPosition));
			sysCode = ((Annonce) getChild(groupPosition, childPosition))
					.getList().getCours().getOfficialCode();
			state = View.VISIBLE;
			break;
		case DOCUMENTS:
			adapter = new DocumentsAdapter(mContext,
					(List<Document>) getGroup(groupPosition));
			sysCode = ((Document) getChild(groupPosition, childPosition))
					.getList().getCours().getOfficialCode();
			state = View.VISIBLE;
			break;
		default:
			return view;
		}

		v = adapter.getView(childPosition, null, parent);
		TextView syscode = (TextView) v.findViewById(R.id.syscode);
		if (syscode != null) {
			syscode.setText(sysCode);
			syscode.setVisibility(state);
		}
		return v;
	}

	@Override
	public Object getGroup(final int groupPosition) {
		return mSearchList.get(groupPosition).get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mSearchList.size();
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
			LayoutInflater inflater = (LayoutInflater) mContext
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
			mTextView.setText(mContext.getString(R.string.no_results,
					new Object[] { mQuery }));
		} else {
			// Display the number of results
			int count = getChildrenCount(groupPosition);
			String countString = mContext.getResources().getQuantityString(
					R.plurals.search_results, count,
					new Object[] { count, mQuery });
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
