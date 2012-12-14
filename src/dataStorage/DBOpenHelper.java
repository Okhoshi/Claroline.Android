/**
 * @author Dim
 * @version 1
 * 
 * @description The benefit is that you don't have to determine in your code when exactly to create or update the database;
 * 			     Android will call the methods which you have defined at the correct moment. 
 */
package dataStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



public class DBOpenHelper extends SQLiteOpenHelper
{
	
	/**
	 *    CONSTANTES
	 *
	 */
	
	// Version de la base de données
	private static final int DATABASE_VERSION = 1;
	
	// Nom de la base
	protected static final String BASE_NAME= "dataBase.db";
	
	// Nom des tables
	public static final String ANNONCE_TABLE      = "Annonce";
	public static final String COURS_TABLE        = "Cours";
	public static final String DOCUMENTS_TABLE    = "Documents";
	public static final String NOTIFICATION_TABLE = "Notification";
	public static final String IMAGE_TABLE 		  = "Image";
	
	
	// Description des colonnes de la table Annonce
	public static final String ANNONCE_COLUMN_ID             = "ID";
	public static final int ANNONCE_NUM_COLUMN_ID            = 0;
	public static final String ANNONCE_COLUMN_RESSOURCEID    = "RESSOURCEID";
	public static final int ANNONCE_NUM_COLUMN_RESSOURCEID   = 1;
	public static final String ANNONCE_COLUMN_COURSID        = "COURSID";
	public static final int ANNONCE_NUM_COLUMN_COURSID       = 2;
	public static final String ANNONCE_COLUMN_TITLE          = "TITLE";
	public static final int ANNONCE_NUM_COLUMN_TITLE         = 3;
	public static final String ANNONCE_COLUMN_CONTENT        = "CONTENT";
	public static final int ANNONCE_NUM_COLUMN_CONTENT       = 4;
	public static final String ANNONCE_COLUMN_NOTIFIED       = "NOTIFIED";
	public static final int ANNONCE_NUM_COLUMN_NOTIFIED      = 5;
	public static final String ANNONCE_COLUMN_UPDATED        = "UPDATED";
	public static final int ANNONCE_NUM_COLUMN_UPDATED       = 6;
	public static final String ANNONCE_COLUMN_VISIBILITY     = "VISIBILITY";
	public static final int ANNONCE_NUM_COLUMN_VISIBILITY    = 7;
	public static final String ANNONCE_COLUMN_DATE     		 = "DATE";
	public static final int ANNONCE_NUM_COLUMN_DATE   		 = 8;
	public static final String ANNONCE_COLUMN_LOADED     	 = "LOADED";
	public static final int ANNONCE_NUM_COLUMN_LOADED   	 = 9;
	
	
	// Description des colonnes de la table Cours
	public static final String COURS_COLUMN_ID 				 = "ID";
	public static final int COURS_NUM_COLUMN_ID 			 = 0;
	public static final String COURS_COLUMN_ANNNOTIF 		 = "ANNNOTIF";
	public static final int COURS_NUM_COLUMN_ANNNOTIF		 = 1;
	public static final String COURS_COLUMN_DNLNOTIF 		 = "DNLNOTIF";
	public static final int COURS_NUM_COLUMN_DNLNOTIF		 = 2;
	public static final String COURS_COLUMN_ISANN 			 = "ISANN";
	public static final int COURS_NUM_COLUMN_ISANN			 = 3;
	public static final String COURS_COLUMN_ISDNL 			 = "ISDNL";
	public static final int COURS_NUM_COLUMN_ISDNL		 	 = 4;
	public static final String COURS_COLUMN_ISLOADED		 = "ISLOADED";
	public static final int COURS_NUM_COLUMN_ISLOADED		 = 5;
	public static final String COURS_COLUMN_NOTIFIED		 = "NOTIFIED";
	public static final int COURS_NUM_COLUMN_NOTIFIED		 = 6;
	public static final String COURS_COLUMN_OFFICIALEMAIL	 = "OFFICIALEMAIL";
	public static final int COURS_NUM_COLUMN_OFFICIALEMAIL	 = 7;
	public static final String COURS_COLUMN_OFFICIALCODE	 = "OFFICIALCODE";
	public static final int COURS_NUM_COLUMN_OFFICIALCODE	 = 8;
	public static final String COURS_COLUMN_SYSCODE		 	 = "SYSCODE";
	public static final int COURS_NUM_COLUMN_SYSCODE		 = 9;
	public static final String COURS_COLUMN_TITLE			 = "TITLE";
	public static final int COURS_NUM_COLUMN_TITLE			 = 10;
	public static final String COURS_COLUMN_TITULAR			 = "TITULAR";
	public static final int COURS_NUM_COLUMN_TITULAR		 = 11;
	public static final String COURS_COLUMN_UPDATED		 	 = "UPDATED";
	public static final int COURS_NUM_COLUMN_UPDATED		 = 12;
	
	
	// Description des colonnes de la table Documents
	public static final String DOCUMENTS_COLUMN_ID 			 = "ID";
	public static final int DOCUMENTS_NUM_COLUMN_ID 		 = 0;
	public static final String DOCUMENTS_COLUMN_COURSID 	 = "COURSID";
	public static final int DOCUMENTS_NUM_COLUMN_COURSID 	 = 1;
	public static final String DOCUMENTS_COLUMN_DATE 		 = "DATE";
	public static final int DOCUMENTS_NUM_COLUMN_DATE 		 = 2;
	public static final String DOCUMENTS_COLUMN_DESCRIPTION  = "DESCRIPTION";
	public static final int DOCUMENTS_NUM_COLUMN_DESCRIPTION = 3;
	public static final String DOCUMENTS_COLUMN_EXTENSION 	 = "EXTENSION";
	public static final int DOCUMENTS_NUM_COLUMN_EXTENSION 	 = 4;
	public static final String DOCUMENTS_COLUMN_ISFOLDER 	 = "ISFOLDER";
	public static final int DOCUMENTS_NUM_COLUMN_ISFOLDER 	 = 5;
	public static final String DOCUMENTS_COLUMN_NAME		 = "NAME";
	public static final int DOCUMENTS_NUM_COLUMN_NAME 		 = 6;
	public static final String DOCUMENTS_COLUMN_NOTIFIED 	 = "NOTIFIED";
	public static final int DOCUMENTS_NUM_COLUMN_NOTIFIED 	 = 7;
	public static final String DOCUMENTS_COLUMN_PATH 		 = "PATH";
	public static final int DOCUMENTS_NUM_COLUMN_PATH		 = 8;
	public static final String DOCUMENTS_COLUMN_SIZE		 = "SIZE";
	public static final int DOCUMENTS_NUM_COLUMN_SIZE 		 = 9;
	public static final String DOCUMENTS_COLUMN_UPDATED		 = "UPDATED";
	public static final int DOCUMENTS_NUM_COLUMN_UPDATED	 = 10;
	public static final String DOCUMENTS_COLUMN_URL 		 = "URL";
	public static final int DOCUMENTS_NUM_COLUMN_URL		 = 11;
	public static final String DOCUMENTS_COLUMN_VISIBILITY 	 = "VISIBILITY";
	public static final int DOCUMENTS_NUM_COLUMN_VISIBILITY  = 12;
	public static final String DOCUMENTS_COLUMN_LOADED 	 	 = "LOADED";
	public static final int DOCUMENTS_NUM_COLUMN_LOADED  	 = 13;
	
