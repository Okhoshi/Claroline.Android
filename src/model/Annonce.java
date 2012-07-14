package model;

//import java.util.Date;

public class Annonce 
{
	// Variables globales : propriétés
	
	private String Cours;
	private String date; 
	
	private int Id;
	private int ressourceId;
	
	private boolean notified;
	private boolean Updated;
	private boolean visibility;
	
	private String title;
	private String content;
	
	
	// Methodes get
	public String getCours()
	{
		return this.Cours;	
	}
	public String getDate()
	{
		return this.date;
	}
	public int getId()
	{
		return this.Id;
	}
	public int getRessourceId()
	{
		return this.ressourceId;
	}
	public String getTitle()
	{
		return this.title;
	}
	public String getContent()
	{
		return this.content;
	}
	
	
	// Methodes Booleennes
	public boolean isNotified()
	{
		return this.notified;
	}
	public boolean isUpdated()
	{
		return this.Updated;
	}
	public boolean isVisible()
	{
		return this.visibility;
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
	public void setId(int Id)
	{
		this.Id=Id;
	}
	public void setRessourceId(int ressourceId)
	{
		this.ressourceId=ressourceId;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public void setContent(String content)
	{
		this.content=content;
	}
	public void setNotified(boolean notified)
	{
		this.notified=notified;
	}
	public void setUpdated(boolean Updated)
	{
		this.Updated=Updated;
	}
	public void setVisible(boolean visibility)
	{
		this.visibility=visibility;
	}
	
	

}
