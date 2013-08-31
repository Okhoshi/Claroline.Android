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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.claroline.mobile.android.R;

import org.joda.time.DateTime;

import android.os.Environment;
import android.util.Log;
import app.App;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
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
@Table(name = "Document")
public class Document extends ModelBase {

	/**
	 * Size conversion factor.
	 */
	private static final int C1024 = 1024;

	/**
	 * @param currentList
	 *            the current ResourceList
	 * @return a new empty root Document
	 */
	public static Document getEmptyRoot(final ResourceList currentList) {
		android.os.Debug.waitForDebugger();

		Document doc = new Document();
		doc.setList(currentList);
		doc.setTitle("ROOT");
		doc.setPath("/");
		doc.setIsFolder(true);
		return doc;
	}

	/**
	 * Title column.
	 */
	@SerializedName("title")
	@Column(name = "Title")
	private String mTitle;

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
	 * Path column.
	 */
	@SerializedName("path")
	@Column(name = "Path")
	private String mPath;

	/**
	 * Updated column.
	 */
	@Column(name = "Updated")
	private boolean mUpdated;

	/**
	 * SeenDate column.
	 */
	@SerializedName("seenDate")
	@Column(name = "SeenDate")
	private DateTime mSeenDate;

	/**
	 * List column.
	 */
	@Column(name = "List")
	private ResourceList mList;

	/**
	 * NotifiedDate column.
	 */
	@SerializedName("notifiedDate")
	@Column(name = "NotifiedDate")
	private DateTime mNotifiedDate;

	/**
	 * Date column.
	 */
	@SerializedName("date")
	@Column(name = "Date")
	private DateTime mDate;

	/**
	 * IsFolder column.
	 */
	@SerializedName("isFolder")
	@Column(name = "IsFolder")
	private boolean mIsFolder;

	/**
	 * Size column.
	 */
	@SerializedName("size")
	@Column(name = "Size")
	private long mSize;

	/**
	 * Extension column.
	 */
	@SerializedName("extension")
	@Column(name = "Extension")
	private String mExtension;

	/**
	 * Description column.
	 */
	@SerializedName("description")
	@Column(name = "Description")
	private String mDescription;

	/**
	 * LoadedDate column.
	 */
	@Column(name = "LoadedDate")
	private DateTime mLoadedDate;

	/**
	 * @return the content of this folder
	 */
	public List<Document> getContent() {
		List<Document> liste;
		if (mIsFolder) {
			String added = mTitle.equals("ROOT") ? "" : mTitle + "/";
			liste = new Select()
					.from(Document.class)
					.where("List = ? AND Path = ?", mList.getId(),
							mPath + added).execute();
		} else {
			liste = new ArrayList<Document>();
		}
		return liste;
	}

	/**
	 * @return the Date
	 */
	public DateTime getDate() {
		return mDate;
	}

	/**
	 * @return the Description
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @return the Extension
	 */
	public String getExtension() {
		return mExtension;
	}

	/**
	 * @return the full path
	 */
	public String getFullPath() {
		return mTitle.equals("ROOT") ? "/" : mPath + mTitle + "/";
	}

	/**
	 * @return the IsFolder
	 */
	public boolean getIsFolder() {
		return mIsFolder;
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
	public DateTime getNotifiedDate() {
		return mNotifiedDate;
	}

	/**
	 * @return the Path
	 */
	public String getPath() {
		return mPath;
	}

	/**
	 * @return the ResourceString
	 */
	@Override
	public String getResourceString() {
		return mPath;
	}

	/**
	 * @return the Root of this Document
	 */
	public Document getRoot() {
		if (mPath.equals("/")) {
			return getEmptyRoot(mList);
		} else {
			String rootPath = mPath.substring(0, mPath.length() - 1);
			String rootName = rootPath.substring(rootPath.lastIndexOf("/") + 1);
			rootPath = rootPath.substring(0, rootPath.lastIndexOf(rootName));
			return new Select()
					.from(Document.class)
					.where("IsFolder = 1 AND List = ? AND Path = ? AND Title = ?",
							mList.getId(), rootPath, rootName).executeSingle();
		}
	}

	/**
	 * @return the SeenDate
	 */
	@Override
	public DateTime getSeenDate() {
		return mSeenDate;
	}

	/**
	 * @return the Size
	 */
	public long getSize() {
		return mSize;
	}

	/**
	 * @return the String representation of the Size
	 */
	public String getStringSize() {
		double div = getSize();
		if (div < 1) {
			return "";
		}
		div /= Double.parseDouble("1E+9");
		if (div > 1) {
			return Math.round(div) + " Go";
		} else {
			div *= C1024;
			if (div > 1) {
				return Math.round(div) + " Mo";
			} else {
				div *= C1024;
				if (div > 1) {
					return Math.round(div) + " Ko";
				}
			}
		}
		return Math.round(getSize()) + " o";
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

	@Override
	public boolean isNotified() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the validity in memory of this Document. A document is valid in
	 *         memory during a week
	 */
	public boolean isOnMemory() {
		if (mLoadedDate.plusWeeks(1).isAfterNow()) {
			// Exits the function if the storage is not writable!
			if (!Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				Log.d("ClaroClient", "Missing SDCard");
				return false;
			}

			File root = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

			File file = new File(root.getAbsolutePath()
					+ "/"
					+ App.getInstance().getResources()
							.getString(R.string.app_name), getTitle() + "."
					+ getExtension());
			return file.exists() && file.canRead();
		}
		return false;
	}

	/**
	 * @param pDate
	 *            the Date to set
	 */
	public void setDate(final DateTime pDate) {
		mDate = pDate;
	}

	/**
	 * @param pDescription
	 *            the Description to set
	 */
	public void setDescription(final String pDescription) {
		mDescription = pDescription;
	}

	/**
	 * @param pExtension
	 *            the Extension to set
	 */
	public void setExtension(final String pExtension) {
		mExtension = pExtension;
	}

	/**
	 * @param pIsFolder
	 *            the IsFolder to set
	 */
	public void setIsFolder(final boolean pIsFolder) {
		mIsFolder = pIsFolder;
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
	public void setNotifiedDate(final DateTime pNotifiedDate) {
		mNotifiedDate = pNotifiedDate;
	}

	/**
	 * @param pPath
	 *            the Path to set
	 */
	public void setPath(final String pPath) {
		mPath = pPath;
	}

	/**
	 * @param pResourceString
	 *            the ResourceString to set
	 */
	@Override
	public void setResourceString(final String pResourceString) {
		mPath = pResourceString;
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
	 * @param pSize
	 *            the Size to set
	 */
	public void setSize(final long pSize) {
		mSize = pSize;
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
