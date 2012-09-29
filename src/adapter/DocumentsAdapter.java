/**
 * @author Dim
 * @version 1
 * 
 * @description :  An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. 
 * 				   The Adapter provides access to the data items. 
 * 			       The Adapter is also responsible for making a View for each item in the data set. 
 */
package adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import mobile.claroline.R;
import model.Annonce;
import model.Documents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DocumentsAdapter extends BaseAdapter {

	private int RESOURCE = R.layout.two_lines_details_list_item;
	private List<Documents> listeDocuments;
	private Context context;
	
	public DocumentsAdapter(Context context,  List<Documents> listeDocuments) {
		this.listeDocuments=listeDocuments;
		this.context=context;
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
	
	public View getView(final int position, View view, ViewGroup viewGroup) //TODO Refaire le layout de la row
	{
		LinearLayout v = (LinearLayout) view;
		Documents doc = getItem(position);
		
		if(v==null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(RESOURCE, viewGroup, false);
		}
		
		if(doc != null){
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail_1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail_2 = (TextView) v.findViewById(R.id.detail_2);
			
			if(name != null){
				name.setText(doc.getName());
			}
			if(detail_1 != null){
				detail_1.setText((new SimpleDateFormat("E MMM y dd")).format(doc.getDate()));
			}
			if(detail_2 != null){
				detail_2.setText(doc.getStringSize());
			}
		}
		
		return v;
	}
}
