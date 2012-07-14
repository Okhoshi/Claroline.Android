package dataStorage;

import java.util.ArrayList;
import java.util.List;

import model.Cours;
//import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CoursRepository extends Repository<Cours> {

	public CoursRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List GetAll() {
		// Récupération de la liste des cours
		Cursor cursor = maBDD.query(DBOpenHelper.COURS_TABLE,
		            new String[] {  DBOpenHelper.COURS_COLUMN_ID ,
				 				    DBOpenHelper.COURS_COLUMN_ANNNOTIF ,
			                        DBOpenHelper.COURS_COLUMN_DNLNOTIF ,
			                        DBOpenHelper.COURS_COLUMN_ISANN ,
			                        DBOpenHelper.COURS_COLUMN_ISDNL ,
			                        DBOpenHelper.COURS_COLUMN_ISLOADED ,
			                        DBOpenHelper.COURS_COLUMN_NOTIFIED ,
			                        DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL ,
			                        DBOpenHelper.COURS_COLUMN_SYSCODE ,
			                        DBOpenHelper.COURS_COLUMN_TITLE ,
			                        DBOpenHelper.COURS_COLUMN_TITULAR ,
			                        DBOpenHelper.COURS_COLUMN_UPDATED  }, null, null, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}

	public Cours GetById(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.COURS_TABLE,
	            new String[] {  DBOpenHelper.COURS_COLUMN_ID ,
			 				    DBOpenHelper.COURS_COLUMN_ANNNOTIF ,
		                        DBOpenHelper.COURS_COLUMN_DNLNOTIF ,
		                        DBOpenHelper.COURS_COLUMN_ISANN ,
		                        DBOpenHelper.COURS_COLUMN_ISDNL ,
		                        DBOpenHelper.COURS_COLUMN_ISLOADED ,
		                        DBOpenHelper.COURS_COLUMN_NOTIFIED ,
		                        DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL ,
		                        DBOpenHelper.COURS_COLUMN_SYSCODE ,
		                        DBOpenHelper.COURS_COLUMN_TITLE ,
		                        DBOpenHelper.COURS_COLUMN_TITULAR ,
		                        DBOpenHelper.COURS_COLUMN_UPDATED  }, 
		        DBOpenHelper.COURS_COLUMN_ID + "=?",
		        new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToObject(cursor);
	}

	public void Save(Cours entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.COURS_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ANNNOTIF,entite.isAnnNotif());
		contentValues.put(DBOpenHelper.COURS_COLUMN_DNLNOTIF, entite.isDnlNotif()); 
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISANN,entite.isAnn());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISDNL, entite.isDnL());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISLOADED,entite.getIsLoaded());
		contentValues.put(DBOpenHelper.COURS_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL,entite.getOfficialEmail());
		contentValues.put(DBOpenHelper.COURS_COLUMN_SYSCODE, entite.getSysCode());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITLE, entite.getTitle());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITULAR, entite.getTitular());
		contentValues.put(DBOpenHelper.COURS_COLUMN_UPDATED, entite.isUpdated());
			 
		maBDD.insert(DBOpenHelper.COURS_TABLE, null, contentValues);
		
	}

	public void Update(Cours entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.COURS_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ANNNOTIF,entite.isAnnNotif());
		contentValues.put(DBOpenHelper.COURS_COLUMN_DNLNOTIF, entite.isDnlNotif()); 
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISANN,entite.isAnn());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISDNL, entite.isDnL());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISLOADED,entite.getIsLoaded());
		contentValues.put(DBOpenHelper.COURS_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL,entite.getOfficialEmail());
		contentValues.put(DBOpenHelper.COURS_COLUMN_SYSCODE, entite.getSysCode());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITLE, entite.getTitle());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITULAR, entite.getTitular());
		contentValues.put(DBOpenHelper.COURS_COLUMN_UPDATED, entite.isUpdated());
		
		maBDD.update(DBOpenHelper.COURS_TABLE, contentValues,	
			     DBOpenHelper.COURS_COLUMN_ID + "=?",
			     new String[] { String.valueOf(entite.getId()) });
		
	}

	public void Delete(int id) {
		maBDD.delete(DBOpenHelper.COURS_TABLE,
		         DBOpenHelper.COURS_COLUMN_ID + "=?",
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
			 
			Cours cours = ConvertCursorToObject(c);
			 
			liste.add(cours);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}

	public Cours ConvertCursorToObject(Cursor c) {
		/*Cours cours = new Cours(
		   c.getString(DBOpenHelper.COURS_NUM_COLUMN_PRODUIT),
	       c.getInt(DBOpenHelper.COURS_NUM_COLUMN_QUANTITE));
		   cours.setId(c.getInt(DBOpenHelper.COURS_NUM_COLUMN_ID));
		   cours.setAchete((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_ACHETE) != 0));
				 
 return cours;*/
		return null;
	}

	public Cours ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		 
		Cours cours = ConvertCursorToObject(c);
			 
		c.close();
		return cours;
	}

}
