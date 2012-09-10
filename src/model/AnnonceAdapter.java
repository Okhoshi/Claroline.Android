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
import android.widget.LinearLayout;
import android.widget.TextView;


public class AnnonceAdapter extends BaseAdapter {
	
	private List<Annonce> listeAnnonce;
	private LayoutInflater inflater;
	private Context context;
	private int resource;

	public AnnonceAdapter(Context context, int _resource, List<Annonce> listeAnnonce) {
		this.listeAnnonce=listeAnnonce;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.resource=_resource;
	}
	
	public void setAnnonce(List<Annonce> listeAnnonce)
	{
		this.listeAnnonce=listeAnnonce;
	}
	
	
	public int getCount()
	{
		return listeAnnonce.size();
	}
	
	public Annonce getItem(int position)
	{
		return listeAnnonce.get(position);
	}

	public long getItemId(int position)
	{
		return listeAnnonce.get(position).getId();
	}
	
	public Context getContext()
	{
		return this.context;
	}
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		LinearLayout newView=null;
		Annonce annonce1   		 = getItem(position);
		String AnnonceText   		 = annonce1.getTitle()+"\n"+annonce1.getDate();
		
		if(view==null)
		{
			newView = new LinearLayout(getContext());
			String inflater1 = Context.LAYOUT_INFLATER_SERVICE;
			this.inflater = (LayoutInflater) getContext().getSystemService(inflater1);
			this.inflater.inflate(resource, newView, true);
			
			TextView textView =  (TextView) newView.findViewById(R.id.details_annonce_tv1);
			textView.setText(AnnonceText);
			
			//textView.setGravity(gravity);
		}
		else
		{
			newView=(LinearLayout) view;
		}
		
		
		return newView;
	}
}
