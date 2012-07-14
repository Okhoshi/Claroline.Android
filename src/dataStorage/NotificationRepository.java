package dataStorage;

import java.util.ArrayList;
import java.util.List;

import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class NotificationRepository extends Repository<Notification> {

	public NotificationRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List GetAll() {
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
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_COURSID, entite.getCours()); 
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE,entite.isOldRessource());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE,entite.getRessourceType()); // Changer le type aussi en String ? --> bizarre
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_DATE, entite.getDate());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_TEXT,entite.getText());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_UPDATED, entite.isUpdated());
			 
		maBDD.insert(DBOpenHelper.NOTIFICATION_TABLE, null, contentValues);
	}

	public void Update(Notification entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_COURSID, entite.getCours()); 
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_ISOLDRESSOURCE,entite.isOldRessource());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_RESSOURCETYPE,entite.getRessourceType()); // Changer le type aussi en String ? --> bizarre
		contentValues.put(DBOpenHelper.NOTIFICATION_COLUMN_DATE, entite.getDate());
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

	public List ConvertCursorToListObject(Cursor c) {
		List liste = new ArrayList();
			 
	    // Si la liste est vide
		if (c.getCount() == 0)
		return liste;
			 
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
		/*Notification notification = new Notification(
			   c.getString(DBOpenHelper.NOTIFICATION_NUM_COLUMN_PRODUIT),
		       c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_QUANTITE));
			   notification.setId(c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ID));
			   notification.setAchete((c.getInt(DBOpenHelper.NOTIFICATION_NUM_COLUMN_ACHETE) != 0));
					 
	    return notification;*/
		return null;
	}

	public Notification ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		 
		Notification notification = ConvertCursorToObject(c);
			 
		c.close();
		return notification;
	}

}
