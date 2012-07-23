/**
 * @author Dim
 * @version 1
 * 
 * @description :  An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. 
 * 				   The Adapter provides access to the data items. 
 * 			       The Adapter is also responsible for making a View for each item in the data set. 
 */
package model;

import java.util.List;

import mobile.claroline.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoursAdapter extends BaseAdapter {

	private List<Cours> listeCours;
	private LayoutInflater inflater;
	private Context context;
	private int resource;
	
	

	public CoursAdapter(Context context, int _resource, List<Cours> listeCours) {
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.listeCours=listeCours;
		this.resource=_resource;
	}
	
	public void setCours(List<Cours> listeCours)
	{
		this.listeCours=listeCours;
	}
	
	public int getCount()
	{
		return listeCours.size();
	}
	
	public Cours getItem(int position)
	{
		return listeCours.get(position);
	}

	public long getItemId(int position)
	{
		return listeCours.get(position).getId();
	}
	
	public Context getContext()
	{
		return this.context;
	}
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		LinearLayout newView=null;
		Cours Cours1   		 = getItem(position);
		String CoursText   		 = Cours1.getTitle()+"\n"+Cours1.getTitular();
		
		if(view==null)
		{
			newView = new LinearLayout(getContext());
			String inflater1 = Context.LAYOUT_INFLATER_SERVICE;
			this.inflater = (LayoutInflater) getContext().getSystemService(inflater1);
			this.inflater.inflate(resource, newView, true);
			
			TextView textView =  (TextView) newView.findViewById(R.id.grid_item_label);
			textView.setText(CoursText);
			//textView.setGravity(gravity);
		}
		else
		{
			newView=(LinearLayout) view;
		}
		
		
		return newView;
	}
	 

}
