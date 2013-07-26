/**
 * @author Dim
 * @version 1
 * 
 * @description An Adapter object acts as a bridge between an AdapterView
 * 				and the underlying data for that view. 
 * 				The Adapter provides access to the data items. 
 * 			    The Adapter is also responsible for making a View for each item in the data set. 
 */
package adapter;

import java.util.List;

import model.Notification;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotificationAdapter extends BaseAdapter {

	private List<Notification> mNotificationList;
	private Context mContext;

	public NotificationAdapter(final Context context,
			final List<Notification> listeNotification) {
		mNotificationList = listeNotification;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mNotificationList.size();
	}

	@Override
	public Object getItem(final int position) {
		return mNotificationList.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return mNotificationList.get(position).getId();
	}

	@Override
	public View getView(final int position, final View view,
			final ViewGroup viewGroup) {

		return null;
		// TODO When the notification system will be ready
	}

	public void setNotification(final List<Notification> listeNotification) {
		mNotificationList = listeNotification;
	}

}
