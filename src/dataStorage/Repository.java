/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Nous avons encore affaire � une classe g�n�rique et abstraite cette fois
// Repository sert � faire la jonction avec notre base de donn�es.

public abstract class Repository<T> implements IRepository<T> 
{
	    // Base de donn�es
	    protected SQLiteDatabase maBDD;
	 
	    protected SQLiteOpenHelper sqLiteOpenHelper;
	 
	    /**
	     * Constructeur par d�faut
	     */
	    public Repository() 
	    {
	 
	    }
	 
	    /**
	     * Ouverture de la connexion
	     */
	    public void Open() 
	    {
	        maBDD = sqLiteOpenHelper.getWritableDatabase();
	    }
	 
	    /**
	     * Fermeture de la connexion
	     */
	    public void Close() 
	    {
	        maBDD.close();
	    }
	}