/**
 * @author Dim
 * @version 1
 */
package model;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CoursAdapter {

	private List<Cours> listeCours;
	private LayoutInflater inflater;
	private Context context;

	public CoursAdapter(Context context, List<Cours> listeCours) {
		this.listeCours=listeCours;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
	}
	
	public void setCours(List<Cours> listeCours)
	{
		this.listeCours=listeCours;
	}
	
	public int getCount()
	{
		return listeCours.size();
	}
	
	public Object getItem(int position)
	{
		return listeCours.get(position);
	}

	public long getItemId(int position)
	{
		return listeCours.get(position).getId();
	}
	
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		return null;
		//TODO
	}

}
