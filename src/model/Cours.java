/**
 * @author Dim
 * @version 1
 */
package model;

import java.util.Date;
//import java.util.Date;
import java.util.List;

import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
import dataStorage.NotificationRepository;


public class Cours 
{
	// Listes
	
	
	
	// Variables globales : propriétés
	
	private Date isLoaded;
	
	private boolean annNotif;
	private boolean dnlNotif;
	private boolean isAnn;
	private boolean isDnL;
	private boolean notified;
	private boolean Updated;
	
	private String officialEmail;
	private String sysCode;
	private String title;
	private String titular;
		
	private int Id;
	
	
	// Contructeur
	public Cours(Date isLoaded, String officialEmail, String sysCode, String title, String titular)
	{
		this.isLoaded=isLoaded;
		this.annNotif=true;
		this.dnlNotif=true;
		this.isAnn=false;
		this.isDnL=false;
		this.notified=true;
		this.Updated=true;
		this.officialEmail=officialEmail;
		this.sysCode=sysCode;
		this.title=title;
		this.titular=titular;
	}
	
	// Méthodes get
	
	public Date getIsLoaded()
	{
		return this.isLoaded;
	}
	public List<Annonce> getAnnonces()
	{
		return AnnonceRepository.GetAllAnnoncesByCoursId(Id);
	}
	public List<Documents> getDocuments()
	{
		return DocumentsRepository.GetDocListByCoursId(Id);
	}
	public List<Notification> getNotifications()
	{
		return NotificationRepository.GetAllNotificationsByCoursId(Id);
	}
	public String getOfficialEmail()
	{
		return this.officialEmail;
	}
	public String getSysCode()
	{
		return this.sysCode;
	}
	public String getTitle()
	{
		return this.title;
	}
	public String getTitular()
	{
		return this.titular;
	}
	public int getId()
	{
		return this.Id;
	}
	
	
	
	// Méthodes booleennes
	
	public boolean isAnnNotif()
	{
		return this.annNotif;
	}
	public boolean isDnlNotif()
	{
		return this.dnlNotif;
	}
	public boolean isAnn()
	{
		return this.isAnn;
	}
	public boolean isDnL()
	{
		return this.isDnL;
	}
	public boolean isNotified()
	{
		return this.notified;
	}
	public boolean isUpdated()
	{
		return this.Updated;
	}
	
	
	// Méthodes set
	
	public void setIsLoaded(Date isLoaded)
	{
		this.isLoaded=isLoaded;
	}
	public void setOfficialEmail(String officialEmail)
	{
		this.officialEmail=officialEmail;
	}
	public void setSysCode(String sysCode)
	{
		this.sysCode=sysCode;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public void setTitular(String titular)
	{
		this.titular=titular;
	}
	public void setId(int Id)
	{
		this.Id=Id;
	}
	public void setAnnNotif(boolean annNotif)
	{
		this.annNotif=annNotif;
	}
	public void setDnlNotif(boolean dnlNotif)
	{
		this.dnlNotif=dnlNotif;
	}
	public void setAnn(boolean isAnn)
	{
		this.isAnn=isAnn;
	}
	public void setDnL(boolean isDnL)
	{
		this.isDnL=isDnL;
	}
	public void setNotified(boolean notified)
	{
		this.notified=notified;
	}
	public void setUpdated(boolean Updated)
	{
		this.Updated=Updated;
	}
	
	@Override
	public boolean equals(Object o){
		if (o instanceof Cours){
			return (((Cours) o).getSysCode().equals(this.getSysCode()) && ((Cours) o).getId() == this.getId());
		}
		return false;
	}

	public int saveInDB(){
		if(this.equals(CoursRepository.GetById(this.getId()))){
			CoursRepository.Update(this);
		} else {
			CoursRepository.Save(this);
		}
		return this.getId();
	}
	
}
