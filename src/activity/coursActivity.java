package activity;

import model.Cours;
import net.claroline.mobile.android.R;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import app.AppActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import fragments.annonceListFragment;
import fragments.documentsListFragment;

public class coursActivity extends AppActivity {
	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private final FragmentActivity mActivity;
		private final String mTag;
		private final Class<T> mClass;
		private final Bundle mArgs;
		private Fragment mFragment;

		public TabListener(final FragmentActivity activity, final String tag,
				final Class<T> clz) {
			this(activity, tag, clz, null);
		}

		public TabListener(final FragmentActivity activity, final String tag,
				final Class<T> clz, final Bundle args) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mArgs = args;

			// Check to see if we already have a fragment for this tab, probably
			// from a previously saved state. If so, deactivate it, because our
			// initial state is that a tab isn't shown.
			mFragment = mActivity.getSupportFragmentManager()
					.findFragmentByTag(mTag);
			if (mFragment != null && !mFragment.isDetached()) {
				FragmentTransaction ft = mActivity.getSupportFragmentManager()
						.beginTransaction();
				ft.detach(mFragment);
				ft.commit();
			}
		}

		@Override
		public void onTabReselected(final Tab tab, final FragmentTransaction ft) {
			// Ignore :)
		}

		@Override
		public void onTabSelected(final Tab tab, final FragmentTransaction ft) {
			if (mFragment == null) {
				mFragment = Fragment.instantiate(mActivity, mClass.getName(),
						mArgs);
				ft.add(R.id.tab_content, mFragment, mTag);
			} else {
				ft.attach(mFragment);
			}
		}

		@Override
		public void onTabUnselected(final Tab tab, final FragmentTransaction ft) {
			if (mFragment != null) {
				ft.detach(mFragment);
			}
		}
	}

	/**
	 * Current course.
	 */
	private Cours mCurrentCours;

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
		int id = -1;
		int item = -1;
		if (extras != null) {
			mCurrentCours = new Select().from(Cours.class)
					.where("Id = ?", extras.getInt("coursID")).executeSingle();
			id = extras.getInt("id", -1);
			item = extras.getInt("tab", -1);
		}

		setTabs(id);

		if (mCurrentCours.isExpired()) {
			setProgressIndicator(true);
			getService().updateCompleteCourse(new AsyncHttpResponseHandler() {

			}, mCurrentCours);
		} else if (mCurrentCours.isTimeToUpdate()) {
			setProgressIndicator(true);
			getService().getUpdates(new AsyncHttpResponseHandler() {

			});
		}

		if (item > -1) {
			getSupportActionBar().setSelectedNavigationItem(item);
		}

		if (savedInstanceState != null) {
			getSupportActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt("tab", 0));
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			switch (getSupportActionBar().getSelectedNavigationIndex()) {
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
		outState.putInt("tab", getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	public void setTabs(final int id) {
		final ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			bar.setDisplayShowTitleEnabled(false);
		} else {
			bar.setDisplayShowTitleEnabled(true);
		}

		Bundle args = new Bundle();
		args.putInt("coursID", mCurrentCours.getId().intValue());

		bar.addTab(bar
				.newTab()
				.setText(getString(R.string.onglet_annonces))
				.setTag("annTab")
				.setTabListener(
						new TabListener<annonceListFragment>(this, "announce",
								annonceListFragment.class, args)));

		args.putInt("docID", id);
		bar.addTab(bar
				.newTab()
				.setText(getString(R.string.onglet_documents))
				.setTag("docTab")
				.setTabListener(
						new TabListener<documentsListFragment>(this,
								"documents", documentsListFragment.class, args)));
	}
}
