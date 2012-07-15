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


public class AnnonceAdapter {
	
	private List<Annonce> listeAnnonce;
	private LayoutInflater inflater;
	private Context context;

	public AnnonceAdapter(Context context, List<Annonce> listeAnnonce) {
		this.listeAnnonce=listeAnnonce;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
	}
	
	public void setAnnonce(List<Annonce> listeAnnonce)
	{
		this.listeAnnonce=listeAnnonce;
	}
	
	public int getCount()
	{
		return listeAnnonce.size();
	}
	
	public Object getItem(int position)
	{
		return listeAnnonce.get(position);
	}

	public long getItemId(int position)
	{
		return listeAnnonce.get(position).getId();
	}
	
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		return null;
		//TODO
	}
		
	
	
}
