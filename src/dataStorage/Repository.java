/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

// Nous avons encore affaire � une classe g�n�rique et abstraite cette fois
// Repository sert � faire la jonction avec notre base de donn�es.

public abstract class Repository<T> implements IRepository<T> 
{
	public static final String ALL = "All";
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
	
	public static void Reset(Context context)
	{
		context.deleteDatabase(DBOpenHelper.BASE_NAME);
		sqLiteOpenHelper = new DBOpenHelper(context, null);
		Open();
		RefreshRepository(ALL);
	}
	
	public static boolean isOpen(){
		return maBDD.isOpen();
	}
	
	public static void registerOnRepositoryRefreshListener(RepositoryRefreshListener listener){
		repositoryRefreshListener.add(listener);
	}
	
	public static void unregisterOnRepositoryRefreshListener(RepositoryRefreshListener listener){
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