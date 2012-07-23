package fragments;

import java.util.ArrayList;
import java.util.List;

import mobile.claroline.R;
import model.Cours;
import model.CoursAdapter;
import activity.coursActivity;
import activity.home;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class mainCoursFragment extends ListFragment 
{
	OnItemSelectedListener mListener;
	
	// Container Activity must implement this interface
    public interface OnItemSelectedListener {
        public void onItemSelected(int position);
    }
    
    
    
    
    
    boolean mDualPane;
    int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate list with our static array of titles.
     // Normalement ca ! -->
     		//List<Cours> Liste = CoursRepository.GetAllCours();
     		// Test avec ca -->

     		
     		final List<Cours> Liste= new ArrayList<Cours>();
     		Cours Cours1= new Cours(null, null, null, null,"ives.smeers@uclouvain.be" , null, "Economie d'entreprise", "Ives Smeers");
     		Cours Cours2= new Cours(null, null, null, null, "peter.vanroy@uclouvain.be", null, "Informatique : Oz", "Peter Van Roy");
     		Cours Cours3= new Cours(null, null, null, null, "francois.remacle@uclouvain.be", null, "Mathématique Q3", "Francois Remacle");
     		Liste.add(Cours1);
     		Liste.add(Cours2);
     		Liste.add(Cours3);
            setListAdapter(new CoursAdapter(getActivity(),android.R.layout.simple_list_item_activated_1,Liste));

        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details_frag);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown, replace if needed.
            detailsCoursFragment details = (detailsCoursFragment)
                    getFragmentManager().findFragmentById(R.id.details_frag);
            if (details == null || details.getShownIndex() != index) {
                // Make new fragment to show this selection.
                details = detailsCoursFragment.newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details_frag, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), coursActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }
    
    
    
    
    
    
  
    
    
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try 
        {
            mListener = (OnItemSelectedListener) activity;
        }   catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
    
    

}
