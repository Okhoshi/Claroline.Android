package connectivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import dataStorage.CoursRepository;
import dataStorage.NotificationRepository;

import model.Notification;

public class JSONNotification extends Notification {

	public JSONNotification(model.Cours Cours, Date date, int ressourceType) {
		super(Cours, date, ressourceType);
		// TODO Auto-generated constructor stub
	}
	
	public int saveInDb(){
		NotificationRepository repo = new NotificationRepository(null);
		
		if(repo.GetById(this.getId()).equals(this)){
			repo.Update(this);
		} else {
			repo.Save(this);
		}
		
		return this.getId();
	}
	
	public static JSONNotification fromJSONObject(JSONObject object, String sysCode, int ressourceType) throws ParseException{		
		JSONNotification notification = new JSONNotification(CoursRepository.GetBySysCode(sysCode),
															 (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(object.optString("date")),
															 ressourceType);
		notification.setRessourceId(object.optInt("ressource_id"));
		notification.setUpdated(true);
		notification.setNotified(true);
		
		return notification;
		
	}

}
