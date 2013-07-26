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

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description here.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
@Table(name = "Notification")
public class Notification extends Model {

	/**
	 * Resource column.
	 */
	@Column(name = "Resource")
	private ModelBase mResource;

	/**
	 * Date column.
	 */
	@Column(name = "Date")
	private DateTime mDate;

	/**
	 * IsOld column.
	 */
	@Column(name = "IsOld")
	private boolean mIsOld;

	/**
	 * Default constructor without arguments. Required.
	 */
	public Notification() {
		super();
	}

	/**
	 * @return the Date
	 */
	public DateTime getDate() {
		return mDate;
	}

	/**
	 * @return the IsOld
	 */
	public boolean getIsOld() {
		return mIsOld;
	}

	/**
	 * @return the Resource
	 */
	public ModelBase getResource() {
		return mResource;
	}

	/**
	 * @return the Notified Status
	 */
	public boolean isNotified() {
		return mResource.getSeenDate().isBefore(mDate);
	}

	/**
	 * @param pDate
	 *            the Date to set
	 */
	public void setDate(final DateTime pDate) {
		mDate = pDate;
	}

	/**
	 * @param pIsOld
	 *            the IsOld to set
	 */
	public void setIsOld(final boolean pIsOld) {
		mIsOld = pIsOld;
	}

	/**
	 * @param pResource
	 *            the Resource to set
	 */
	public void setResource(final ModelBase pResource) {
		mResource = pResource;
	}
}
