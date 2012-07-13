package model;

import java.util.Date;

public class Cours 
{
	// Listes
	
	
	
	// Variables globales : propriétés
	
	private Date isLoaded; 
	
	private EntitySet<Annonce> Annonces;
	private EntitySet<Documents> Documents;
	private EntitySet<Notification> Notifications;
	
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
	
	
	// Méthodes get
	
	public Date getIsLoaded()
	{
		return this.isLoaded;
	}
	public EntitySet<Annonce> getAnnonces()
	{
		return this.Annonces;
	}
	public EntitySet<Documents> getDocuments()
	{
		return this.Documents;
	}
	public EntitySet<Notification> getNotifications()
	{
		return this.Notifications;
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
	public void setAnnonces(EntitySet<Annonce> Annonces)
	{
		this.Annonces=Annonces;
	}
	public void setDocuments(EntitySet<Documents> Documents)
	{
		this.Documents=Documents;
	}
	public void setNotifications(EntitySet<Notification> Notifications)
	{
		this.Notifications=Notifications;
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
	
}
