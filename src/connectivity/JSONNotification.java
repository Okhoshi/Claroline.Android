package connectivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import app.GlobalApplication;

import dataStorage.CoursRepository;
import dataStorage.NotificationRepository;

import model.Notification;

public class JSONNotification extends Notification {

	public JSONNotification(model.Cours Cours, Date date, int ressourceType) {
		super(Cours, date, ressourceType);
	}
	
	public int saveInDb(){		
		if(this.equals(NotificationRepository.GetById(this.getId()))){
			NotificationRepository.Update(this);
		} else {
			NotificationRepository.Save(this);
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
