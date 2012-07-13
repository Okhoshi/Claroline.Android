package dataStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



public class DataOpenHelper extends SQLiteOpenHelper
{
	
	/**
	 *    CONSTANTES
	 *
	 */
	
	// Version de la base de donn�es
	private static final int DATABASE_VERSION = 1;
	
	// Nom de la base
	private static final String FIRST_BASE_NAME= "first.db";
	
	// Nom de la table
	public static final String FIRST_TABLE_NAME = "First";
	
	// Description des colonnes
	
	
	// Requ�te SQL pour la cr�ation de la base
	private static final String REQUETE_CREATION_BDD = "CREATE TABLE ";
	
	
	// Constructeur
	public DataOpenHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, FIRST_BASE_NAME, factory, DATABASE_VERSION);
	}
	
			 
	/**
	 * Cr�ation de la base
	 * Appel�e si la base n'existe pas encore
	 */
	@Override	
	public void onCreate(SQLiteDatabase db) 
	{
		    db.execSQL(REQUETE_CREATION_BDD);
	}
			 
	/**
	 * Mise � jour de la base
	 * Appel�e quand la base existe d�j�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	    // Lorsque l'on change le num�ro de version de la base on supprime la
	    // table puis on la recr�e
	    if (newVersion > DATABASE_VERSION) 
	    {
	    	db.execSQL("DROP TABLE " + FIRST_TABLE_NAME + ";");
	    	onCreate(db);
	    }
	}
	
}