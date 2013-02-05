/**
 * @author Dim
 * @version 1
 * 
 * @description :  An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. 
 * 				   The Adapter provides access to the data items. 
 * 			       The Adapter is also responsible for making a View for each item in the data set. 
 */
package adapter;

import java.util.List;

import net.claroline.mobile.android.R;
import model.Cours;
import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoursAdapter extends BaseAdapter {

	private List<Cours> listeCours;
	private Context context;
	private int selectedItem;
	
	public CoursAdapter(Context context, List<Cours> listeCours) {
		this.context=context;
		this.listeCours=listeCours;
		selectedItem = -1;
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
	
	public void setSelection(int position){
		selectedItem = position;
		notifyDataSetChanged();
	}
	
	public int getSelection(){
		return selectedItem;
	}
	
	public View getView(final int position, View view, ViewGroup parent) 
	{
		Cours cours = getItem(position);
		LinearLayout v = (LinearLayout) view;
		
		if(v==null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(R.layout.two_lines_item, parent, false);
		}
		
		if(cours != null){
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail_1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail_2 = (TextView) v.findViewById(R.id.detail_2);
			
			if(name != null){
				name.setText(cours.getTitle());
			}
			if(detail_1 != null){
				detail_1.setText(cours.getTitular());
			}
			if(detail_2 != null){
				detail_2.setText(cours.getOfficialCode());
			}
			
			if(selectedItem != -1 && selectedItem == position){
				v.setBackgroundColor(context.getResources().getColor(color.holo_blue_dark));
			} else {
				v.setBackgroundColor(Color.TRANSPARENT);
			}
			
		}
		
		return v;
	}
	 

}
