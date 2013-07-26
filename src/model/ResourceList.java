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
import com.activeandroid.annotation.Column;
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
	@Column(name = "Cours")
	private Cours mCours;

	/**
	 * Name column.
	 */
	@SerializedName("name")
	@Column(name = "Name")
	private String mName;

	/**
	 * Label column.
	 */
	@SerializedName("label")
	@Column(name = "Label")
	private String mLabel;

	/**
	 * IsVisible column.
	 */
	@SerializedName("visibility")
	@Column(name = "IsVisible")
	private boolean mIsVisible;

	/**
	 * LoadedDate column.
	 */
	@Column(name = "LoadedDate")
	private DateTime mLoadedDate;

	/**
	 * Updated column.
	 */
	@Column(name = "Updated")
	private boolean mUpdated;

	/**
	 * ResourceType column.
	 */
	@Column(name = "ResourceType")
	private Class<? extends ModelBase> mResourceType;

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
	 * @return the List of Resources
	 */
	@SuppressWarnings("unchecked")
	public List<ModelBase> resources() {
		return (List<ModelBase>) getMany(mResourceType, "List");
	}

	/**
	 * @param pCours
	 *            the Cours to set
	 */
	public void setCours(final Cours pCours) {
		mCours = pCours;
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
}