	// Description des colonnes de la table Notification
	public static final String NOTIFICATION_COLUMN_ID 		 		= "ID";
	public static final int NOTIFICATION_NUM_COLUMN_ID 		 		= 0;
	public static final String NOTIFICATION_COLUMN_RESSOURCEID  	= "RESSOURCEID";
	public static final int NOTIFICATION_NUM_COLUMN_RESSOURCEID 	= 1;
	public static final String NOTIFICATION_COLUMN_COURSID 			= "COURSID";
	public static final int NOTIFICATION_NUM_COLUMN_COURSID 		= 2;
	public static final String NOTIFICATION_COLUMN_ISOLDRESSOURCE 	= "ISOLDRESSOURCE";
	public static final int NOTIFICATION_NUM_COLUMN_ISOLDRESSOURCE  = 3;
	public static final String NOTIFICATION_COLUMN_NOTIFIED 		= "NOTIFIED";
	public static final int NOTIFICATION_NUM_COLUMN_NOTIFIED 		= 4;
	public static final String NOTIFICATION_COLUMN_RESSOURCETYPE 	= "RESSOURCETYPE";
	public static final int NOTIFICATION_NUM_COLUMN_RESSOURCETYPE 	= 5;
	public static final String NOTIFICATION_COLUMN_DATE 		 	= "DATE";
	public static final int NOTIFICATION_NUM_COLUMN_DATE		 	= 6;
	public static final String NOTIFICATION_COLUMN_TEXT 		 	= "TEXT";
	public static final int NOTIFICATION_NUM_COLUMN_TEXT	 		= 7;
	public static final String NOTIFICATION_COLUMN_UPDATED 		 	= "UPDATED";
	public static final int NOTIFICATION_NUM_COLUMN_UPDATED 		= 8;
	
	
	// Description des colonnes de la table Documents
	public static final String IMAGE_COLUMN_ID 			 = "ID";
	public static final int IMAGE_NUM_COLUMN_ID 		 = 0;
	public static final String IMAGE_COLUMN_PATH 		 = "PATH";
	public static final int IMAGE_NUM_COLUMN_PATH		 = 1;
	
	
	
	
	
