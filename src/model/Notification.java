/**
 * @author Dim
 * @version 1
 */
package model;

import dataStorage.NotificationRepository;

import java.util.Date;

//import java.util.Date;

public class Notification 
{
	// Enumeration
	
	//private enum ValidTypes
	//{
	//	DOCUMENTS,ANNONCES;
	//}
	
	// Variables globales : propri�t�s
	
	private Cours Cours;
	private Date date;
	private int ressourceType;
	
	private boolean isOldRessource;
	private boolean notified;
	private boolean Updated;
	
	private int Id;
	private int ressourceId;
	
	
	// Constructeur
	public Notification(Cours Cours, Date date, int ressourceType)
	{
		this.Cours=Cours;
		this.date=date;
		this.ressourceType=ressourceType;
		this.isOldRessource=false;
		this.notified=true;
		this.Updated=true;
	}

	// M�thodes get
	
	public Cours getCours()
	{
		return this.Cours;	
	}
	public Date getDate()
	{
		return this.date;
	}
	public int getRessourceType()
	{
		return this.ressourceType;
	}
	public String getText()
	{
		//TODO Write Text for Notif!
		return "Notification";
	}
	public int getId()
	{
		return this.Id;
	}
	public int getRessourceId()
	{
		return this.ressourceId;
	}
	
	// M�thodes booleennes
	
	public boolean isOldRessource()
	{
		return this.isOldRessource;
	}
	public boolean isNotified()
	{
		return this.notified;
	}
	public boolean isUpdated()
	{
		return this.Updated;
	}
	
	// M�thodes set
	
	public void setCours(Cours Cours)
	{
		this.Cours=Cours;
	}
	public void setDate(Date date)
	{
		this.date=date;
	}
	public void setValidTypes(int ressourceType)
	{
		this.ressourceType=ressourceType;
	}
	public void setId(int Id)
	{
		this.Id=Id;
	}
	public void setRessourceId(int ressourceId)
	{
		this.ressourceId=ressourceId;
	}	
	public void setOldRessource(boolean isOldRessource)
	{
		this.isOldRessource=isOldRessource;
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
		if(o instanceof Notification){
			return ((Notification) o).getRessourceType() == ressourceType && ((Notification) o).ressourceId == ressourceId && ((Notification) o).getCours().equals(Cours);
		}
		return false;
	}

	public int saveInDb(){		
		if(this.equals(NotificationRepository.GetById(this.getId()))){
			NotificationRepository.Update(this);
		} else {
			this.setId(NotificationRepository.Save(this));
		}
		
		return this.getId();
	}
}
