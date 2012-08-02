/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class NotificationRepository extends Repository<Notification> {

	public NotificationRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List<Notification> GetAll() {
		// Récupération de la liste des notifications
		Cursor cursor = maBDD.query(DBOpenHelper.NOTIFICATION_TABLE,
	                new String[] {  DBOpenHelper.NOTIFICATION_COLUMN_ID ,
								    DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_COURSID ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_DATE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_TEXT ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_UPDATED  }, null, null, null, null, null);			 
	return ConvertCursorToListObject(cursor);
	}

	public List<Notification> GetAllNotificationsByCoursId(int coursId) {
		// Récupération de la liste des notifications
		Cursor cursor = maBDD.query(DBOpenHelper.NOTIFICATION_TABLE,
	                new String[] {  DBOpenHelper.NOTIFICATION_COLUMN_ID ,
								    DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_COURSID ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_DATE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_TEXT ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_UPDATED  },
			        DBOpenHelper.NOTIFICATION_COLUMN_COURSID + "=?", 
			        new String[] {String.valueOf(coursId)}, null, null, null);			 
	return ConvertCursorToListObject(cursor);
	}

	public Notification GetById(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.NOTIFICATION_TABLE,
									new String[] { DBOpenHelper.NOTIFICATION_COLUMN_ID,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_COURSID,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_DATE,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_TEXT,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_UPDATED },
					                        	DBOpenHelper.NOTIFICATION_COLUMN_ID + "=?",
					                        	new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToObject(cursor);
	}
	
	

	public void Save(Notification entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_COURSID, entite.getCours().getId()); 
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE,entite.isOldRessource());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE,entite.getRessourceType()); 
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_DATE, entite.getDate().toString());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_TEXT,entite.getText());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_UPDATED, entite.isUpdated());
			 
		maBDD.insert(DBOpenHelper.NOTIFICATION_TABLE, null, contentValues);
	}

	public void Update(Notification entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_COURSID, entite.getCours().getId()); 
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE,entite.isOldRessource());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE,entite.getRessourceType()); 
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_DATE, entite.getDate().toString());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_TEXT,entite.getText());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_UPDATED, entite.isUpdated());
			 
		maBDD.update(DBOpenHelper.NOTIFICATION_TABLE, contentValues,	
				     DBOpenHelper.NOTIFICATION_COLUMN_ID + "=?",
				     new String[] { String.valueOf(entite.getId()) });
	}

	public void Delete(int id) {
		maBDD.delete(DBOpenHelper.NOTIFICATION_TABLE,
		         DBOpenHelper.NOTIFICATION_COLUMN_ID + "=?",
		         new String[] { String.valueOf(id) });	
	}

	public List<Notification> ConvertCursorToListObject(Cursor c) {
		List<Notification> liste = new ArrayList<Notification>();
			 
	    // Si la liste est vide
		if (c.getCount() == 0){
			c.close();
		return liste;}
			 
		// position sur le premier item
		c.moveToFirst();
			 
		// Pour chaque item
		do {
			 
			Notification notification = ConvertCursorToObject(c);
			 
			liste.add(notification);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}

	public Notification ConvertCursorToObject(Cursor c) {
		Notification notification = new Notification(	
														CoursRepository.GetByCoursId(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_COURSID)),
														new Date(c.getString(DBOpenHelper.NOTIFICATION_NUM_COLUMN_DATE)),
														c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_RESSOURCETYPE)
												 	);
		
			   notification.setId(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ID));
			   notification.setRessourceId((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_RESSOURCEID)));
			   notification.setOldRessource((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ISOLDRESSOURCE) != 0));
			   notification.setNotified((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_NOTIFIED) != 0));
			   notification.setUpdated((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_UPDATED) != 0));
					 
	    return notification;
	}

	public Notification ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		 
		Notification notification = ConvertCursorToObject(c);
			 
		c.close();
		return notification;
	}

	
	
	public static Notification GetByCourseId(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.NOTIFICATION_TABLE,
									new String[] { DBOpenHelper.NOTIFICATION_COLUMN_ID,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_COURSID,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_DATE,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_TEXT,
					                        	   DBOpenHelper.NOTIFICATION_COLUMN_UPDATED },
					                        	DBOpenHelper.NOTIFICATION_COLUMN_COURSID + "=?",
					                        	new String[] { String.valueOf(id) }, null, null, null);
		return NotificationConvertCursorToObject(cursor);
	}
	
	public static List<Notification> GetAllNotifications() {
		// Récupération de la liste des notifications
		Cursor cursor = maBDD.query(DBOpenHelper.NOTIFICATION_TABLE,
	                new String[] {  DBOpenHelper.NOTIFICATION_COLUMN_ID ,
								    DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_COURSID ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_DATE ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_TEXT ,
			                        DBOpenHelper.NOTIFICATION_COLUMN_UPDATED  }, null, null, null, null, null);			 
	return NotificationConvertCursorToListObject(cursor);
	}
	
	public static Notification NotificationConvertCursorToObject(Cursor c) {
		Notification notification = new Notification(	
														CoursRepository.GetByCoursId(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_COURSID)),
														new Date(c.getString(DBOpenHelper.NOTIFICATION_NUM_COLUMN_DATE)),
														c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_RESSOURCETYPE)
												 	);
		
			   notification.setId(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ID));
			   notification.setOldRessource((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ISOLDRESSOURCE) != 0));
			   notification.setNotified((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_NOTIFIED) != 0));
			   notification.setUpdated((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_UPDATED) != 0));
					 
	    return notification;
	}
	
	public static List<Notification> NotificationConvertCursorToListObject(Cursor c) {
		List<Notification> liste = new ArrayList<Notification>();
			 
	    // Si la liste est vide
		if (c.getCount() == 0){
			c.close();
		return liste;}
			 
		// position sur le premier item
		c.moveToFirst();
			 
		// Pour chaque item
		do {
			 
			Notification notification = NotificationConvertCursorToObject(c);
			 
			liste.add(notification);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}
}
