/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Nous avons encore affaire à une classe générique et abstraite cette fois
// Repository sert à faire la jonction avec notre base de données.

public abstract class Repository<T> implements IRepository<T> 
{
	protected static ArrayList<RepositoryRefreshListener> repositoryRefreshListener = new ArrayList<RepositoryRefreshListener>();
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
	 * @return 
	 * 
	 */
	public static void SetOpenHelper(Context context){
		sqLiteOpenHelper = new DBOpenHelper(context, null);
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
	
	public static void addOnRepositoryRefreshListener(RepositoryRefreshListener listener){
		repositoryRefreshListener.add(listener);
	}
	
	public static void remOnRepositoryRefreshListener(RepositoryRefreshListener listener){
		repositoryRefreshListener.remove(listener);
	}
	
	protected static void RefreshRepository(String type){
		if(repositoryRefreshListener.size() > 0){
			for (RepositoryRefreshListener listener : repositoryRefreshListener) {
				listener.onRepositoryRefresh(type);
			}
		}
	}
}