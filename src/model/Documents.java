/**
 * @author Dim
 * @version 1
 */
package model;

import android.os.Environment;
import android.util.Log;
import app.GlobalApplication;
import dataStorage.DocumentsRepository;
import net.claroline.mobile.android.R;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//import java.util.Date;

public class Documents 
{
	// Variables globales : propri�t�s
	
	private Cours Cours;
	private Date date; 
	private Date loaded;
	
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
		
		
		// M�thodes booleennes
		
		/**
		 * @return the loaded
		 */
		public Date getLoaded() {
			return loaded;
		}

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
		
		/**
		 * @param loaded the loaded to set
		 */
		public void setLoaded(Date loaded) {
			this.loaded = loaded;
		}

		public List<Documents> getContent(){
			List<Documents> liste;
			if(IsFolder){
				String added = name.equals("ROOT")?"": name + "/";
				liste = DocumentsRepository.GetAllByPath(path + added , Cours.getId());
			} else {
				liste = null;
			}
			return liste;
		}
		
		public Documents getRoot(){
            if (path.equals("/"))
            {
                return getEmptyRoot(Cours);
            }
            else
            {
                String rootPath = path.substring(0, path.length()-1);
                String rootName = rootPath.substring(rootPath.lastIndexOf("/")+1);
                rootPath = rootPath.substring(0, rootPath.lastIndexOf(rootName));
				return DocumentsRepository.GetRootByPath(rootName, rootPath, Cours.getId());
            }
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof Documents){
				return ((Documents) o).getPath().equals(path) && ((Documents) o).getCours().equals(Cours) &&
						((Documents) o).getName().equals(name);
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

		public static Documents getEmptyRoot(Cours currentCours) {
            Documents _doc = new Documents(currentCours, null, "", "", "ROOT", "/", "");
            _doc.setFolder(true);
            return _doc;
		}

		public String getFullPath() {
		 return name.equals("ROOT")?"/":getPath() + getName() + "/";
		}

		public int saveInDB(){
			if(this.equals(DocumentsRepository.GetWithoutId(this))){
				this.setLoaded(new Date(0));
				DocumentsRepository.Update(this);
			} else {
				this.setId(DocumentsRepository.Save(this));
			}
			
			return this.getId();
		}

		public boolean isOnMemory() {
			GregorianCalendar temp = new GregorianCalendar();
			temp.setTime(getLoaded());
			temp.add(Calendar.DAY_OF_YEAR, 7);

			if((new GregorianCalendar()).before(temp)){
				//Exits the function if the storage is not writable!
				if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					Log.d("ClaroClient", "Missing SDCard");
					return false;
				}

				File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);               

				File file = new File (root.getAbsolutePath() + "/" + GlobalApplication.getInstance().getResources().getString(R.string.app_name), getName() + "." + getExtension());
				return file.exists() && file.canRead();
			}
			return false;
		}
}
