package activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import dataStorage.AnnonceRepository;
import fragments.annonceDetailFragment;
import model.Annonce;
import net.claroline.mobile.android.R;

public class detailsAnnonce extends AppActivity
{

	Annonce currentAnnonce;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
	    if (extras != null)
	    {
	        currentAnnonce = AnnonceRepository.GetById(extras.getInt("annID"));		
	    }
	    
	    Bundle args = new Bundle();
	    args.putInt("annID", currentAnnonce.getId());
	    annonceDetailFragment frag = (annonceDetailFragment) Fragment.instantiate(this, annonceDetailFragment.class.getName(), args);
	    getFragmentManager().beginTransaction().add(android.R.id.content, frag, "annonce_detail").commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			GlobalApplication.setProgressIndicator(this, true);
			if(currentAnnonce.isExpired()){
				(new Thread(GlobalApplication.getClient(handler,AllowedOperations.getSingleAnnounce, currentAnnonce.getCours(), currentAnnonce.getResourceId()))).start();
			} else {
				(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getUpdates))).start();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onRepositoryRefresh(String type) {
		if(type.equals(AnnonceRepository.REPO_TYPE)){
			annonceDetailFragment frag = (annonceDetailFragment) getFragmentManager().findFragmentByTag("annonce_detail");
			if(frag != null)
				frag.refreshList.sendEmptyMessage(0);
		}
	}
}
