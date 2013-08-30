package activity;

import model.Annonce;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import app.AppActivity;

import com.actionbarsherlock.view.MenuItem;
import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import fragments.annonceDetailFragment;

public class DetailsAnnonce extends AppActivity {

	private Annonce mCurrentAnnonce;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mCurrentAnnonce = new Select().from(Annonce.class)
					.where("Id = ?", extras.getInt("annID")).executeSingle();
		}

		Bundle args = new Bundle();
		args.putInt("annID", mCurrentAnnonce.getId().intValue());
		annonceDetailFragment frag = (annonceDetailFragment) Fragment
				.instantiate(this, annonceDetailFragment.class.getName(), args);
		getSupportFragmentManager().beginTransaction()
				.add(android.R.id.content, frag, "annonce_detail").commit();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			setProgressIndicator(true);
			if (mCurrentAnnonce.getLoadedDate().plusWeeks(1).isBeforeNow()) {
				getService().getSingleResource(
						mCurrentAnnonce.getList().getCours(),
						mCurrentAnnonce.getList(),
						mCurrentAnnonce.getResourceString(),
						new AsyncHttpResponseHandler() {

						});
			} else {
				getService().getUpdates(new AsyncHttpResponseHandler() {

				});
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
