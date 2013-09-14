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

import model.Cours;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import app.AppActivity;

import com.activeandroid.query.Select;

import fragments.ToolViewPagerFragment;

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
	 * Current cours.
	 */
	private Cours mCurrentCours;

	/**
	 * Fragment.
	 */
	private ToolViewPagerFragment mFragment;

	@Override
	public void onBackPressed() {
		if (mFragment.onBackPressed()) {
			super.onBackPressed();
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			mCurrentCours = new Select().from(Cours.class)
					.where("Id = ?", extras.get("coursID")).executeSingle();
		}
		if (mCurrentCours == null) {
			finish();
		}
		setTitle(String.format("[%s] %s", mCurrentCours.getOfficialCode(),
				mCurrentCours.getName()), mCurrentCours.getTitular());

		mFragment = new ToolViewPagerFragment();
		mFragment.setArguments(extras);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(android.R.id.content, mFragment);
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			mFragment.refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void refreshUI() {
		mFragment.refreshUI();
	}
}
