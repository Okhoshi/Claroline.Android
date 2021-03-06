/**
 * Claroline Mobile - Android
 * 
 * @package     model
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package model;

import java.util.List;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description here.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public abstract class ModelBase extends Model {

	/**
	 * Default constructor without arguments. Required.
	 */
	public ModelBase() {
		super();
	}

	/**
	 * @return the Date
	 */
	public abstract DateTime getDate();

	/**
	 * @return the IsVisible
	 */
	public abstract boolean getIsVisible();

	/**
	 * @return the List
	 */
	public abstract ResourceList getList();

	/**
	 * @return the last loading date
	 */
	public abstract DateTime getLoadedDate();

	/**
	 * @return the NotifiedDate
	 */
	public abstract DateTime getNotifiedDate();

	/**
	 * @return the ResourceString
	 */
	public abstract String getResourceString();

	/**
	 * @return the SeenDate
	 */
	public abstract DateTime getSeenDate();

	/**
	 * @return the Title
	 */
	public abstract String getTitle();

	/**
	 * @return the Updated
	 */
	public abstract boolean getUpdated();

	/**
	 * @return the URL
	 */
	public abstract String getURL();

	/**
	 * @return the expiration state
	 */
	public boolean isExpired() {
		return getLoadedDate().isAfter(DateTime.now().plusWeeks(1));
	}

	/**
	 * @return the Notified Status
	 */
	public abstract boolean isNotified();

	/**
	 * @return the update state
	 */
	public boolean isTimeToUpdate() {
		return getLoadedDate().isAfter(DateTime.now().plusHours(2));
	}

	/**
	 * @return the notifications of this resource
	 */
	public List<Notification> notifications() {
		return getMany(Notification.class, "Resource");
	}

	/**
	 * @param pDate
	 *            the Date to set
	 */
	public abstract void setDate(final DateTime pDate);

	/**
	 * @param pIsVisible
	 *            the IsVisible to set
	 */
	public abstract void setIsVisible(final boolean pIsVisible);

	/**
	 * @param pList
	 *            the List to set
	 */
	public abstract void setList(final ResourceList pList);

	/**
	 * @param date
	 *            the last loading date to set
	 */
	public abstract void setLoadedDate(DateTime date);

	/**
	 * @param pNotifiedDate
	 *            the NotifiedDate to set
	 */
	public abstract void setNotifiedDate(final DateTime pNotifiedDate);

	/**
	 * @param pResourceString
	 *            the ResourceString to set
	 */
	public abstract void setResourceString(final String pResourceString);

	/**
	 * @param pSeenDate
	 *            the SeenDate to set
	 */
	public abstract void setSeenDate(final DateTime pSeenDate);

	/**
	 * @param pTitle
	 *            the Title to set
	 */
	public abstract void setTitle(final String pTitle);

	/**
	 * @param pUpdated
	 *            the Updated to set
	 */
	public abstract void setUpdated(final boolean pUpdated);

	/**
	 * @param pURL
	 *            the URL to set
	 */
	public abstract void setURL(final String pURL);

	/**
	 * Loads the infos from JSON item.
	 * 
	 * @param item
	 *            the data to load
	 * @throws JSONException
	 *             if the data does not match the format
	 */
	public abstract void update(JSONObject item) throws JSONException;

}