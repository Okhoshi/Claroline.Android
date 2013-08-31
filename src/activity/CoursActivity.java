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

import java.util.List;

import model.Cours;
import model.ResourceList;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;
import fragments.annonceListFragment;
import fragments.documentsListFragment;

/**
 * Claroline Mobile - Android
 * 
 * Cours resources activity.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class CoursActivity extends AppActivity {

	/**
	 * Claroline Mobile - Android
	 * 
	 * ViewPager Adapter.
	 * 
	 * @author Devos Quentin
	 * @version 1.0
	 */
	public class ResourcesListPagerAdapter extends FragmentStatePagerAdapter {
		/**
		 * Lists present in this course.
		 */
		private List<ResourceList> mResourceLists;

		/**
		 * Default constructor.
		 * 
		 * @param fm
		 *            the current {@link FragmentManager}
		 */
		public ResourcesListPagerAdapter(final FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mResourceLists.size();
		}

		/**
		 * @param label
		 *            the label of the resource type to load
		 * @return the Fragment instance of the resource type to load
		 */
		private Fragment getFragmentForLabel(final String label) {
			switch (Enum.valueOf(SupportedModules.class, label)) {
			case CLANN:
				return new annonceListFragment();
			case CLDOC:
				return new documentsListFragment();
			default:
				return new Fragment();
			}
		}

		@Override
		public Fragment getItem(final int position) {
			Fragment fragment = getFragmentForLabel(mResourceLists
					.get(position).getLabel());
			// Bundle args = new Bundle();
			// args.putInt("coursID", mCurrentCours.getId().intValue());
			fragment.setArguments(getIntent().getExtras());
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(final int position) {
			return mResourceLists.get(position).getName();
		}

		/**
		 * @param lists
		 *            the new {@link ResourceList} {@link List} to set
		 */
		public void setResourceLists(final List<ResourceList> lists) {
			mResourceLists = lists;
			notifyDataSetChanged();
		}
	}

	/**
	 * Current cours.
	 */
	private Cours mCurrentCours;
	/**
	 * UI reference.
	 */
	private ViewPager mViewPager;
	/**
	 * {@link ViewPager} Adapter.
	 */
	private ResourcesListPagerAdapter mAdapter;

	@Override
	public void onBackPressed() {
		documentsListFragment frag = (documentsListFragment) getSupportFragmentManager()
				.findFragmentByTag("documents");
		if (frag != null && !frag.isOnRoot()) {
			frag.goUp();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cours_activity);

		Bundle extras = getIntent().getExtras();
		int currentDocumentId = -1;
		int tabIndex = -1;

		if (extras != null) {
			mCurrentCours = new Select().from(Cours.class)
					.where("Id = ?", extras.get("coursID")).executeSingle();
			currentDocumentId = extras.getInt("currentDocumentId", -1);
			tabIndex = extras.getInt("tab", -1);
		}
		if (mCurrentCours == null) {
			finish();
		}

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new ResourcesListPagerAdapter(getSupportFragmentManager());
		mAdapter.setResourceLists(mCurrentCours.lists());
		mViewPager.setAdapter(mAdapter);

		if (mCurrentCours.isExpired()) {
			setProgressIndicator(true);
			getService().updateCompleteCourse(new AsyncHttpResponseHandler() {
			}, mCurrentCours);
		} else if (mCurrentCours.isTimeToUpdate()) {
			setProgressIndicator(true);
			getService().getUpdates(new AsyncHttpResponseHandler() {
			});
		}
		if (savedInstanceState != null) {
			// getActionBar().setSelectedNavigationItem(
			// savedInstanceState.getInt("tab", 0));
		} else if (tabIndex > -1) {
			// getActionBar().setSelectedNavigationItem(item);
		}

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			switch (1) { // getActionBar().getSelectedNavigationIndex()) {
			default:
				setProgressIndicator(true);
				if (mCurrentCours.isExpired()) {
					getService().updateCompleteCourse(
							new AsyncHttpResponseHandler() {

							}, mCurrentCours);
				} else {
					getService().getUpdates(new AsyncHttpResponseHandler() {

					});
				}
				break;
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	public void setTabs(final int id) {
		/*
		 * final ActionBar bar = getActionBar();
		 * bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); if
		 * (getResources().getConfiguration().orientation !=
		 * Configuration.ORIENTATION_LANDSCAPE) {
		 * bar.setDisplayShowTitleEnabled(false); } else {
		 * bar.setDisplayShowTitleEnabled(true); }
		 * 
		 * Bundle args = new Bundle(); args.putInt("coursID",
		 * mCurrentCours.getId().intValue());
		 * 
		 * bar.addTab(bar .newTab()
		 * .setText(getString(R.string.onglet_annonces)) .setTag("annTab")
		 * .setTabListener( new TabListener<annonceListFragment>(this,
		 * "announce", annonceListFragment.class, args)));
		 * 
		 * args.putInt("docID", id); bar.addTab(bar .newTab()
		 * .setText(getString(R.string.onglet_documents)) .setTag("docTab")
		 * .setTabListener( new TabListener<documentsListFragment>(this,
		 * "documents", documentsListFragment.class, args)));
		 */
	}
}
