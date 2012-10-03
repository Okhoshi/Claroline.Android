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

import model.Documents;
//import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DocumentsRepository extends Repository<Documents> {

	public static final String REPO_TYPE = "Documents";

	public DocumentsRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List<Documents> getAll() {
		return GetAll();
	}

	public Documents getById(int id) {
		return GetById(id);
	}

	public void save(Documents entite) {
		Save(entite);
	}

	public void update(Documents entite) {
		Update(entite);
	}

	public void delete(int id) {
		Delete(id);
	}

	public List<Documents> convertCursorToListObject(Cursor c) {
		return ConvertCursorToListObject(c);
	}

	public Documents convertCursorToObject(Cursor c) {
		return ConvertCursorToObject(c);
	}

	/*
	 * Static methods of DocumentsRepository
	 */

	public static List<Documents> GetAll() {
		// Récupération de la liste des documents
		Cursor cursor = maBDD.query(DBOpenHelper.DOCUMENTS_TABLE,
				new String[] {  DBOpenHelper.DOCUMENTS_COLUMN_ID ,
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID ,
				DBOpenHelper.DOCUMENTS_COLUMN_DATE ,
				DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION ,
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION ,
				DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER ,
				DBOpenHelper.DOCUMENTS_COLUMN_NAME ,
				DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED ,
				DBOpenHelper.DOCUMENTS_COLUMN_PATH ,
				DBOpenHelper.DOCUMENTS_COLUMN_SIZE ,
				DBOpenHelper.DOCUMENTS_COLUMN_UPDATED ,
				DBOpenHelper.DOCUMENTS_COLUMN_URL ,
				DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY  }, null, null, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}

	public static Documents GetById(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.DOCUMENTS_TABLE,
				new String[] {  DBOpenHelper.DOCUMENTS_COLUMN_ID ,
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID ,
				DBOpenHelper.DOCUMENTS_COLUMN_DATE ,
				DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION ,
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION ,
				DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER ,
				DBOpenHelper.DOCUMENTS_COLUMN_NAME ,
				DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED ,
				DBOpenHelper.DOCUMENTS_COLUMN_PATH ,
				DBOpenHelper.DOCUMENTS_COLUMN_SIZE ,
				DBOpenHelper.DOCUMENTS_COLUMN_UPDATED ,
				DBOpenHelper.DOCUMENTS_COLUMN_URL ,
				DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY  },
				DBOpenHelper.DOCUMENTS_COLUMN_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null);

		Documents documents;
		if(cursor.moveToFirst()){
			documents = ConvertCursorToObject(cursor);
		} else {
			documents = null;
		}
		cursor.close();
		return documents;
	}

	/*
	 * Static methods of DocumentsRepository
	 */

	public static List<Documents> GetAllByPath(String path, int coursId) {
		Cursor cursor = maBDD.query(DBOpenHelper.DOCUMENTS_TABLE,
				new String[] {  DBOpenHelper.DOCUMENTS_COLUMN_ID ,
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID ,
				DBOpenHelper.DOCUMENTS_COLUMN_DATE ,
				DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION ,
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION ,
				DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER ,
				DBOpenHelper.DOCUMENTS_COLUMN_NAME ,
				DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED ,
				DBOpenHelper.DOCUMENTS_COLUMN_PATH ,
				DBOpenHelper.DOCUMENTS_COLUMN_SIZE ,
				DBOpenHelper.DOCUMENTS_COLUMN_UPDATED ,
				DBOpenHelper.DOCUMENTS_COLUMN_URL ,
				DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY  },
				DBOpenHelper.DOCUMENTS_COLUMN_PATH + " = ? AND " + 
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID + "=?",
								new String[] {path, String.valueOf(coursId)}, null, null, null);
		return ConvertCursorToListObject(cursor);
	}

	public static int Save(Documents entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_COURSID,entite.getCours().getId());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_DATE, (new SimpleDateFormat("E MMM y dd HH:mm:ss")).format(entite.getDate())); 
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION,entite.getDescription());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION, entite.getExtension());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER,entite.isFolder());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_NAME, entite.getName());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_PATH, entite.getPath());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_SIZE, entite.getSize());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_URL, entite.getUrl());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY, entite.isVisible());

		int id = (int) maBDD.insert(DBOpenHelper.DOCUMENTS_TABLE, null, contentValues);
		RefreshRepository(REPO_TYPE);
		return id;
	}

	public static void Update(Documents entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_COURSID,entite.getCours().getId());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_DATE, (new SimpleDateFormat("E MMM y dd HH:mm:ss")).format(entite.getDate())); 
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION,entite.getDescription());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION, entite.getExtension());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER,entite.isFolder());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_NAME, entite.getName());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED,entite.isNotified());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_PATH, entite.getPath());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_SIZE, entite.getSize());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_UPDATED, entite.isUpdated());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_URL, entite.getUrl());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY, entite.isVisible());

		maBDD.update(DBOpenHelper.DOCUMENTS_TABLE, contentValues,	
				DBOpenHelper.DOCUMENTS_COLUMN_ID + "=?",
				new String[] { String.valueOf(entite.getId()) });
		RefreshRepository(REPO_TYPE);
	}

	public static Documents GetWithoutId(Documents entite) {
		String[] contentValues = new String[] {  DBOpenHelper.DOCUMENTS_COLUMN_ID ,
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID ,
				DBOpenHelper.DOCUMENTS_COLUMN_DATE ,
				DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION ,
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION ,
				DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER ,
				DBOpenHelper.DOCUMENTS_COLUMN_NAME ,
				DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED ,
				DBOpenHelper.DOCUMENTS_COLUMN_PATH ,
				DBOpenHelper.DOCUMENTS_COLUMN_SIZE ,
				DBOpenHelper.DOCUMENTS_COLUMN_UPDATED ,
				DBOpenHelper.DOCUMENTS_COLUMN_URL ,
				DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY  };

		Cursor c = maBDD.query(DBOpenHelper.DOCUMENTS_TABLE, contentValues,	
				DBOpenHelper.DOCUMENTS_COLUMN_PATH + " LIKE ? AND " + 
						DBOpenHelper.DOCUMENTS_COLUMN_NAME + " LIKE ? AND " + 
						DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION + " LIKE ?",
						new String[] { entite.getPath(), entite.getName(), entite.getExtension() }, null, null, null);
		List<Documents> list = ConvertCursorToListObject(c);
		return list.isEmpty()?null:list.get(0);
	}

	public static void Delete(int id) {
		maBDD.delete(DBOpenHelper.DOCUMENTS_TABLE,
				DBOpenHelper.DOCUMENTS_COLUMN_ID + "=?",
				new String[] { String.valueOf(id) });
		RefreshRepository(REPO_TYPE);
	}

	public static List<Documents> ConvertCursorToListObject(Cursor c) {
		List<Documents> liste = new ArrayList<Documents>();

		// Si la liste est vide
		if (c.getCount() == 0){
			c.close();
			return liste;
		}
		// Pour chaque item
		while (c.moveToNext()){
			Documents documents = ConvertCursorToObject(c);	 
			liste.add(documents);
		}

		// Fermeture du curseur
		c.close();
		return liste;
	}

	public  static Documents ConvertCursorToObject(Cursor c) {
		Documents documents;
		try {
			documents = new Documents(
					CoursRepository.GetById(c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_COURSID)),
					(new SimpleDateFormat("E MMM y dd HH:mm:ss")).parse(c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_DATE)),
					c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_DESCRIPTION),
					c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_EXTENSION),
					c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_NAME),
					c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_PATH),
					c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_URL)												
					);

			documents.setId(c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_ID));
			documents.setSize(c.getDouble(DBOpenHelper.DOCUMENTS_NUM_COLUMN_SIZE));
			documents.setFolder((c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_ISFOLDER) != 0));
			documents.setNotified((c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_NOTIFIED) != 0));
			documents.setUpdated((c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_UPDATED) != 0));
			documents.setVisible((c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_VISIBILITY) != 0));

			return documents;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Documents> GetDocListByCoursId(int coursId) {
		// Récupération de la liste des documents
		Cursor cursor = maBDD.query(DBOpenHelper.DOCUMENTS_TABLE,
				new String[] {  DBOpenHelper.DOCUMENTS_COLUMN_ID ,
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID ,
				DBOpenHelper.DOCUMENTS_COLUMN_DATE ,
				DBOpenHelper.DOCUMENTS_COLUMN_DESCRIPTION ,
				DBOpenHelper.DOCUMENTS_COLUMN_EXTENSION ,
				DBOpenHelper.DOCUMENTS_COLUMN_ISFOLDER ,
				DBOpenHelper.DOCUMENTS_COLUMN_NAME ,
				DBOpenHelper.DOCUMENTS_COLUMN_NOTIFIED ,
				DBOpenHelper.DOCUMENTS_COLUMN_PATH ,
				DBOpenHelper.DOCUMENTS_COLUMN_SIZE ,
				DBOpenHelper.DOCUMENTS_COLUMN_UPDATED ,
				DBOpenHelper.DOCUMENTS_COLUMN_URL ,
				DBOpenHelper.DOCUMENTS_COLUMN_VISIBILITY  },
				DBOpenHelper.DOCUMENTS_COLUMN_COURSID + "=?",
						new String[] {String.valueOf(coursId)}, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}
}
