package model;

//import java.util.Date;

public class Documents 
{
	// Variables globales : propriétés
	
	private String Cours;
	private String date; 
	
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
}
