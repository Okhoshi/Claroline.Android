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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Cours;
import model.ResourceList;
import net.claroline.mobile.android.R;
import util.Tools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.ViewGroup;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;
import fragments.AnnonceListFragment;
import fragments.DocumentsListFragment;
import fragments.GenericListFragment;

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
		 * Currently active fragment in ViewPager.
		 */
		private Map<String, Fragment> mActiveFragmentMap;

		/**
		 * Lists present in this course.
		 */
		private List<ResourceList> mCurrentList;

		/**
		 * Default constructor.
		 * 
		 * @param fm
		 *            the current {@link FragmentManager}
		 * @param cours
		 *            the current cours
		 */
		public ResourcesListPagerAdapter(final FragmentManager fm,
				final Cours cours) {
			super(fm);
			mCurrentList = cours.lists();
			mActiveFragmentMap = new HashMap<String, Fragment>();
		}

		@Override
		public void destroyItem(final ViewGroup container, final int position,
				final Object object) {
			super.destroyItem(container, position, object);
			mActiveFragmentMap.remove(mCurrentList.get(position).getLabel());
		}

		@Override
		public int getCount() {
			return mCurrentList.size();
		}

		/**
		 * @param label
		 *            the requested label
		 * @return the Fragment corresponding if active, null otherwise
		 */
		public Fragment getFragment(final String label) {
			return mActiveFragmentMap.get(label);
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
					return new GenericListFragment();
				}
			} else {
				return new GenericListFragment();
			}
		}

		@Override
		public Fragment getItem(final int position) {
			Fragment fragment = getFragmentForLabel(mCurrentList.get(position)
					.getLabel());
			Bundle args = new Bundle(getIntent().getExtras());
			args.putString("type", mCurrentList.get(position).getLabel());
			fragment.setArguments(args);
			mActiveFragmentMap.put(mCurrentList.get(position).getLabel(),
					fragment);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(final int position) {
			return mCurrentList.get(position).getName();
		}

		/**
		 * Refresh the tabs.
		 * 
		 * @param cours
		 *            the course data to refresh with
		 */
		public void refresh(final Cours cours) {
			mCurrentList = cours.lists();
			notifyDataSetChanged();
		}
	}

	/**
	 * {@link ViewPager} Adapter.
	 */
	private ResourcesListPagerAdapter mAdapter;
	/**
	 * Current cours.
	 */
	private Cours mCurrentCours;
	/**
	 * UI reference.
	 */
	private ViewPager mViewPager;

	@Override
	public void onBackPressed() {
		Fragment frag = mAdapter.getFragment("CLDOC");
		if (frag != null && frag instanceof DocumentsListFragment
				&& !((DocumentsListFragment) frag).isOnRoot()) {
			((DocumentsListFragment) frag).goUp();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cours_activity);

		Bundle extras = getIntent().getExtras();
		int tabIndex = -1;

		if (extras != null) {
			mCurrentCours = new Select().from(Cours.class)
					.where("Id = ?", extras.get("coursID")).executeSingle();
			tabIndex = extras.getInt("tab", -1);
		}
		if (mCurrentCours == null) {
			finish();
		}
		setTitle(String.format("[%s] %s", mCurrentCours.getOfficialCode(),
				mCurrentCours.getName()), mCurrentCours.getTitular());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new ResourcesListPagerAdapter(getSupportFragmentManager(),
				mCurrentCours);
		mViewPager.setAdapter(mAdapter);

		if (mCurrentCours.isExpired() || mCurrentCours.lists().size() == 0
				&& mustUpdate(ONCE_PER_DAY)) {
			setProgressIndicator(true);
			getService().updateCompleteCourse(mCurrentCours, this,
					new AsyncHttpResponseHandler() {
						@Override
						public void onFinish() {
							setProgressIndicator(false);
						}

						@Override
						public void onSuccess(final String response) {
							refreshUI();
							updatesNow();
						}
					});
		} else if (mCurrentCours.isTimeToUpdate()) {
			setProgressIndicator(true);
			getService().getUpdates(new AsyncHttpResponseHandler() {
				@Override
				public void onFinish() {
					setProgressIndicator(false);
				}

				@Override
				public void onSuccess(final String response) {
					if (!response.equals("[]")) {
						refreshUI();
					}
					updatesNow();
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
					getService().updateCompleteCourse(mCurrentCours, this,
							new AsyncHttpResponseHandler() {
								@Override
								public void onFinish() {
									setProgressIndicator(false);
								}

								@Override
								public void onSuccess(final String response) {
									refreshUI();
									updatesNow();
								}
							});
				} else {
					getService().getUpdates(new AsyncHttpResponseHandler() {
						@Override
						public void onFinish() {
							setProgressIndicator(false);
						}

						@Override
						public void onSuccess(final String response) {
							if (!response.equals("[]")) {
								refreshUI();
							}
							updatesNow();
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

	@Override
	public void refreshUI() {
		mAdapter.refresh(mCurrentCours);
	}
}
