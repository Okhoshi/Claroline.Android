/**
 * @author Dim
 * @version 1
 */
package model;

//import java.util.Date;

public class Notification 
{
	// Enumeration
	
	private enum ValidTypes
	{
		DOCUMENTS,ANNONCES;
	}
	
	// Variables globales : propriétés
	
	private String Cours;
	private String date;
	private ValidTypes ressourceType;
	
	private String Text;
	
	private boolean isOldRessource;
	private boolean notified;
	private boolean Updated;
	
	private int Id;
	private int ressourceId;
	
	
	// Constructeur
	public Notification(String Cours, String date, ValidTypes ressourceType, String Text)
	{
		this.Cours=Cours;
		this.date=date;
		this.ressourceType=ressourceType;
		this.Text=Text;
		this.isOldRessource=false;
		this.notified=true;
		this.Updated=true;
	}

	// Méthodes get
	
	public String getCours()
	{
		return this.Cours;	
	}
	public String getDate()
	{
		return this.date;
	}
	public ValidTypes getRessourceType()
	{
		return this.ressourceType;
	}
	public String getText()
	{
		return this.Text;
	}
	public int getId()
	{
		return this.Id;
	}
	public int getRessourceId()
	{
		return this.ressourceId;
	}
	
	// Méthodes booleennes
	
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
	
	// Méthodes set
	
	public void setCours(String Cours)
	{
		this.Cours=Cours;
	}
	public void setDate(String date)
	{
		this.date=date;
	}
	public void setValidTypes(ValidTypes ressourceType)
	{
		this.ressourceType=ressourceType;
	}
	public void setText(String Text)
	{
		this.Text=Text;
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
}
