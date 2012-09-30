/**
 * @author Dim
 * @version 1
 */
package model;

import java.util.Date;

import mobile.claroline.R;

//import java.util.Date;

public class Documents 
{
	// Variables globales : propriétés
	
	private Cours Cours;
	private Date date; 
	
	private boolean IsFolder;
	private boolean notified;
	private boolean Updated;
	private boolean visibility;
	
	private String Description;
	private String Extension;
	private String name;
	private String path;
	private String url;
	
	private int Id;
	private double size;
	
	// Constructeur
	public Documents(Cours Cours, Date date, String Description, String Extension, String name, String path, String url)
	{
		this.Cours=Cours;
		this.date=date;
		this.IsFolder=false;
		this.notified=true;
		this.Updated=true;
		this.visibility=true;
		this.Description=Description;
		this.Extension=Extension;
		this.name=name;
		this.path=path;
		this.url=url;
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
		public double getSize()
		{
			return this.size;
		}
		public String getDescription()
		{
			return this.Description;
		}
		public String getExtension()
		{
			return this.Extension;
		}
		public String getName()
		{
			return this.name;
		}
		public String getPath()
		{
			return this.path;
		}
		public String getUrl()
		{
			return this.url;
		}
		
		
		// Méthodes booleennes
		
		public boolean isFolder()
		{
			return this.IsFolder;
		}
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
		public void setSize(double size)
		{
			this.size=size;
		}
		public void setDescription(String Description)
		{
			this.Description=Description;
		}
		public void setExtension(String Extension)
		{
			this.Extension=Extension;
		}
		public void setName(String name)
		{
			this.name=name;
		}
		public void setPath(String path)
		{
			this.path=path;
		}
		public void setUrl(String url)
		{
			this.url=url;
		}
		public void setFolder(boolean IsFolder)
		{
			this.IsFolder=IsFolder;
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
		
		@Override
		public boolean equals(Object o){
			if(o instanceof Documents){
				return ((Documents) o).getPath().equals(path) && ((Documents) o).getCours().equals(Cours);
			}
			return false;
		}

		public String getStringSize() {
			double div = getSize();
			if (div < 1) return "";
			div /= Double.parseDouble("1E+9");
			if (div > 1)
				return Math.round(div) + " Go";
			else
			{
				div *= 1000;
				if (div > 1)
					return Math.round(div) + " Mo";
				else
				{
					div *= 1000;
					if (div > 1)
						return Math.round(div) + " Ko";
				}
			}
			return Math.round(getSize()) + " o";
		}
}
