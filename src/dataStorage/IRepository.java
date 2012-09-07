/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import java.util.List;
import android.database.Cursor;


// Classe générique : le T n'est pas défini , nous pouvons le définir par la suite
public interface IRepository<T>
{	
	public List<T> getAll();
	public T getById(int id);

	public void save(T entite);
	public void update(T entite);
	public void delete(int id);

	public List<T> convertCursorToListObject(Cursor c);
	public T convertCursorToObject(Cursor c);
	
	public interface RepositoryRefreshListener {
		public abstract void onRepositoryRefresh(String type);
	}
}