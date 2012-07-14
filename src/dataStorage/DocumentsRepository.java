package dataStorage;

import java.util.ArrayList;
import java.util.List;

import model.Documents;
//import model.Notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DocumentsRepository extends Repository<Documents> {

	public DocumentsRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List GetAll() {
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

	public Documents GetById(int id) {
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
return ConvertCursorToObject(cursor);
	}

	public void Save(Documents entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_COURSID,entite.getCours());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_DATE, entite.getDate()); 
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
			 
		maBDD.insert(DBOpenHelper.DOCUMENTS_TABLE, null, contentValues);
	}

	public void Update(Documents entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_COURSID,entite.getCours());
		contentValues.put(DBOpenHelper.DOCUMENTS_COLUMN_DATE, entite.getDate()); 
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
		
	}

	public void Delete(int id) {
		maBDD.delete(DBOpenHelper.DOCUMENTS_TABLE,
			         DBOpenHelper.DOCUMENTS_COLUMN_ID + "=?",
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
			 
			Documents documents = ConvertCursorToObject(c);
			 
			liste.add(documents);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}

	public Documents ConvertCursorToObject(Cursor c) {
		/*Documents documents = new Documents(
		   c.getString(DBOpenHelper.DOCUMENTS_NUM_COLUMN_PRODUIT),
	       c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_QUANTITE));
		   documents.setId(c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_ID));
		   documents.setAchete((c.getInt(DBOpenHelper.DOCUMENTS_NUM_COLUMN_ACHETE) != 0));
				 
 return documents;*/
		return null;
	}

	public Documents ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
			 
		Documents documents = ConvertCursorToObject(c);
			 
		c.close();
		return documents;
	}

}
