package connectivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import app.GlobalApplication;

import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;

import model.Documents;

public class JSONDocument extends Documents {

	public JSONDocument(model.Cours Cours, Date date, String Description,
			String Extension, String name, String path, String url) {
		super(Cours, date, Description, Extension, name, path, url);
		// TODO Auto-generated constructor stub
	}
	
	public int saveInDB(){
		if(this.equals(DocumentsRepository.GetById(this.getId()))){
			DocumentsRepository.Update(this);
		} else {
			DocumentsRepository.Save(this);
		}
		
		return this.getId();
	}
	
	public static JSONDocument fromJSONObject(JSONObject object) throws ParseException{
		String sysCode = object.optJSONArray("cours").optJSONObject(0).optString("sysCode");
		
		JSONDocument doc = new JSONDocument(CoursRepository.GetBySysCode(sysCode),
											(new SimpleDateFormat("yyyy-MM-dd")).parse(object.optString("date")), 
											object.optString("description"), 
											object.optString("extension"), 
											object.optString("name"), 
											object.optString("path"), 
											object.optString("url"));
		
		doc.setNotified(object.optBoolean("notified"));
		doc.setSize(object.optDouble("size"));
		doc.setVisible(object.optBoolean("visibility"));
		
		return doc;
	}

}
