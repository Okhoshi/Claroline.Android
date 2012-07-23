package fragments;

import mobile.claroline.R;
import model.Cours;
import activity.coursActivity;
import activity.home;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class detailsCoursFragment extends Fragment 
{
	
	// Permit to update the data of a certain course with this reference
			private Cours currentCours = home.currentCours;
		

					
			@Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		            Bundle savedInstanceState) {
		        if (container == null) {
		            // We have different layouts, and in one of them this
		            // fragment's containing frame doesn't exist.  The fragment
		            // may still be created from its saved state, but there is
		            // no reason to try to create its view hierarchy because it
		            // won't be displayed.  Note this is not needed -- we could
		            // just run the code below, where we would create and return
		            // the view hierarchy; it would just never be used.
		            return null;
		        }
		        
		        TabHost tabHost=(TabHost) getActivity().findViewById(R.id.tabHost);
				tabHost.setup();

				TabSpec spec1=tabHost.newTabSpec(getString(R.string.onglet_annonces));
				spec1.setContent(R.id.tab1);
				spec1.setIndicator(getString(R.string.onglet_annonces));


				TabSpec spec2=tabHost.newTabSpec(getString(R.string.onglet_documents));
				spec2.setIndicator(getString(R.string.onglet_documents));
				spec2.setContent(R.id.tab2);

				tabHost.addTab(spec1);
				tabHost.addTab(spec2);
		        
				return tabHost;
			}
		   

	public void updateContent(int position) 
	{
		Intent intent = new Intent(null, coursActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
		
	}



	/**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static detailsCoursFragment newInstance(int index) {
        detailsCoursFragment f = new detailsCoursFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
}
