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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotificationAdapter extends BaseAdapter {

	private List<Notification> listeNotification;
	private LayoutInflater inflater;
	private Context context;

	public NotificationAdapter(Context context, List<Notification> listeNotification) {
		this.listeNotification=listeNotification;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
	}
	
	public void setNotification(List<Notification> listeNotification)
	{
		this.listeNotification=listeNotification;
	}
	
	public int getCount()
	{
		return listeNotification.size();
	}
	
	public Object getItem(int position)
	{
		return listeNotification.get(position);
	}

	public long getItemId(int position)
	{
		return listeNotification.get(position).getId();
	}
	
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		return null;
		//TODO
	}

}
