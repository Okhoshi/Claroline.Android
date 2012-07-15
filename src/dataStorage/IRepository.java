/**
 * @author Dim
 * @version 1
 */
package dataStorage;

import java.util.List;
import android.database.Cursor;


// Classe g�n�rique : le T n'est pas d�fini , nous pouvons le d�finir par la suite
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