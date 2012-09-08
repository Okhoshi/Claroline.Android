/**
 * 
 */
package connectivity;

import java.util.Date;
import java.util.List;

import model.Annonce;
import model.Cours;
import model.Documents;
import model.Notification;

import org.json.JSONObject;

import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;
import dataStorage.NotificationRepository;

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
							 AnnonceRepository.GetAllAnnoncesByCoursId(object.optInt("cours_id")), 
							 DocumentsRepository.GetDocListByCoursId(object.optInt("cours_id")), 
							 NotificationRepository.GetAllNotificationsByCoursId(object.optInt("cours_id")), 
							 object.optString("officialEmail"), 
							 object.optString("sysCode"), 
							 object.optString("title"), 
							 object.optString("titular"));
		cours.setId(object.optInt("cours_id"));
		cours.setNotified(object.optBoolean("notified"));
		return cours;
	}

}
