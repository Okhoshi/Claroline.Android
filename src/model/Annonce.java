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
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description here.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
@Table(name = "Annonce")
public class Annonce extends ModelBase {
	/**
	 * Content column.
	 */
	@SerializedName("content")
	@Column(name = "Content")
	private String mContent;

	/**
	 * Date column.
	 */
	@SerializedName("date")
	@Column(name = "Date")
	private DateTime mDate;

	/**
	 * IsVisible column.
	 */
	@SerializedName("visibility")
	@Column(name = "IsVisible")
	private boolean mIsVisible;

	/**
	 * List column.
	 */
	@Column(name = "List", onDelete = ForeignKeyAction.CASCADE)
	private ResourceList mList;

	/**
	 * LoadedDate column.
	 */
	@Column(name = "LoadedDate")
	private DateTime mLoadedDate;

	/**
	 * NotifiedDate column.
	 */
	@SerializedName("notifiedDate")
	@Column(name = "NotifiedDate")
	private DateTime mNotifiedDate;

	/**
	 * Rank column.
	 */
	@SerializedName("rank")
	@Column(name = "Rank")
	private int mRank;

	/**
	 * ResourceString column.
	 */
	@SerializedName("resourceId")
	@Column(name = "ResourceString")
	private String mResourceString;

	/**
	 * SeenDate column.
	 */
	@SerializedName("seenDate")
	@Column(name = "SeenDate")
	private DateTime mSeenDate;

	/**
	 * Title column.
	 */
	@SerializedName("title")
	@Column(name = "Title")
	private String mTitle;

	/**
	 * Updated column.
	 */
	@Column(name = "Updated")
	private boolean mUpdated;

	/**
	 * URL column.
	 */
	@Column(name = "URL")
	private String mURL;

	/**
	 * @return the Content
	 */
	public String getContent() {
		return mContent;
	}

	/**
	 * @return the Date
	 */
	@Override
	public DateTime getDate() {
		return mDate;
	}

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
	 * @return the NotifiedDate
	 */
	@Override
	public DateTime getNotifiedDate() {
		return mNotifiedDate;
	}

	/**
	 * @return the Rank
	 */
	public int getRank() {
		return mRank;
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
		return mNotifiedDate.isAfter(mSeenDate);
	}

	/**
	 * @param pContent
	 *            the Content to set
	 */
	public void setContent(final String pContent) {
		mContent = pContent;
	}

	/**
	 * @param pDate
	 *            the Date to set
	 */
	@Override
	public void setDate(final DateTime pDate) {
		mDate = pDate;
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
	 * @param pNotifiedDate
	 *            the NotifiedDate to set
	 */
	@Override
	public void setNotifiedDate(final DateTime pNotifiedDate) {
		mNotifiedDate = pNotifiedDate;
	}

	/**
	 * @param pRank
	 *            the Rank to set
	 */
	public void setRank(final int pRank) {
		mRank = pRank;
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

	@Override
	public void update(final JSONObject item) throws JSONException {
		setContent(item.getString("content"));
		setRank(item.getInt("rank"));
		setIsVisible(item.getBoolean("visibility"));
		setTitle(item.getString("title"));
		setDate(new DateTime(item.getString("date")));
	}
}
