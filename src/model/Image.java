package model;


public class Image {
		// Variables globales : propri�t�s
		
		private String path;
		private int Id;
		
		
		// Construteur // Sans boolean et sans Id et RessourceId 
		public  Image(String path)
		{
			this.path=path;

			
		}
		
		// Methodes get
		public String getPath()
		{
			return this.path;	
		}	
		public int getId()
		{
			return this.Id;
		}
		
		// M�thodes set
		
		public void setPath(String path)
		{
			this.path=path;
		}
		public void setId(int Id)
		{
			this.Id=Id;
		}

		
		
}
