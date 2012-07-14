package dataStorage;

import java.util.ArrayList;
import java.util.List;

import model.Annonce;
//import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AnnonceRepository extends Repository<Annonce> {

	public AnnonceRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List GetAll() {
		// Récupération de la liste des annonces
		Cursor cursor = maBDD.query(DBOpenHelper.ANNONCE_TABLE,
	                new String[] {  DBOpenHelper.ANNONCE_COLUMN_ID ,
								    DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID ,
			                        DBOpenHelper.ANNONCE_COLUMN_COURSID ,
			                        DBOpenHelper.ANNONCE_COLUMN_TITLE ,
			                        DBOpenHelper.ANNONCE_COLUMN_CONTENT ,
			                        DBOpenHelper.ANNONCE_COLUMN_NOTIFIED ,
			                        DBOpenHelper.ANNONCE_COLUMN_UPDATED ,
			                        DBOpenHelper.ANNONCE_COLUMN_VISIBILITY ,
			                        DBOpenHelper.ANNONCE_COLUMN_DATE  }, null, null, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}

	public Annonce GetById(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.ANNONCE_TABLE,
				new String[] { DBOpenHelper.ANNONCE_COLUMN_ID,
                        	   DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID,
                        	   DBOpenHelper.ANNONCE_COLUMN_COURSID,
                        	   DBOpenHelper.ANNONCE_COLUMN_TITLE,
                        	   DBOpenHelper.ANNONCE_COLUMN_CONTENT,
                        	   DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,
                        	   DBOpenHelper.ANNONCE_COLUMN_UPDATED,
                        	   DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,
                        	   DBOpenHelper.ANNONCE_COLUMN_DATE },
                DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null);
		return ConvertCursorToObject(cursor);
	}

	public void Save(Annonce entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_COURSID, entite.getCours()); // Changeons nous le type du return du getCours() en String à la place de Cours
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_TITLE,entite.getTitle());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_CONTENT, entite.getContent());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,entite.isVisible());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_DATE, entite.getDate());
			 
		maBDD.insert(DBOpenHelper.ANNONCE_TABLE, null, contentValues);
		
	}

	public void Update(Annonce entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_COURSID, entite.getCours()); // Changeons nous le type du return du getCours() en String à la place de Cours
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_TITLE,entite.getTitle());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_CONTENT, entite.getContent());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,entite.isVisible());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_DATE, entite.getDate());
		
		maBDD.update(DBOpenHelper.ANNONCE_TABLE, contentValues,	
			     DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
			     new String[] { String.valueOf(entite.getId()) });
		
	}

	public void Delete(int id) {
		maBDD.delete(DBOpenHelper.ANNONCE_TABLE,
		         DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
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
			 
			Annonce annonce = ConvertCursorToObject(c);
			 
			liste.add(annonce);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}

	public Annonce ConvertCursorToObject(Cursor c) {
		/*Annonce annonce = new Annonce(
		   c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_PRODUIT),
	       c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_QUANTITE));
		   Annonce.setId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_ID));
		   Annonce.setAchete((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_ACHETE) != 0));
				 
 return annonce;*/
		return null;
	}

	public Annonce ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		 
		Annonce annonce = ConvertCursorToObject(c);
			 
		c.close();
		return annonce;
	}

}
