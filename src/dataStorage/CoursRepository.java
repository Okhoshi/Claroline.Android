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
import java.util.List;
import java.util.Locale;

import model.Cours;
//import model.Notification;

import android.content.ContentValues;
import android.database.Cursor;

public class CoursRepository extends Repository<Cours> {

	public static final String REPO_TYPE = "Cours";

	public List<Cours> getAll(){
		return GetAll();
	}

	public Cours getById(int id){
		return GetById(id);
	}

	public void save(Cours entite) {
		Save(entite);
	}

	public void update(Cours entite) {
		Update(entite);
	}

	public void delete(int id) {
		Delete(id);
	}

	public List<Cours> convertCursorToListObject(Cursor c) {
		return ConvertCursorToListObject(c);
	}

	public Cours convertCursorToObject(Cursor c) {
		return ConvertCursorToObject(c);
	}

	/*
	 * Static methods of CoursRepository 
	 */

	public static List<Cours> GetAll() {
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
				DBOpenHelper.COURS_COLUMN_OFFICIALCODE ,
				DBOpenHelper.COURS_COLUMN_SYSCODE ,
				DBOpenHelper.COURS_COLUMN_TITLE ,
				DBOpenHelper.COURS_COLUMN_TITULAR ,
				DBOpenHelper.COURS_COLUMN_UPDATED  }, null, null, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}

	public static Cours GetById(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.COURS_TABLE,
				new String[] {  DBOpenHelper.COURS_COLUMN_ID ,
				DBOpenHelper.COURS_COLUMN_ANNNOTIF ,
				DBOpenHelper.COURS_COLUMN_DNLNOTIF ,
				DBOpenHelper.COURS_COLUMN_ISANN ,
				DBOpenHelper.COURS_COLUMN_ISDNL ,
				DBOpenHelper.COURS_COLUMN_ISLOADED ,
				DBOpenHelper.COURS_COLUMN_NOTIFIED ,
				DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL ,
				DBOpenHelper.COURS_COLUMN_OFFICIALCODE ,
				DBOpenHelper.COURS_COLUMN_SYSCODE ,
				DBOpenHelper.COURS_COLUMN_TITLE ,
				DBOpenHelper.COURS_COLUMN_TITULAR ,
				DBOpenHelper.COURS_COLUMN_UPDATED  }, 
				DBOpenHelper.COURS_COLUMN_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);
		Cours cours;
		if(cursor.moveToFirst()){
			cours = ConvertCursorToObject(cursor);
		} else {
			cours = null;
		}
		cursor.close();
		return cours;
	}

	public static Cours GetBySysCode(String sysCode) {
		Cursor cursor = maBDD.query(DBOpenHelper.COURS_TABLE,
				new String[] {  DBOpenHelper.COURS_COLUMN_ID ,
				DBOpenHelper.COURS_COLUMN_ANNNOTIF ,
				DBOpenHelper.COURS_COLUMN_DNLNOTIF ,
				DBOpenHelper.COURS_COLUMN_ISANN ,
				DBOpenHelper.COURS_COLUMN_ISDNL ,
				DBOpenHelper.COURS_COLUMN_ISLOADED ,
				DBOpenHelper.COURS_COLUMN_NOTIFIED ,
				DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL ,
				DBOpenHelper.COURS_COLUMN_OFFICIALCODE ,
				DBOpenHelper.COURS_COLUMN_SYSCODE ,
				DBOpenHelper.COURS_COLUMN_TITLE ,
				DBOpenHelper.COURS_COLUMN_TITULAR ,
				DBOpenHelper.COURS_COLUMN_UPDATED  }, 
				DBOpenHelper.COURS_COLUMN_SYSCODE + "=?",
						new String[] { sysCode }, null, null, null);
		Cours cours;
		if(cursor.moveToFirst()){
			cours = ConvertCursorToObject(cursor);
		} else {
			cours = null;
		}
		cursor.close();
		return cours;
	}

	public static List<Cours> GetAllCours() {
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
				DBOpenHelper.COURS_COLUMN_OFFICIALCODE ,
				DBOpenHelper.COURS_COLUMN_SYSCODE ,
				DBOpenHelper.COURS_COLUMN_TITLE ,
				DBOpenHelper.COURS_COLUMN_TITULAR ,
				DBOpenHelper.COURS_COLUMN_UPDATED  }, null, null, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}

	public static List<Cours> QueryOnDB(String selection, String[] selectionArgs){
		Cursor cursor = maBDD.query(DBOpenHelper.COURS_TABLE,
				new String[] {  DBOpenHelper.COURS_COLUMN_ID ,
				DBOpenHelper.COURS_COLUMN_ANNNOTIF ,
				DBOpenHelper.COURS_COLUMN_DNLNOTIF ,
				DBOpenHelper.COURS_COLUMN_ISANN ,
				DBOpenHelper.COURS_COLUMN_ISDNL ,
				DBOpenHelper.COURS_COLUMN_ISLOADED ,
				DBOpenHelper.COURS_COLUMN_NOTIFIED ,
				DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL ,
				DBOpenHelper.COURS_COLUMN_OFFICIALCODE ,
				DBOpenHelper.COURS_COLUMN_SYSCODE ,
				DBOpenHelper.COURS_COLUMN_TITLE ,
				DBOpenHelper.COURS_COLUMN_TITULAR ,
				DBOpenHelper.COURS_COLUMN_UPDATED  }, selection, selectionArgs,
				null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	public static void Save(Cours entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.COURS_COLUMN_ANNNOTIF,entite.isAnnNotif());
		contentValues.put(DBOpenHelper.COURS_COLUMN_DNLNOTIF, entite.isDnlNotif()); 
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISANN,entite.isAnn());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISDNL, entite.isDnL());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISLOADED,(new SimpleDateFormat("E MMM y dd HH:mm:ss", Locale.US)).format(entite.getIsLoaded()));
		contentValues.put(DBOpenHelper.COURS_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL,entite.getOfficialEmail());
		contentValues.put(DBOpenHelper.COURS_COLUMN_OFFICIALCODE,entite.getOfficialCode());
		contentValues.put(DBOpenHelper.COURS_COLUMN_SYSCODE, entite.getSysCode());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITLE, entite.getTitle());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITULAR, entite.getTitular());
		contentValues.put(DBOpenHelper.COURS_COLUMN_UPDATED, entite.isUpdated());

		maBDD.insert(DBOpenHelper.COURS_TABLE, null, contentValues);
		RefreshRepository(REPO_TYPE);
	}

	public static void Update(Cours entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.COURS_COLUMN_ANNNOTIF,entite.isAnnNotif());
		contentValues.put(DBOpenHelper.COURS_COLUMN_DNLNOTIF, entite.isDnlNotif()); 
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISANN,entite.isAnn());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISDNL, entite.isDnL());
		contentValues.put(DBOpenHelper.COURS_COLUMN_ISLOADED,(new SimpleDateFormat("E MMM y dd HH:mm:ss", Locale.US)).format(entite.getIsLoaded()));
		contentValues.put(DBOpenHelper.COURS_COLUMN_NOTIFIED, entite.isNotified());
		contentValues.put(DBOpenHelper.COURS_COLUMN_OFFICIALEMAIL,entite.getOfficialEmail());
		contentValues.put(DBOpenHelper.COURS_COLUMN_OFFICIALCODE,entite.getOfficialCode());
		contentValues.put(DBOpenHelper.COURS_COLUMN_SYSCODE, entite.getSysCode());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITLE, entite.getTitle());
		contentValues.put(DBOpenHelper.COURS_COLUMN_TITULAR, entite.getTitular());
		contentValues.put(DBOpenHelper.COURS_COLUMN_UPDATED, entite.isUpdated());

		maBDD.update(DBOpenHelper.COURS_TABLE, contentValues,	
				DBOpenHelper.COURS_COLUMN_ID + "=?",
				new String[] { String.valueOf(entite.getId()) });
		RefreshRepository(REPO_TYPE);

	}

	public static void Delete(int id) {
		DocumentsRepository.CleanTable(DBOpenHelper.DOCUMENTS_COLUMN_COURSID + "=?", new String[] {String.valueOf(id)});
		AnnonceRepository.CleanTable(DBOpenHelper.ANNONCE_COLUMN_COURSID + "=?", new String[] {String.valueOf(id)});
		maBDD.delete(DBOpenHelper.COURS_TABLE,
				DBOpenHelper.COURS_COLUMN_ID + "=?",
				new String[] { String.valueOf(id) });
		RefreshRepository(REPO_TYPE);
	}
	
	public static void ResetTable(){
		ContentValues cv = new ContentValues();
		cv.put(DBOpenHelper.COURS_COLUMN_UPDATED, false);
		maBDD.update(DBOpenHelper.COURS_TABLE, cv, DBOpenHelper.COURS_COLUMN_UPDATED + "=?", new String[]{"1"});
	}

	public static void CleanTable(String selection, String[] selectionArgs){
		maBDD.delete(DBOpenHelper.COURS_TABLE,
				selection,
				selectionArgs);
		RefreshRepository(REPO_TYPE);
	}
	
	public static void CleanNotUpdated(){
		List<Cours> cours = QueryOnDB(DBOpenHelper.COURS_COLUMN_UPDATED + "=?", new String[] {"0"});
		for (Cours _c : cours) {
			Delete(_c.getId());
		}
		ResetTable();
	}

	public static List<Cours> ConvertCursorToListObject(Cursor c) {
		List<Cours> liste = new ArrayList<Cours>();
		// Pour chaque item
		while(c.moveToNext()){
			Cours cours = ConvertCursorToObject(c);	 
			liste.add(cours);
		}

		// Fermeture du curseur
		c.close();

		return liste;
	}

	public static Cours ConvertCursorToObject(Cursor c) {
		Cours cours;
		try {
			cours = new Cours(
					(new SimpleDateFormat("E MMM y dd HH:mm:ss", Locale.US)).parse(c.getString(DBOpenHelper.COURS_NUM_COLUMN_ISLOADED)),
					c.getString(DBOpenHelper.COURS_NUM_COLUMN_OFFICIALEMAIL),
					c.getString(DBOpenHelper.COURS_NUM_COLUMN_SYSCODE),
					c.getString(DBOpenHelper.COURS_NUM_COLUMN_OFFICIALCODE),
					c.getString(DBOpenHelper.COURS_NUM_COLUMN_TITLE),
					c.getString(DBOpenHelper.COURS_NUM_COLUMN_TITULAR)
					);

			cours.setId(c.getInt(DBOpenHelper.COURS_NUM_COLUMN_ID));
			cours.setAnnNotif((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_ANNNOTIF) != 0));
			cours.setDnlNotif((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_DNLNOTIF) != 0));
			cours.setAnn((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_ISANN) 		   != 0));
			cours.setDnL((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_ISDNL) 		   != 0));
			cours.setNotified((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_NOTIFIED) != 0));
			cours.setUpdated((c.getInt(DBOpenHelper.COURS_NUM_COLUMN_UPDATED)   != 0));

			return cours;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
