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

import java.util.Arrays;
import java.util.List;

import model.Cours;
import model.ResourceList;
import net.claroline.mobile.android.R;
import util.Tools;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import app.App;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;
import fragments.AnnonceListFragment;
import fragments.DocumentsListFragment;

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
			if (Arrays.asList(
					Tools.enumValuesToStrings(SupportedModules.values()))
					.contains(label)) {
				switch (SupportedModules.valueOf(label)) {
				case CLANN:
					return new AnnonceListFragment();
				case CLDOC:
					return new DocumentsListFragment();
				default:
					return new Fragment();
				}
			} else {
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
		DocumentsListFragment frag = (DocumentsListFragment) getSupportFragmentManager()
				.findFragmentByTag("documents");
		if (frag != null && !frag.isOnRoot()) {
			frag.goUp();
		} else {
			super.onBackPressed();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
		setTitle(String.format("[%s] %s", mCurrentCours.getOfficialCode(),
				mCurrentCours.getName()));
		if (App.isNewerAPI(Build.VERSION_CODES.HONEYCOMB)) {
			getActionBar().setSubtitle(mCurrentCours.getTitular());
		}

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new ResourcesListPagerAdapter(getSupportFragmentManager());
		mAdapter.setResourceLists(mCurrentCours.lists());
		mViewPager.setAdapter(mAdapter);

		if (mCurrentCours.isExpired()) {
			setProgressIndicator(true);
			getService().updateCompleteCourse(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(final String response) {
					if (!response.equals("[]")) {
						refreshUI();
					}
				}
			}, mCurrentCours);
		} else if (mCurrentCours.isTimeToUpdate()) {
			setProgressIndicator(true);
			getService().getUpdates(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(final String response) {
					if (!response.equals("[]")) {
						refreshUI();
					}
				}
			});
		}
		if (savedInstanceState != null) {
			mViewPager
					.setCurrentItem(savedInstanceState.getInt("tab", 0), true);
		} else if (tabIndex > -1) {
			mViewPager.setCurrentItem(tabIndex, true);
		}

	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			switch (mViewPager.getCurrentItem()) {
			default:
				setProgressIndicator(true);
				if (mCurrentCours.isExpired()) {
					getService().updateCompleteCourse(
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(final String response) {
									refreshUI();
								}
							}, mCurrentCours);
				} else {
					getService().getUpdates(new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(final String response) {
							if (!response.equals("[]")) {
								refreshUI();
							}
						}
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
		outState.putInt("tab", mViewPager.getCurrentItem());
	}

	private void refreshUI() {
		int position = mViewPager.getCurrentItem();
		mAdapter.setResourceLists(mCurrentCours.lists());
		mViewPager.setCurrentItem(position, false);
	}
}
