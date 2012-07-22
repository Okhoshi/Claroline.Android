/**
 * 
 */
package connectivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import dataStorage.CoursRepository;

import model.Annonce;
import model.Cours;
import model.Notification;
import model.Documents;

/**
 * @author Quentin
 *
 */
public class JSONCours extends Cours {

	/**
	 * @param isLoaded
	 * @param Annonces
	 * @param Documents
	 * @param Notifications
	 * @param officialEmail
	 * @param sysCode
	 * @param title
	 * @param titular
	 */
	public JSONCours(Date isLoaded, List<Annonce> Annonces,
			List<Documents> Documents, List<Notification> Notifications,
			String officialEmail, String sysCode, String title, String titular) {
		super(isLoaded, Annonces, Documents, Notifications, officialEmail,
				sysCode, title, titular);
		// TODO Auto-generated constructor stub
	}
	
	public int saveInDB(){
		CoursRepository repo = new CoursRepository(null);
		if(CoursRepository.GetByCoursId(this.getId()).equals(this)){
			repo.Update(this);
		} else {
			repo.Save(this);
		}
		return this.getId();
	}
	
	public static JSONCours fromJSONObject(JSONObject object){
		JSONCours cours = new JSONCours(new Date(),
							 new ArrayList<Annonce>(), 
							 new ArrayList<Documents>(), 
							 new ArrayList<Notification>(), 
							 object.optString("officialEmail"), 
							 object.optString("sysCode"), 
							 object.optString("title"), 
							 object.optString("titular"));
		cours.setId(object.optInt("cours_id"));
		cours.setNotified(object.optBoolean("notified"));
		return cours;
	}

}
