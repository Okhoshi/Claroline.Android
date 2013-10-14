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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Cours;
import model.ResourceList;
import net.claroline.mobile.android.R;
import util.Refreshable;
import util.Tools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.App;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;

/**
 * Claroline Mobile - Android
 * 
 * Cours Tools ViewPager fragment.
 * 
 * @author Devos Quentin
 * @version 1.0
 * 
 */
public class ToolViewPagerFragment extends Fragment {
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

		private Bundle mExtras;

		/**
		 * Default constructor.
		 * 
		 * @param fm
		 *            the current {@link FragmentManager}
		 * @param cours
		 *            the current cours
		 */
		public ResourcesListPagerAdapter(final FragmentManager fm,
				final Cours cours, final Bundle extras) {
			super(fm);
			mExtras = extras;
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

		@Override
		public Fragment getItem(final int position) {
			Fragment fragment = getFragmentForLabel(mCurrentList.get(position)
					.getLabel());
			Bundle args = new Bundle(mExtras);
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

	public boolean onBackPressed() {
		Fragment frag = mAdapter.getFragment("CLDOC");
		if (frag != null && frag instanceof DocumentsListFragment
				&& !((DocumentsListFragment) frag).isOnRoot()) {
			((DocumentsListFragment) frag).goUp();
			return false;
		} else {
			return true;
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.cours_fragment, container, false);
		final AppActivity a = (AppActivity) getActivity();

		Bundle extras = getArguments();
		int tabIndex = -1;

		if (extras != null) {
			mCurrentCours = new Select().from(Cours.class)
					.where("Id = ?", extras.get("coursID")).executeSingle();
			tabIndex = extras.getInt("tab", -1);
		}
		if (mCurrentCours == null && !App.isTwoPane()) {
			a.finish();
		}
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		mAdapter = new ResourcesListPagerAdapter(getChildFragmentManager(),
				mCurrentCours, getArguments());
		mViewPager.setAdapter(mAdapter);

		if (mCurrentCours.isExpired() || mCurrentCours.lists().size() == 0
				&& a.mustUpdate(AppActivity.ONCE_PER_DAY)) {
			a.setProgressIndicator(true);
			a.getService().updateCompleteCourse(mCurrentCours, a,
					new AsyncHttpResponseHandler() {
						@Override
						public void onFinish() {
							a.setProgressIndicator(false);
						}

						@Override
						public void onSuccess(final String response) {
							refreshUI();
							a.updatesNow();
						}
					});
		} else if (mCurrentCours.isTimeToUpdate()) {
			a.setProgressIndicator(true);
			a.getService().getUpdates(new AsyncHttpResponseHandler() {
				@Override
				public void onFinish() {
					a.setProgressIndicator(false);
				}

				@Override
				public void onSuccess(final String response) {
					if (!response.equals("[]")) {
						refreshUI();
					}
					a.updatesNow();
				}
			});
		}
		if (savedInstanceState != null) {
			mViewPager
					.setCurrentItem(savedInstanceState.getInt("tab", 0), true);
		} else if (tabIndex > -1) {
			mViewPager.setCurrentItem(tabIndex, true);
		}

		return v;
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", mViewPager.getCurrentItem());
	}

	public void refresh() {
		final AppActivity a = (AppActivity) getActivity();
		Refreshable item = (Refreshable) mAdapter.getItem(mViewPager
				.getCurrentItem());
		if (item != null) {
			item.refresh();
		} else {
			a.setProgressIndicator(true);
			if (mCurrentCours.isExpired()) {
				a.getService().updateCompleteCourse(mCurrentCours, a,
						new AsyncHttpResponseHandler() {
							@Override
							public void onFinish() {
								a.setProgressIndicator(false);
							}

							@Override
							public void onSuccess(final String response) {
								refreshUI();
								a.updatesNow();
							}
						});
			} else {
				a.getService().getUpdates(new AsyncHttpResponseHandler() {
					@Override
					public void onFinish() {
						a.setProgressIndicator(false);
					}

					@Override
					public void onSuccess(final String response) {
						if (!response.equals("[]")) {
							refreshUI();
						}
						a.updatesNow();
					}
				});

			}
		}
	}

	public void refreshUI() {
		mAdapter.refresh(mCurrentCours);
	}

	public void setCurrentCours(final Cours cours) {
		mCurrentCours = cours;
	}
}
