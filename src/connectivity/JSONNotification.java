package connectivity;

import dataStorage.CoursRepository;
import model.Notification;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONNotification extends Notification {

	public JSONNotification(model.Cours Cours, Date date, int ressourceType) {
		super(Cours, date, ressourceType);
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
