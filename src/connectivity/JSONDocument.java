package connectivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;

import model.Documents;

public class JSONDocument extends Documents {

	public JSONDocument(model.Cours Cours, Date date, String Description,
			String Extension, String name, String path, String url) {
		super(Cours, date, Description, Extension, name, path, url);
	}
	
	public int saveInDB(){
		if(this.equals(DocumentsRepository.GetWithoutId(this))){
			DocumentsRepository.Update(this);
		} else {
			this.setId(DocumentsRepository.Save(this));
		}
		
		return this.getId();
	}
	
	public static JSONDocument fromJSONObject(JSONObject object) throws ParseException{
		String sysCode = object.optJSONObject("cours").optString("sysCode");
		
		JSONDocument doc = new JSONDocument(CoursRepository.GetBySysCode(sysCode),
											(new SimpleDateFormat("yyyy-MM-dd")).parse(object.optString("date")), 
											object.optString("description"), 
											object.optString("extension"), 
											object.optString("name"), 
											object.optString("path").replace(object.optBoolean("isFolder")?
																			object.optString("name") :
																			object.optString("name") + "." + object.optString("extension")
																			, ""), 
											object.optString("url"));
		
		doc.setFolder(object.optBoolean("isFolder"));
		doc.setNotified(object.optBoolean("notified"));
		doc.setVisible(object.optBoolean("visibility"));
		
		if(!doc.isFolder()){
			doc.setSize(object.optDouble("size"));
		}
		doc.setUpdated(true);
		
		return doc;
	}

}
