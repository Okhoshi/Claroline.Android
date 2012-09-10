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

public class DocumentsAdapter extends BaseAdapter {

	private List<Documents> listeDocuments;
	private LayoutInflater inflater;
	private Context context;
	int resource;
	
	public DocumentsAdapter(Context context, int _resource,  List<Documents> listeDocuments) {
		this.listeDocuments=listeDocuments;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
		this.resource=_resource;
	}
	
	public void setDocuments(List<Documents> listeDocuments)
	{
		this.listeDocuments=listeDocuments;
	}
	
	public int getCount()
	{
		return listeDocuments.size();
	}
	
	public Documents getItem(int position)
	{
		return listeDocuments.get(position);
	}

	public long getItemId(int position)
	{
		return listeDocuments.get(position).getId();
	}
	
	

	public Context getContext()
	{
		return this.context;
	}
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		LinearLayout newView=null;
		Documents document1   		 = getItem(position);
		String DocumentsText   		 = document1.getName()+"\n"+document1.getSize();
		
		if(view==null)
		{
			newView = new LinearLayout(getContext());
			String inflater1 = Context.LAYOUT_INFLATER_SERVICE;
			this.inflater = (LayoutInflater) getContext().getSystemService(inflater1);
			this.inflater.inflate(resource, newView, true);
			
			TextView textView =  (TextView) newView.findViewById(R.id.details_annonce_tv1); //TODO changer ici
			textView.setText(DocumentsText);
			
			//textView.setGravity(gravity);
		}
		else
		{
			newView=(LinearLayout) view;
		}
		
		
		return newView;
	}
}
