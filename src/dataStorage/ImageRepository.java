package dataStorage;

import java.util.ArrayList;
import java.util.List;

import model.Image;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public abstract class ImageRepository extends Repository<Image> {

	public ImageRepository(Context context) {
		sqLiteOpenHelper = new DBOpenHelper(context, null);
	}

	public List<Image> GetAll() {
		// Récupération de la liste des documents
		Cursor cursor = maBDD.query(DBOpenHelper.IMAGE_TABLE,
	                new String[] {  DBOpenHelper.IMAGE_COLUMN_ID ,
									DBOpenHelper.IMAGE_COLUMN_PATH
			                         }, null, null, null, null, null);			 
		return ConvertCursorToListObject(cursor);
	}

	public Image GetById(int id) {
		Cursor cursor = maBDD.query(DBOpenHelper.IMAGE_TABLE,
                new String[] {  DBOpenHelper.IMAGE_COLUMN_ID ,
								DBOpenHelper.IMAGE_COLUMN_PATH ,  },
								DBOpenHelper.IMAGE_COLUMN_ID + "=?",
		        new String[] { String.valueOf(id) }, null, null, null);
return ConvertCursorToObject(cursor);
	}

	public void Save(Image entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.IMAGE_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.IMAGE_COLUMN_PATH, entite.getPath());
		maBDD.insert(DBOpenHelper.IMAGE_TABLE, null, contentValues);
	}

	public void Update(Image entite) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBOpenHelper.IMAGE_COLUMN_ID, entite.getId());
		contentValues.put(DBOpenHelper.IMAGE_COLUMN_PATH, entite.getPath());
		
		maBDD.update(DBOpenHelper.IMAGE_TABLE, contentValues,	
			     DBOpenHelper.IMAGE_COLUMN_ID + "=?",
			     new String[] { String.valueOf(entite.getId()) });
		
	}

	public void Delete(int id) {
		maBDD.delete(DBOpenHelper.IMAGE_TABLE,
			         DBOpenHelper.IMAGE_COLUMN_ID + "=?",
			         new String[] { String.valueOf(id) });		
	}

	public List<Image> ConvertCursorToListObject(Cursor c) {
		List<Image> liste = new ArrayList<Image>();
		 
	    // Si la liste est vide
		if (c.getCount() == 0){
		c.close();
		return liste;}
			 
		// position sur le premier item
		c.moveToFirst();
			 
		// Pour chaque item
		do {
			 
			Image img = ConvertCursorToObject(c);
			 
			liste.add(img);
		   } while (c.moveToNext());
			 
		// Fermeture du curseur
		c.close();
			 
		return liste;
	}

	public Image ConvertCursorToObject(Cursor c) {
		Image img = new Image(c.getString(DBOpenHelper.IMAGE_NUM_COLUMN_PATH));	
		img.setId(c.getInt(DBOpenHelper.IMAGE_NUM_COLUMN_ID));
		return img;
	}

	public Image ConvertCursorToOneObject(Cursor c) {
		c.moveToFirst();
			 
		Image img = ConvertCursorToObject(c);
			 
		c.close();
		return img;
	}


}
