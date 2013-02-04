package activity;

import net.claroline.mobile.android.R;
import model.Cours;
import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import app.AppActivity;
import app.GlobalApplication;
import app.TabListener;
import connectivity.AllowedOperations;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
import dataStorage.Repository;
import fragments.annonceListFragment;
import fragments.documentsListFragment;


public class coursActivity extends AppActivity
{
	protected Cours currentCours;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cours_activity);
		
		Bundle extras = getIntent().getExtras();
		int id = -1;
		int item = -1;
		if (extras != null)
	    {
	        currentCours=CoursRepository.GetById(extras.getInt("coursID"));
			id  = extras.getInt("id", -1);
			item  = extras.getInt("tab", -1);
	    }
	    
	    setTabs(id);
	    
		if(currentCours.isExpired()){
			GlobalApplication.setProgressIndicator(this, true);
			(new Thread(GlobalApplication.getClient(handler, AllowedOperations.updateCompleteCourse, currentCours))).start();
		} else if(currentCours.isTimeToUpdate()){
			GlobalApplication.setProgressIndicator(this, true);
			(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getUpdates))).start();
		}
	    
	    if(item > -1){
	        getActionBar().setSelectedNavigationItem(item);
	    }

        if (savedInstanceState != null) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_refresh:
            switch(getActionBar().getSelectedNavigationIndex()){
    		default:
            	GlobalApplication.setProgressIndicator(this, true);
    			if(currentCours.isExpired()){
    				(new Thread(GlobalApplication.getClient(handler, AllowedOperations.updateCompleteCourse, currentCours))).start();
    			} else {
        			(new Thread(GlobalApplication.getClient(handler, AllowedOperations.getUpdates))).start();
    			}
    			break;
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onBackPressed(){
    	documentsListFragment frag = (documentsListFragment) getFragmentManager().findFragmentByTag("documents");
    	if(frag != null && !frag.isOnRoot()){
			frag.refreshList.sendEmptyMessage(documentsListFragment.GOUP);
    	} else
    		super.onBackPressed();
    }
	
    public void setTabs(int id){    	
    	final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		if (getResources().getConfiguration().orientation != 
				Configuration.ORIENTATION_LANDSCAPE)
		{
			bar.setDisplayShowTitleEnabled(false);
		}
		else
		{
			bar.setDisplayShowTitleEnabled(true);
		}
        
        Bundle args = new Bundle();
        args.putInt("coursID", currentCours.getId());
        
        bar.addTab(bar.newTab().setText(getString(R.string.onglet_annonces)).setTag("annTab")
                .setTabListener(new TabListener<annonceListFragment>(this, "announce", annonceListFragment.class, R.id.tab_content, args)));
        
        args.putInt("docID", id);
        bar.addTab(bar.newTab().setText(getString(R.string.onglet_documents)).setTag("docTab")
                .setTabListener(new TabListener<documentsListFragment>(this, "documents", documentsListFragment.class, R.id.tab_content, args)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

	public void onRepositoryRefresh(String type) {
		if(type.equals(AnnonceRepository.REPO_TYPE) || type.equals(Repository.ALL)){
			annonceListFragment list = (annonceListFragment) getFragmentManager().findFragmentByTag("announce");
			if(list != null)
				list.refreshList.sendEmptyMessage(annonceListFragment.REFRESH);
		}
		if(type.equals(DocumentsRepository.REPO_TYPE) || type.equals(Repository.ALL)){
			documentsListFragment list = (documentsListFragment) getFragmentManager().findFragmentByTag("documents");
			if(list != null)
				list.refreshList.sendEmptyMessage(documentsListFragment.REFRESH);
		}
	}
}
