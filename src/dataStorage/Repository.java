/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Nous avons encore affaire à une classe générique et abstraite cette fois
// Repository sert à faire la jonction avec notre base de données.

public abstract class Repository<T> implements IRepository<T> 
{
	    // Base de données
	    protected static SQLiteDatabase maBDD;
	 
	    protected static SQLiteOpenHelper sqLiteOpenHelper;
	 
	    /**
	     * Constructeur par défaut
	     */
	    public Repository() 
	    {
	 
	    }
	 
	    /**
	     * Ouverture de la connexion
	     */
	    public static void Open() 
	    {
	        maBDD = sqLiteOpenHelper.getWritableDatabase();
	    }
	 
	    /**
	     * Fermeture de la connexion
	     */
	    public static void Close() 
	    {
	        maBDD.close();
	    }
	}