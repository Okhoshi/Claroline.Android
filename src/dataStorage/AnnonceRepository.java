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

import model.Annonce;
import dataStorage.CoursRepository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AnnonceRepository extends Repository<Annonce> {

	public AnnonceRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List<Annonce> GetAll() {
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

	public List<Annonce> GetAllAnnoncesByCoursId(int coursId) {
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
			                        DBOpenHelper.ANNONCE_COLUMN_DATE  },
			        DBOpenHelper.ANNONCE_COLUMN_COURSID + "=?", 
			        new String[] {String.valueOf(coursId)}, null, null, null);			 
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
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_COURSID, entite.getCours().getId()); // Changeons nous le type du return du getCours() en String à la place de Cours
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_TITLE,entite.getTitle());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_CONTENT, entite.getContent());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,entite.isVisible());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_DATE, entite.getDate().toString());
			 
		maBDD.insert(DBOpenHelper.ANNONCE_TABLE, null, contentValues);
		
	}

	public void Update(Annonce entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_COURSID, entite.getCours().getId()); // Changeons nous le type du return du getCours() en String à la place de Cours
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_TITLE,entite.getTitle());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_CONTENT, entite.getContent());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,entite.isVisible());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_DATE, entite.getDate().toString());
		
		maBDD.update(DBOpenHelper.ANNONCE_TABLE, contentValues,	
			     DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
			     new String[] { String.valueOf(entite.getId()) });
		
	}

	public void Delete(int id) {
		maBDD.delete(DBOpenHelper.ANNONCE_TABLE,
		         DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
		         new String[] { String.valueOf(id) });
	}

	public List<Annonce> ConvertCursorToListObject(Cursor c) {
		List<Annonce> liste = new ArrayList<Annonce>();
		 
	    // Si la liste est vide
		if (c.getCount() == 0){
			c.close();
		return liste;}
			 
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
		Annonce annonce = new Annonce(
										CoursRepository.GetByCoursId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_COURSID)), //TODO
										new Date(c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_DATE)),
										c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_TITLE), 
										c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_CONTENT)
									  );
		
		   annonce.setId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_ID));
		   annonce.setRessourceId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_RESSOURCEID));
		   annonce.setNotified((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_NOTIFIED) != 0));
		   annonce.setUpdated((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_UPDATED) != 0));
		   annonce.setVisible((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_VISIBILITY) != 0));
				 
		return annonce;
	}

	public Annonce ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
		 
		Annonce annonce = ConvertCursorToObject(c);
			 
		c.close();
		return annonce;
	}

	

	public static Annonce GetByCourseId(int id) {
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
                DBOpenHelper.ANNONCE_COLUMN_COURSID + "=?",
                new String[] { String.valueOf(id) }, null, null, null);
		return AnnonceConvertCursorToObject(cursor);
	}
	
	
	public static Annonce AnnonceConvertCursorToObject(Cursor c) {
		Annonce annonce = new Annonce(
										CoursRepository.GetByCoursId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_COURSID)), //TODO
										new Date(c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_DATE)),
										c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_TITLE), 
										c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_CONTENT)
									  );
		
		   annonce.setId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_ID));
		   annonce.setRessourceId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_RESSOURCEID));
		   annonce.setNotified((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_NOTIFIED) != 0));
		   annonce.setUpdated((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_UPDATED) != 0));
		   annonce.setVisible((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_VISIBILITY) != 0));
				 
		return annonce;
	}
	
	public static List<Annonce> GetAllAnnonces() {
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
		return AnnonceConvertCursorToListObject(cursor);
	}
	
	
	public static List<Annonce> AnnonceConvertCursorToListObject(Cursor c) {
		List<Annonce> liste = new ArrayList<Annonce>();
		 
	    // Si la liste est vide
		if (c.getCount() == 0){
			c.close();
		return liste;}
			 
		// position sur le premier item
		c.moveToFirst();
			 
		// Pour chaque item
		do {
			 
			Annonce annonce = AnnonceConvertCursorToObject(c);
			 
			liste.add(annonce);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}
}
