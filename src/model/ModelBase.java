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
	 * @return the Notified Status
	 */
	public abstract boolean isNotified();

	/**
	 * @return the notifications of this resource
	 */
	public List<Notification> notifications() {
		return getMany(Notification.class, "Resource");
	}

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

}