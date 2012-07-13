package dataStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



public class DataOpenHelper extends SQLiteOpenHelper
{
	
	/**
	 *    Constantes
	 *
	 */
	
	// Version de la base de données
	private static final int DATABASE_VERSION = 1;
	
	// Nom de la base
	private static final String FIRST_BASE_NAME= "first.db";
	
	// Nom de la table
	public static final String FIRST_TABLE_NAME = "First";
	
	// Description des colonnes
	
	
	// Requête SQL pour la création da la base
	
	
	// Constructeur
	public DataOpenHelper(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}
	
}