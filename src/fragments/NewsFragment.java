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

import model.ModelBase;
import model.ResourceList;
import net.claroline.mobile.android.R;
import adapter.NewsListAdapter;
import adapter.NewsListAdapter.Group;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import app.App;

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
public class NewsFragment extends Fragment implements OnGroupExpandListener,
		OnChildClickListener {

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
		mELV.setOnChildClickListener(this);
	}

	@Override
	public boolean onChildClick(final ExpandableListView parent, final View v,
			final int groupPosition, final int childPosition, final long id) {
		ModelBase item = (ModelBase) mELV.getExpandableListAdapter().getChild(
				groupPosition, childPosition);
		if (App.isTwoPane() && item.getList().getLabel() == "CLANN") {
			Bundle data = new Bundle();
			data.putLong("resID", item.getId());
			data.putString("label", item.getList().getLabel());

			DetailFragment f = new AnnonceDetailFragment();
			f.setArguments(data);
			f.show(getActivity().getSupportFragmentManager(), "dialog");
		} else {
			Intent intent = new Intent(getActivity(),
					activity.DetailsActivity.class);
			intent.putExtra("resID", item.getId());
			intent.putExtra("label", item.getList().getLabel());
			startActivity(intent);
		}
		return true;
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

		List<ResourceList> l = new Select().distinct().from(ResourceList.class)
				.groupBy("Label").execute();
		for (ResourceList resourceList : l) {
			List<ModelBase> list = new Select("T.*")
					.from(resourceList.getResourceType()).as("T")
					.join(ResourceList.class).as("L").on("T.List == L.Id")
					.where("L.Label == ?", resourceList.getLabel())
					.orderBy("Date DESC").limit(NEWS_LAST_NUMBERS).execute();
			if (list.size() > 0) {
				groups.add(new Group<ModelBase>(resourceList.getName(), list));
			}
		}

		return groups;
	}
}
