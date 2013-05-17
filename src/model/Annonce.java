/**
 * @author Dim
 * @version 1
 */
package model;

import dataStorage.AnnonceRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import java.util.Date;

public class Annonce 
{
	// Variables globales : propri�t�s
	
	private Cours Cours;
	private Date date;
	private Date loaded;
	
	private int Id;
	private int resourceId;
	
	private boolean notified;
	private boolean Updated;
	private boolean visibility;
	
	private String title;
	private String content;
	
	
	// Construteur // Sans boolean et sans Id et RessourceId 
	public  Annonce(Cours Cours, Date date, String title, String content)
	{
		this.Cours=Cours;
		this.date=date;
		this.notified=true;
		this.Updated=true;
		this.visibility=true;
		this.title=title;
		this.content=content;
		
	}
	
	// Methodes get
	public Cours getCours()
	{
		return this.Cours;	
	}
	public Date getDate()
	{
		return this.date;
	}
	public int getId()
	{
		return this.Id;
	}
	public int getResourceId()
	{
		return this.resourceId;
	}
	public String getTitle()
	{
		return this.title;
	}
	public String getContent()
	{
		return this.content;
	}
	
	
	/**
	 * @return the loaded
	 */
	public Date getLoaded() {
		return loaded;
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
	
	
	// M�thodes set
	
	public void setCours(Cours Cours)
	{
		this.Cours=Cours;
	}
	public void setDate(Date date)
	{
		this.date=date;
	}
	public void setId(int Id)
	{
		this.Id=Id;
	}
	public void setResourceId(int resourceId)
	{
		this.resourceId = resourceId;
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
	
	/**
	 * @param loaded the loaded to set
	 */
	public void setLoaded(Date loaded) {
		this.loaded = loaded;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Annonce){
			return ((Annonce) o).getCours().equals(Cours) && ((Annonce) o).getResourceId() == resourceId;
		}
		return false;
	}

	public int saveInDB(){
		if(this.equals(AnnonceRepository.GetByResourceId(this.getResourceId(), this.getCours().getId()))){
			AnnonceRepository.Update(this);
		} else {
			this.setId(AnnonceRepository.Save(this));
		}
		return this.getId();
	}
	
	public boolean isExpired(){
		GregorianCalendar temp = new GregorianCalendar();
		temp.setTime(getLoaded());
		temp.add(Calendar.DAY_OF_YEAR, 7);
		return (new GregorianCalendar()).after(temp);
	}

}
