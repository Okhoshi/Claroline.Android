/**
 * Claroline Mobile - Android
 * 
 * @package     model
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @date        21 juil. 2013
 * @version     1.0
 *
 * @license     
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
 * Cours Model class.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
@Table(name = "Cours")
public class Cours extends Model {
	/**
	 * SysCode column.
	 */
	@SerializedName("sysCode")
	@Column(name = "SysCode")
	private String mSysCode;
	/**
	 * CoursId column.
	 */
	@SerializedName("cours_id")
	@Column(name = "CoursId")
	private int mCoursId;

	/**
	 * OfficialCode column.
	 */
	@SerializedName("officialCode")
	@Column(name = "OfficialCode")
	private String mOfficialCode;

	/**
	 * Name column.
	 */
	@SerializedName("title")
	@Column(name = "Name")
	private String mName;
	/**
	 * Titular column.
	 */
	@SerializedName("titular")
	@Column(name = "Titular")
	private String mTitular;

	/**
	 * OfficialEmail column.
	 */
	@SerializedName("officialEmail")
	@Column(name = "OfficialEmail")
	private String mOfficialEmail;

	/**
	 * LoadedDate column.
	 */
	@Column(name = "LoadedDate")
	private DateTime mLoadedDate;

	/**
	 * IsUpdated column.
	 */
	@Column(name = "IsUpdated")
	private boolean mIsUpdated;

	/**
	 * Default constructor without arguments. Required.
	 */
	public Cours() {
		super();
	}

	/**
	 * @return the CoursId
	 */
	public int getCoursId() {
		return mCoursId;
	}

	/**
	 * @return the IsUpdated
	 */
	public boolean getIsUpdated() {
		return mIsUpdated;
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
	 * @return the OfficialCode
	 */
	public String getOfficialCode() {
		return mOfficialCode;
	}

	/**
	 * @return the OfficialEmail
	 */
	public String getOfficialEmail() {
		return mOfficialEmail;
	}

	/**
	 * @return the SysCode
	 */
	public String getSysCode() {
		return mSysCode;
	}

	/**
	 * @return the Titular
	 */
	public String getTitular() {
		return mTitular;
	}

	/**
	 * @return the expiration state
	 */
	public boolean isExpired() {
		return mLoadedDate.isAfter(DateTime.now().plusWeeks(1));
	}

	/**
	 * @return the notified state
	 */
	public boolean isNotified() {
		return false;
	}

	/**
	 * @return the update state
	 */
	public boolean isTimeToUpdate() {
		return mLoadedDate.isAfter(DateTime.now().plusHours(2));
	}

	/**
	 * @return the {@link ResourceList} of this Cours
	 */
	public List<ResourceList> lists() {
		return getMany(ResourceList.class, "Cours");
	}

	/**
	 * @param pCoursId
	 *            the CoursId to set
	 */
	public void setCoursId(final int pCoursId) {
		mCoursId = pCoursId;
	}

	/**
	 * @param pIsUpdated
	 *            the IsUpdated to set
	 */
	public void setIsUpdated(final boolean pIsUpdated) {
		mIsUpdated = pIsUpdated;
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
	 * @param pOfficialCode
	 *            the OfficialCode to set
	 */
	public void setOfficialCode(final String pOfficialCode) {
		mOfficialCode = pOfficialCode;
	}

	/**
	 * @param pOfficialEmail
	 *            the OfficialEmail to set
	 */
	public void setOfficialEmail(final String pOfficialEmail) {
		mOfficialEmail = pOfficialEmail;
	}

	/**
	 * @param pSysCode
	 *            the SysCode to set
	 */
	public void setSysCode(final String pSysCode) {
		mSysCode = pSysCode;
	}

	/**
	 * @param pTitular
	 *            the Titular to set
	 */
	public void setTitular(final String pTitular) {
		mTitular = pTitular;
	}
}
