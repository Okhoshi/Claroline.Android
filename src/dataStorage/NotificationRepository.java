/**
 * @author Dim
 * @version 1
 * 
 * @description : Repository commonly refers to a location for storage
 */
package dataStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Annonce;
import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class NotificationRepository extends Repository<Notification> {

	public NotificationRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List<Notification> getAll() {
		return GetAll();
	}

	public Notification getById(int id) {
		return GetById(id);
	}

	public void save(Notification entite) {
		Save(entite);
	}

	public void update(Notification entite) {
		Update(entite);
	}

	public void delete(int id) {
		Delete(id);
	}

	public List<Notification> convertCursorToListObject(Cursor c) {
		return ConvertCursorToListObject(c);
	}

	public Notification convertCursorToObject(Cursor c) {
		return ConvertCursorToObject(c);
	}

	/*
	 * Static methods of NotificationRepository
	 */

	public static List<Notification> GetAll() {
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

	public static Notification GetById(int id) {
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
		Notification notification;
		if(cursor.moveToFirst()){
			notification = ConvertCursorToObject(cursor);
		} else {
			notification = null;
		}
		return notification;
	}



	public static void Save(Notification entite) {
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

	public static void Update(Notification entite) {
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

	public static void Delete(int id) {
		maBDD.delete(DBOpenHelper.NOTIFICATION_TABLE,
				DBOpenHelper.NOTIFICATION_COLUMN_ID + "=?",
				new String[] { String.valueOf(id) });	
	}

	public static List<Notification> ConvertCursorToListObject(Cursor c) {
		List<Notification> liste = new ArrayList<Notification>();

		// Si la liste est vide
		if (c.getCount() == 0){
			c.close();
			return liste;
		}

		// Pour chaque item
		while (c.moveToNext()){

			Notification notification = ConvertCursorToObject(c);

			liste.add(notification);
		}

		// Fermeture du curseur
		c.close();

		return liste;
	}

	public static Notification ConvertCursorToObject(Cursor c) {
		Notification notification;
		try {
			notification = new Notification(	
					CoursRepository.GetById(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_COURSID)),
					(new SimpleDateFormat("yyyy-MM-dd")).parse(c.getString(DBOpenHelper.NOTIFICATION_NUM_COLUMN_DATE)),
					c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_RESSOURCETYPE)
					);

			notification.setId(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ID));
			notification.setRessourceId((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_RESSOURCEID)));
			notification.setOldRessource((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ISOLDRESSOURCE) != 0));
			notification.setNotified((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_NOTIFIED) != 0));
			notification.setUpdated((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_UPDATED) != 0));

			return notification;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Notification> GetAllNotificationsByCoursId(int coursId) {
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
}
