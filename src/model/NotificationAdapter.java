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

public class NotificationAdapter {

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