	// Requêtes SQL pour la création de la base
	
	private static final String CREATE_TABLE_ANNONCE =
			" CREATE TABLE " + ANNONCE_TABLE +
			" (" + ANNONCE_COLUMN_ID 			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				   ANNONCE_COLUMN_RESSOURCEID 	+ " INTEGER NOT NULL, " +
				   ANNONCE_COLUMN_COURSID  		+ " INTEGER NOT NULL, " +
				   ANNONCE_COLUMN_TITLE  		+ " TEXT NOT NULL, " +
				   ANNONCE_COLUMN_CONTENT  		+ " TEXT NOT NULL, " +
				   ANNONCE_COLUMN_NOTIFIED  	+ " INTEGER NOT NULL, " +
				   ANNONCE_COLUMN_UPDATED  		+ " INTEGER NOT NULL, " +
				   ANNONCE_COLUMN_VISIBILITY 	+ " INTEGER NOT NULL, " +
				   ANNONCE_COLUMN_DATE  		+ " INTEGER NOT NULL," +
				   ANNONCE_COLUMN_LOADED  		+ " INTEGER NOT NULL" +
			");";

	private static final String CREATE_TABLE_COURS =
			" CREATE TABLE " + COURS_TABLE +
			" (" + COURS_COLUMN_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
				   COURS_COLUMN_ANNNOTIF 		+ " INTEGER NOT NULL," +
				   COURS_COLUMN_DNLNOTIF 		+ " INTEGER NOT NULL," +
				   COURS_COLUMN_ISANN 			+ " INTEGER NOT NULL," +
				   COURS_COLUMN_ISDNL 			+ " INTEGER NOT NULL," +
				   COURS_COLUMN_ISLOADED 		+ " INTEGER NOT NULL," +
				   COURS_COLUMN_NOTIFIED 		+ " INTEGER NOT NULL," +
				   COURS_COLUMN_OFFICIALEMAIL 	+ " TEXT," +
				   COURS_COLUMN_OFFICIALCODE 	+ " TEXT," +
				   COURS_COLUMN_SYSCODE 		+ " TEXT NOT NULL," +
				   COURS_COLUMN_TITLE 			+ " TEXT NOT NULL," +
				   COURS_COLUMN_TITULAR 		+ " TEXT NOT NULL," +
				   COURS_COLUMN_UPDATED 		+ " INTEGER NOT NULL" +
			")";
		
