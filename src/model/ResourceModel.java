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

import com.activeandroid.annotation.Column;

/**
 * Claroline Mobile - Android
 * 
 * Generic resource model class.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class ResourceModel extends ModelBase {
	/**
	 * Title column.
	 */
	@Column(name = "Title")
	private String mTitle;

	/**
	 * ResourceString column.
	 */
	@Column(name = "ResourceString")
	private String mResourceString;

	/**
	 * IsVisible column.
	 */
	@Column(name = "IsVisible")
	private boolean mIsVisible;

	/**
	 * URL column.
	 */
	@Column(name = "URL")
	private String mURL;

	/**
	 * Updated column.
	 */
	@Column(name = "Updated")
	private boolean mUpdated;

	/**
	 * List column.
	 */
	@Column(name = "List")
	private ResourceList mList;

	/**
	 * SeenDate column.
	 */
	@Column(name = "SeenDate")
	private DateTime mSeenDate;

	/**
	 * @return the IsVisible
	 */
	@Override
	public boolean getIsVisible() {
		return mIsVisible;
	}

	/**
	 * @return the List
	 */
	@Override
	public ResourceList getList() {
		return mList;
	}

	/**
	 * @return the ResourceString
	 */
	@Override
	public String getResourceString() {
		return mResourceString;
	}

	/**
	 * @return the SeenDate
	 */
	@Override
	public DateTime getSeenDate() {
		return mSeenDate;
	}

	/**
	 * @return the Title
	 */
	@Override
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @return the Updated
	 */
	@Override
	public boolean getUpdated() {
		return mUpdated;
	}

	/**
	 * @return the URL
	 */
	@Override
	public String getURL() {
		return mURL;
	}

	/**
	 * @return is this resource is notified
	 */
	@Override
	public boolean isNotified() {
		return false;
	}

	/**
	 * @param pIsVisible
	 *            the IsVisible to set
	 */
	@Override
	public void setIsVisible(final boolean pIsVisible) {
		mIsVisible = pIsVisible;
	}

	/**
	 * @param pList
	 *            the List to set
	 */
	@Override
	public void setList(final ResourceList pList) {
		mList = pList;
	}

	/**
	 * @param pResourceString
	 *            the ResourceString to set
	 */
	@Override
	public void setResourceString(final String pResourceString) {
		mResourceString = pResourceString;
	}

	/**
	 * @param pSeenDate
	 *            the SeenDate to set
	 */
	@Override
	public void setSeenDate(final DateTime pSeenDate) {
		mSeenDate = pSeenDate;
	}

	/**
	 * @param pTitle
	 *            the Title to set
	 */
	@Override
	public void setTitle(final String pTitle) {
		mTitle = pTitle;
	}

	/**
	 * @param pUpdated
	 *            the Updated to set
	 */
	@Override
	public void setUpdated(final boolean pUpdated) {
		mUpdated = pUpdated;
	}

	/**
	 * @param pURL
	 *            the URL to set
	 */
	@Override
	public void setURL(final String pURL) {
		mURL = pURL;
	}

}
