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
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ConflictAction;
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
@Table(name = "ResourceList")
public class ResourceList extends Model {

	/**
	 * Cours column.
	 */
	@Column(name = "Cours", onDelete = ForeignKeyAction.CASCADE)
	private Cours mCours;

	/**
	 * Index column.
	 */
	@Column(name = "UniqueIndex", unique = true, onUniqueConflict = ConflictAction.IGNORE)
	private String mIndex;

	/**
	 * IsVisible column.
	 */
	@SerializedName("visibility")
	@Column(name = "IsVisible")
	private boolean mIsVisible;

	/**
	 * Label column.
	 */
	@SerializedName("label")
	@Column(name = "Label")
	private String mLabel;

	/**
	 * LoadedDate column.
	 */
	@Column(name = "LoadedDate")
	private DateTime mLoadedDate;

	/**
	 * Name column.
	 */
	@SerializedName("name")
	@Column(name = "Name")
	private String mName;

	/**
	 * ResourceType column.
	 */
	@Column(name = "ResourceType")
	private Class<? extends ModelBase> mResourceType = ResourceModel.class;

	/**
	 * Updated column.
	 */
	@Column(name = "Updated")
	private boolean mUpdated;

	/**
	 * Default constructor without arguments. Required.
	 */
	public ResourceList() {
		super();
	}

	/**
	 * @return the Cours
	 */
	public Cours getCours() {
		return mCours;
	}

	/**
	 * @return the Index
	 */
	public String getIndex() {
		if (mIndex == null) {
			setIndex(getCours().getSysCode() + getLabel());
			save();
		}
		return mIndex;
	}

	/**
	 * @return the IsVisible
	 */
	public boolean getIsVisible() {
		return mIsVisible;
	}

	/**
	 * @return the Label
	 */
	public String getLabel() {
		return mLabel;
	}

	/**
	 * @return the LoadedDate
	 */
	public DateTime getLoadedDate() {
		if (mLoadedDate == null) {
			mLoadedDate = new DateTime(0L);
		}
		return mLoadedDate;
	}

	/**
	 * @return the Name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return the ResourceType
	 */
	public Class<? extends ModelBase> getResourceType() {
		return mResourceType;
	}

	/**
	 * @return the Updated
	 */
	public boolean getUpdated() {
		return mUpdated;
	}

	/**
	 * @return the expiration state
	 */
	public boolean isExpired() {
		return getLoadedDate().isBefore(DateTime.now().minusWeeks(1));
	}

	/**
	 * @return the update state
	 */
	public boolean isTimeToUpdate() {
		return getLoadedDate().isBefore(DateTime.now().minusHours(2));
	}

	/**
	 * @param <T>
	 *            the type of list
	 * @return the List of Resources
	 */
	@SuppressWarnings("unchecked")
	public <T extends ModelBase> List<T> resources() {
		return (List<T>) getMany(mResourceType, "List");
	}

	/**
	 * @param pCours
	 *            the Cours to set
	 */
	public void setCours(final Cours pCours) {
		mCours = pCours;
		setIndex(mCours.getSysCode() + getLabel());
	}

	/**
	 * @param pIndex
	 *            the Index to set
	 */
	public void setIndex(final String pIndex) {
		mIndex = pIndex;
	}

	/**
	 * @param pIsVisible
	 *            the IsVisible to set
	 */
	public void setIsVisible(final boolean pIsVisible) {
		mIsVisible = pIsVisible;
	}

	/**
	 * @param pLabel
	 *            the Label to set
	 */
	public void setLabel(final String pLabel) {
		mLabel = pLabel;
		setIndex(getCours().getSysCode() + mLabel);
	}

	/**
	 * @param pLoadedDate
	 *            the LoadedDate to set
	 */
	public void setLoadedDate(final DateTime pLoadedDate) {
		mLoadedDate = pLoadedDate;
	}

	/**
	 * @param pName
	 *            the Name to set
	 */
	public void setName(final String pName) {
		mName = pName;
	}

	/**
	 * @param pResourceType
	 *            the ResourceType to set
	 */
	public void setResourceType(final Class<? extends ModelBase> pResourceType) {
		mResourceType = pResourceType;
	}

	/**
	 * @param pUpdated
	 *            the Updated to set
	 */
	public void setUpdated(final boolean pUpdated) {
		mUpdated = pUpdated;
	}

	/**
	 * Loads the infos from JSON item.
	 * 
	 * @param item
	 *            the data to load
	 * @throws JSONException
	 *             if the data does not match the format
	 */
	public void update(final JSONObject item) throws JSONException {
		setName(item.getString("name"));
	}
}
