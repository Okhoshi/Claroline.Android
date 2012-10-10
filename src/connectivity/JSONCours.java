/**
 * 
 */
package connectivity;

import java.util.Date;
import model.Cours;
import org.json.JSONObject;

import dataStorage.CoursRepository;

/**
 * @author Quentin
 *
 */
public class JSONCours extends Cours {

	/**
	 * @param isLoaded
	 * @param officialEmail
	 * @param sysCode
	 * @param title
	 * @param titular
	 */
	public JSONCours(Date isLoaded, String officialEmail, String sysCode, String title, String titular) {
		super(isLoaded, officialEmail, sysCode, title, titular);
	}
	
	public int saveInDB(){
		if(((Cours)this).equals(CoursRepository.GetById(this.getId()))){
			CoursRepository.Update(this);
		} else {
			CoursRepository.Save(this);
		}
		return this.getId();
	}
	
	public static JSONCours fromJSONObject(JSONObject object){
		JSONCours cours = new JSONCours(new Date(),
							 object.optString("officialEmail"), 
							 object.optString("sysCode"), 
							 object.optString("title"), 
							 object.optString("titular"));
		cours.setId(object.optInt("cours_id"));
		cours.setNotified(object.optBoolean("notified"));
		cours.setUpdated(true);
		return cours;
	}

}
