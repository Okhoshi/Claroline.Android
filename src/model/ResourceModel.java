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
import com.google.gson.annotations.SerializedName;

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
	@SerializedName("title")
	@Column(name = "Title")
	private String mTitle;

	/**
	 * ResourceString column.
	 */
	@SerializedName("resourceId")
	@Column(name = "ResourceString")
	private String mResourceString;

	/**
	 * IsVisible column.
	 */
	@SerializedName("visibility")
	@Column(name = "IsVisible")
	private boolean mIsVisible;

	/**
	 * URL column.
	 */
	@SerializedName("url")
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
	 * LoadedDate column.
	 */
	@Column(name = "LoadedDate")
	private DateTime mLoadedDate;

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
	 * @return the LoadedDate
	 */
	@Override
	public DateTime getLoadedDate() {
		return mLoadedDate;
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
	 * @param pLoadedDate
	 *            the LoadedDate to set
	 */
	@Override
	public void setLoadedDate(final DateTime pLoadedDate) {
		mLoadedDate = pLoadedDate;
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
