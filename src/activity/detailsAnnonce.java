package activity;

import model.Annonce;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

import fragments.annonceDetailFragment;

public class detailsAnnonce extends AppActivity {

	Annonce currentAnnonce;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			currentAnnonce = new Select().from(Annonce.class)
					.where("Id = ?", extras.getInt("annID")).executeSingle();
		}

		Bundle args = new Bundle();
		args.putInt("annID", currentAnnonce.getId().intValue());
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
			if (true) { // TODO MustUpdate method in ModelBase ?
				// TODO Write getSingleResource in Service
			} else {
				mService.getUpdates(new AsyncHttpResponseHandler());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
