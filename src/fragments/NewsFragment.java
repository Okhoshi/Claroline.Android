/**
 * Claroline Mobile - Android
 * 
 * @package     fragments
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package fragments;

import java.util.ArrayList;
import java.util.List;

import model.Annonce;
import model.ModelBase;
import net.claroline.mobile.android.R;
import adapter.NewsListAdapter;
import adapter.NewsListAdapter.Group;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.activeandroid.query.Select;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description here.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class NewsFragment extends Fragment implements OnGroupExpandListener {

	/**
	 * 
	 */
	private static final int NEWS_LAST_NUMBERS = 5;
	/**
	 * UI References.
	 */
	private ExpandableListView mELV;
	/**
	 * Currently expanded group.
	 */
	private int mExpandedGroup;

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mELV = (ExpandableListView) getActivity().findViewById(
				R.id.news_expandable_list);

		mELV.setAdapter(new NewsListAdapter(getActivity(), getGroups()));
		mELV.setOnGroupExpandListener(this);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.news_fragment, container, false);
	}

	@Override
	public void onGroupExpand(final int groupPosition) {
		if (groupPosition != mExpandedGroup) {
			mELV.collapseGroup(mExpandedGroup);
			mExpandedGroup = groupPosition;
		}
	}

	/**
	 * @return the groups to show.
	 */
	private List<Group<?>> getGroups() {
		List<Group<?>> groups = new ArrayList<NewsListAdapter.Group<?>>();

		List<ModelBase> list = new Select().from(Annonce.class)
				.orderBy("Date DESC").limit(NEWS_LAST_NUMBERS).execute();
		groups.add(new Group<ModelBase>("Annonces", list));

		return groups;
	}
}
