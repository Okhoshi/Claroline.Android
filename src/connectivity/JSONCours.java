/**
 * 
 */
package connectivity;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import dataStorage.CoursRepository;
import dataStorage.AnnonceRepository;
import dataStorage.DocumentsRepository;
import dataStorage.NotificationRepository;

import app.GlobalApplication;

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
							 (new AnnonceRepository(null)).GetAllAnnoncesByCoursId(object.optInt("cours_id")), 
							 (new DocumentsRepository(null)).GetDocListByCoursId(object.optInt("cours_id")), 
							 (new NotificationRepository(null)).GetAllNotificationsByCoursId(object.optInt("cours_id")), 
							 object.optString("officialEmail"), 
							 object.optString("sysCode"), 
							 object.optString("title"), 
							 object.optString("titular"));
		cours.setId(object.optInt("cours_id"));
		cours.setNotified(object.optBoolean("notified"));
		return cours;
	}

}
