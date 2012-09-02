/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import java.util.ArrayList;

import dataStorage.IRepository.RepositoryRefreshListener;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Nous avons encore affaire � une classe g�n�rique et abstraite cette fois
// Repository sert � faire la jonction avec notre base de donn�es.

public abstract class Repository<T> implements IRepository<T> 
{
	protected static ArrayList<RepositoryRefreshListener> repositoryRefreshListener = new ArrayList<RepositoryRefreshListener>();
	// Base de donn�es
	protected static SQLiteDatabase maBDD;

	protected static SQLiteOpenHelper sqLiteOpenHelper;

	/**
	 * Constructeur par d�faut
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
	
	public static void addOnRepositoryRefreshListener(RepositoryRefreshListener listener){
		repositoryRefreshListener.add(listener);
	}
	
	public static void remOnRepositoryRefreshListener(RepositoryRefreshListener listener){
		repositoryRefreshListener.remove(listener);
	}
	
	protected static void RefreshRepository(String type){
		for (RepositoryRefreshListener listener : repositoryRefreshListener) {
			listener.onRepositoryRefresh(type);
		}
	}
}