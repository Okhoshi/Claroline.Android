/**
 * @author Dim
 * @version 1
 */
package dataStorage;

import java.util.List;
import android.database.Cursor;


// Classe générique : le T n'est pas défini , nous pouvons le définir par la suite
public interface IRepository<T>
{
	public List<T> GetAll();
		    public T GetById(int id);
		 
	 	    public void Save(T entite);
		    public void Update(T entite);
		    public void Delete(int id);
		 
		    public List<T> ConvertCursorToListObject(Cursor c);
		    public T ConvertCursorToObject(Cursor c);
		    public T ConvertCursorToOneObject(Cursor c);
}