/**
 * Claroline Mobile - Android
 * 
 * @package     adapter
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package adapter;

import java.util.List;
import java.util.Locale;

import model.ModelBase;
import net.claroline.mobile.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description here.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class NewsListAdapter extends BaseExpandableListAdapter {

	/**
	 * Claroline Mobile - Android
	 * 
	 * TODO Description here.
	 * 
	 * 
	 * @author Devos Quentin
	 * @version 1.0
	 */
	public static class Group<T extends ModelBase> {
		/**
		 * List.
		 */
		private List<T> mContent;

		/**
		 * Name.
		 */
		private String mName;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *            the name of the group
		 * @param list
		 *            the content
		 */
		public Group(final String name, final List<T> list) {
			this.mName = name;
			mContent = list;
		}
	}

	/**
	 * Content.
	 */
	private List<Group<?>> mGroups;

	/**
	 * Context.
	 */
	private Context mContext;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            the current context
	 * @param groups
	 *            the items to show
	 */
	public NewsListAdapter(final Context context, final List<Group<?>> groups) {
		super();
		mContext = context;
		mGroups = groups;
	}

	@Override
	public Object getChild(final int groupPosition, final int childPosition) {
		return mGroups.get(groupPosition).mContent.get(childPosition);
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return ((ModelBase) getChild(groupPosition, childPosition)).getId();
	}

	@Override
	public int getChildrenCount(final int groupPosition) {
		return mGroups.get(groupPosition).mContent.size();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			final boolean isLastChild, final View convertView,
			final ViewGroup parent) {
		View v = convertView;
		ModelBase mb = (ModelBase) getChild(groupPosition, childPosition);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.two_lines_item, parent, false);
		}

		if (mb != null) {
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail1 = (TextView) v.findViewById(R.id.detail_1);
			TextView syscode = (TextView) v.findViewById(R.id.detail_2);

			if (name != null) {
				name.setText(mb.getTitle());
			}
			if (detail1 != null) {
				detail1.setText(mb.getDate().toString("E MMM y dd",
						Locale.getDefault()));
			}
			if (syscode != null) {
				syscode.setText(mb.getList().getCours().getOfficialCode());
			}
		}
		return v;
	}

	@Override
	public Object getGroup(final int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public long getGroupId(final int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			final View convertView, final ViewGroup parent) {
		View v = convertView;
		Group<?> group = (Group<?>) getGroup(groupPosition);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.search, parent, false);
		}

		if (group != null) {
			TextView name = (TextView) v.findViewById(R.id.type_search);

			if (name != null) {
				name.setText(mContext.getString(R.string.news_last_title,
						getChildrenCount(groupPosition), group.mName));
			}
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
