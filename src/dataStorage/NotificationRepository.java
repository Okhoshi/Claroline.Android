package dataStorage;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class NotificationRepository extends Repository<Object> {

	public NotificationRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List GetAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object GetById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void Save(Object entite) {
		// TODO Auto-generated method stub
		
	}

	public void Update(Object entite) {
		// TODO Auto-generated method stub
		
	}

	public void Delete(int id) {
		// TODO Auto-generated method stub
		
	}

	public List ConvertCursorToListObject(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object ConvertCursorToObject(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object ConvertCursorToOneObject(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

}
