package activity;

import mobile.claroline.R;
import model.Cours;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
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
	    if (extras != null)
	    {
	        currentCours=CoursRepository.GetById(extras.getInt("coursID"));		
	    }
	    
	    setTabs();

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
    			(new Thread(GlobalApplication.getClient().makeOperation(handler, AllowedOperations.updateCompleteCourse, currentCours))).start();
    			break;
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
    public void setTabs(){    	
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
        
        bar.addTab(bar.newTab().setText(getString(R.string.onglet_annonces))
                .setTabListener(new TabListener<annonceListFragment>(this, "announce", annonceListFragment.class, args)));
        bar.addTab(bar.newTab().setText(getString(R.string.onglet_documents))
                .setTabListener(new TabListener<documentsListFragment>(this, "documents", documentsListFragment.class, args)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

	public void onRepositoryRefresh(String type) {
		if(type.equals(AnnonceRepository.REPO_TYPE)){
			annonceListFragment list = (annonceListFragment) getFragmentManager().findFragmentByTag("announce");
			if(list != null)
				list.refreshList.sendEmptyMessage(0);
		} else if(type.equals(DocumentsRepository.REPO_TYPE)){
			documentsListFragment list = (documentsListFragment) getFragmentManager().findFragmentByTag("documents");
			if(list != null)
				list.refreshList.sendEmptyMessage(0);
		}
	}
	
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	if (mFragment == null) {
        		mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
        		ft.add(R.id.tab_content, mFragment, mTag);
        	} else {
        		ft.attach(mFragment);
        	}
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        	// Ignore :)
        }
    }
}
