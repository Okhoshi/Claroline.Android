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
	
	// Version de la base de données
	private static final int DATABASE_VERSION = 1;
	
	// Nom de la base
	private static final String FIRST_BASE_NAME= "first.db";
	
	// Nom de la table
	public static final String FIRST_TABLE_NAME = "First";
	
	// Description des colonnes
	
	
	// Requête SQL pour la création de la base
	private static final String REQUETE_CREATION_BDD = "CREATE TABLE ";
	
	
	// Constructeur
	public DataOpenHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, FIRST_BASE_NAME, factory, DATABASE_VERSION);
	}
	
			 
	/**
	 * Création de la base
	 * Appelée si la base n'existe pas encore
	 */
	@Override	
	public void onCreate(SQLiteDatabase db) 
	{
		    db.execSQL(REQUETE_CREATION_BDD);
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
	    	db.execSQL("DROP TABLE " + FIRST_TABLE_NAME + ";");
	    	onCreate(db);
	    }
	}
	
}