	private static final String CREATE_TABLE_DOCUMENTS =
			" CREATE TABLE " + DOCUMENTS_TABLE  +
			" (" + DOCUMENTS_COLUMN_ID 			+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				   DOCUMENTS_COLUMN_COURSID 	+" INTEGER NOT NULL," + 
				   DOCUMENTS_COLUMN_DATE 		+" INTEGER," +
				   DOCUMENTS_COLUMN_DESCRIPTION +" TEXT," +
				   DOCUMENTS_COLUMN_EXTENSION 	+" TEXT," +
				   DOCUMENTS_COLUMN_ISFOLDER 	+" INTEGER NOT NULL," +
				   DOCUMENTS_COLUMN_NAME 		+" TEXT NOT NULL," +
				   DOCUMENTS_COLUMN_NOTIFIED 	+" INTEGER NOT NULL," +
				   DOCUMENTS_COLUMN_PATH 		+" TEXT NOT NULL," +
				   DOCUMENTS_COLUMN_SIZE 		+" TEXT," + 
				   DOCUMENTS_COLUMN_UPDATED 	+" INTEGER NOT NULL," +
				   DOCUMENTS_COLUMN_URL 		+" TEXT," +
				   DOCUMENTS_COLUMN_VISIBILITY 	+" INTEGER NOT NULL," +
				   DOCUMENTS_COLUMN_LOADED		+" INTEGER NOT NULL" +
			");";
			
	private static final String CREATE_TABLE_NOTIFICATION =
			" CREATE TABLE " + NOTIFICATION_TABLE + 
			" (" + NOTIFICATION_COLUMN_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
				   NOTIFICATION_COLUMN_RESSOURCEID 		+ " INTEGER NOT NULL," +
				   NOTIFICATION_COLUMN_COURSID 			+ " INTEGER NOT NULL," +
				   NOTIFICATION_COLUMN_ISOLDRESSOURCE 	+ " INTEGER NOT NULL," +
				   NOTIFICATION_COLUMN_NOTIFIED 		+ " INTEGER NOT NULL," +
				   NOTIFICATION_COLUMN_RESSOURCETYPE 	+ " INTEGER NOT NULL," +
				   NOTIFICATION_COLUMN_DATE 			+ " INTEGER NOT NULL," +
				   NOTIFICATION_COLUMN_TEXT 			+ " TEXT NOT NULL," +
				   NOTIFICATION_COLUMN_UPDATED 			+ " INTEGER NOT NULL" +
			");";
	
	private static final String CREATE_TABLE_IMAGE =
			" CREATE TABLE " + IMAGE_TABLE +
			" (" + IMAGE_COLUMN_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
				   IMAGE_COLUMN_PATH 			+" TEXT NOT NULL" +
		     ");";
	
	
	// Constructeur
	public DBOpenHelper(Context context, CursorFactory factory) 
	{
		super(context, BASE_NAME, factory, DATABASE_VERSION);
	}
	
			
	
	
	/**
	 * Création des tables
	 * Appelée si la base n'existe pas encore
	 */
	@Override	
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(CREATE_TABLE_ANNONCE);
		db.execSQL(CREATE_TABLE_COURS);
		db.execSQL(CREATE_TABLE_DOCUMENTS);
		db.execSQL(CREATE_TABLE_NOTIFICATION);
		db.execSQL(CREATE_TABLE_IMAGE);
	}
		
	
	
	/**
	 * Mise à jour de la base
	 * Appelée quand la base existe déjà
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	    // Lorsque l'on change le numéro de version de la base on supprime la
	    // table puis on la recrée
	    if (newVersion > DATABASE_VERSION) 
	    {
	    	db.execSQL("DROP TABLE " + ANNONCE_TABLE + ";");
	    	db.execSQL("DROP TABLE " + COURS_TABLE + ";");
	    	db.execSQL("DROP TABLE " + DOCUMENTS_TABLE + ";");
	    	db.execSQL("DROP TABLE " + NOTIFICATION_TABLE + ";");
	    	db.execSQL("DROP TABLE " + IMAGE_TABLE + ";");
	    	onCreate(db);
	    }
	}
	
}