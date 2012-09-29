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
import dataStorage.CoursRepository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class AnnonceRepository extends Repository<Annonce> {

	public static final String REPO_TYPE = "Annonce";

	public AnnonceRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List<Annonce> getAll() {
		return GetAll();
	}

	public Annonce getById(int id) {
		return GetById(id);
	}

	public void save(Annonce entite) {
		Save(entite);
	}

	public void update(Annonce entite) {
		Update(entite);
	}

	public void delete(int id) {
		Delete(id);
	}

	public List<Annonce> convertCursorToListObject(Cursor c) {
		return ConvertCursorToListObject(c);
	}

	public Annonce convertCursorToObject(Cursor c) {
		return ConvertCursorToObject(c);
	}
	
	/*
	 * Static methods of AnnonceRepository
	 */

	public static List<Annonce> GetAll() {
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

	public static Annonce GetById(int id) {
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
		Annonce annonce;
		if(cursor.moveToFirst()){
			annonce = ConvertCursorToObject(cursor);
		} else {
			annonce = null;
		}
		return annonce;
	}

	public static Annonce GetByRessourceId(int id) {
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
				DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
		Annonce annonce;
		if(cursor.moveToFirst()){
			annonce = ConvertCursorToObject(cursor);
		} else {
			annonce = null;
		}
		return annonce;
	}
	
	public static void Save(Annonce entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_COURSID, entite.getCours().getId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_TITLE,entite.getTitle());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_CONTENT, entite.getContent());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,entite.isVisible());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_DATE, (new SimpleDateFormat("E MMM y dd HH:mm:ss")).format(entite.getDate()));

		maBDD.insert(DBOpenHelper.ANNONCE_TABLE, null, contentValues);
		RefreshRepository(REPO_TYPE);
	}

	public static void Update(Annonce entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_RESSOURCEID,entite.getRessourceId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_COURSID, entite.getCours().getId());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_TITLE,entite.getTitle());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_CONTENT, entite.getContent());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_VISIBILITY,entite.isVisible());
		contentValues.put(DBOpenHelper.ANNONCE_COLUMN_DATE, (new SimpleDateFormat("E MMM y dd HH:mm:ss")).format(entite.getDate()));

		maBDD.update(DBOpenHelper.ANNONCE_TABLE, contentValues,	
				DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
				new String[] { String.valueOf(entite.getId()) });
		RefreshRepository(REPO_TYPE);
	}

	public static void Delete(int id) {
		maBDD.delete(DBOpenHelper.ANNONCE_TABLE,
				DBOpenHelper.ANNONCE_COLUMN_ID + "=?",
				new String[] { String.valueOf(id) });
		RefreshRepository(REPO_TYPE);
	}

	public static List<Annonce> ConvertCursorToListObject(Cursor c) {
		List<Annonce> liste = new ArrayList<Annonce>();

		// Si la liste est vide
		if (c.getCount() == 0){
			c.close();
			return liste;
		}

		// Pour chaque item
		while (c.moveToNext()){
			Annonce annonce = ConvertCursorToObject(c);
			liste.add(annonce);
		}

		// Fermeture du curseur
		c.close();	 
		return liste;
	}

	public static Annonce ConvertCursorToObject(Cursor c) {
		Annonce annonce;
		try {
			annonce = new Annonce(
					CoursRepository.GetById(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_COURSID)),
					(new SimpleDateFormat("E MMM y dd HH:mm:ss")).parse(c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_DATE)),
					c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_TITLE), 
					c.getString(DBOpenHelper.ANNONCE_NUM_COLUMN_CONTENT)
					);

			annonce.setId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_ID));
			annonce.setRessourceId(c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_RESSOURCEID));
			annonce.setNotified((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_NOTIFIED) != 0));
			annonce.setUpdated((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_UPDATED) != 0));
			annonce.setVisible((c.getInt(DBOpenHelper.ANNONCE_NUM_COLUMN_VISIBILITY) != 0));

			return annonce;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Annonce> GetAllAnnoncesByCoursId(int coursId) {
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
}
