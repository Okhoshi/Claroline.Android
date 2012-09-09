/**
 * 
 */
package connectivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;

import model.Annonce;

/**
 * @author Quentin
 *
 */
public class JSONAnnonce extends Annonce {

	/**
	 * @param Cours
	 * @param date
	 * @param title
	 * @param content
	 */
	public JSONAnnonce(model.Cours Cours, Date date, String title,
			String content) {
		super(Cours, date, title, content);
		// TODO Auto-generated constructor stub
	}
	
	public int saveInDB(){
		if(this.equals(AnnonceRepository.GetById(this.getId()))){
			AnnonceRepository.Update(this);
		} else {
			AnnonceRepository.Save(this);
		}
		return this.getId();
	}
	
	public static JSONAnnonce fromJSONObject(JSONObject object) throws ParseException{
		String sysCode = object.optJSONArray("cours").optJSONObject(0).optString("sysCode");
		
		JSONAnnonce annonce = new JSONAnnonce(
				CoursRepository.GetBySysCode(sysCode),
				(new SimpleDateFormat("yyyy-MM-dd")).parse(object.optString("date")),
				object.optString("title"),
				object.optString("content")
				);
		
		annonce.setRessourceId(object.optInt("ressourceId"));
		annonce.setNotified(object.optBoolean("notified"));
		annonce.setVisible(object.optBoolean("visibility"));
		
		return annonce;
	}

}
