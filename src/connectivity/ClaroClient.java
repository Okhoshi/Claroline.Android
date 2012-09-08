/**
 * 
 */
package connectivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import model.Annonce;
import model.Cours;
import model.Documents;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import app.GlobalApplication;
import dataStorage.AnnonceRepository;
import dataStorage.CoursRepository;
import dataStorage.DocumentsRepository;


/**
 * @author Quentin
 *
 */
public class ClaroClient implements Runnable {

	private HttpClient client;
	private HttpContext httpContext;

	private CookieStore cookies;
	private Date cookieCreation;

	private AllowedOperations op = AllowedOperations.authenticate;
	private Cours reqCours = null;
	private int resID = -1;
	private Handler handler = null;

	public ClaroClient(){

		cookieCreation = new Date(0);
		cookies = new BasicCookieStore();

		httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		client = new DefaultHttpClient();
	}

	public HttpPost getClient(boolean forAuth, CallbackArgs args) throws UnsupportedEncodingException{
		HttpPost postMessage;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance().getApplicationContext());
		if(forAuth){
			postMessage = new HttpPost(pref.getString("platform_host", "") + "/claroline/auth/login.php");
		} else {
			postMessage = new HttpPost(pref.getString("platform_host", "") + pref.getString("platform_module", ""));
		}
		postMessage.addHeader("Content-Type", "application/x-www-form-urlencoded");
		postMessage.setEntity(args.entity);
		HttpClientParams.setRedirecting(postMessage.getParams(), false);
		return postMessage;
	}

	public ClaroClient makeOperation(Handler handler, AllowedOperations op, Cours reqCours, int resID){
		this.op = op;
		this.reqCours = reqCours;
		this.resID = resID;
		this.handler = handler;
		return this;
	}

	public ClaroClient makeOperation(Handler handler, AllowedOperations op){
		return this.makeOperation(handler, op, null, -1);
	}

	public ClaroClient makeOperation(Handler handler, AllowedOperations op, Cours reqCours){
		return this.makeOperation(handler, op, reqCours, -1);
	}

	public void run(){
		CallbackArgs args;
		switch(op){
		case authenticate:
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance().getApplicationContext());
			args = new CallbackArgs(pref.getString("user_login", ""), pref.getString("user_password", ""), AllowedOperations.authenticate);
			getSessionCookie(args);
			break;
		case getSingleAnnounce:
			if(resID < 0 || reqCours == null)
				return;
			args = new CallbackArgs(reqCours, resID, op);
			Execute(args);
			break;
		case getCourseToolList:
		case getDocList:
		case getAnnounceList:
		case updateCompleteCourse:
			if(reqCours == null)
				return;
			args = new CallbackArgs(reqCours, op);
			Execute(args);
			break;
		case getCourseList:
		case getUserData:
		case getUpdates:
			args = new CallbackArgs(op);
			Execute(args);
			break;
		}

		Message msg = new Message();
		msg.what = 0;
		msg.obj = "Hello ! It's OK";
		handler.sendMessage(msg);
		return;
	}

	public void Execute(CallbackArgs args){
			if(!isExpired()){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GlobalApplication.getInstance().getApplicationContext());
				if(!getSessionCookie(new CallbackArgs(pref.getString("user_login", ""), pref.getString("user_password", ""), AllowedOperations.authenticate))){
					Log.e(this.toString(), "Authentication Failed!");
					return;
				}
			}

			if(args.operation == AllowedOperations.updateCompleteCourse){
				Execute(new CallbackArgs(args.cidReq,AllowedOperations.getCourseToolList));
				Execute(new CallbackArgs(args.cidReq, AllowedOperations.getDocList));
				Execute(new CallbackArgs(args.cidReq, AllowedOperations.getAnnounceList));
				//TODO update the "isLoaded" property of args.cidReq
			} else {
				try {
					HttpResponse response = client.execute(getClient(false, args), httpContext);
					Log.i(this.toString(), "Status:[" + response.getStatusLine().toString() + "]");
					JSONArray JSONresponse;

					switch(args.operation){
					case getCourseList:
						JSONresponse = new JSONArray(readResponse(response));
						for (int i = 0; i < JSONresponse.length(); i++) {
							JSONObject object = JSONresponse.getJSONObject(i);
							JSONCours.fromJSONObject(object).saveInDB();
						}
						break;
					case getCourseToolList:
						JSONresponse = new JSONArray(readResponse(response));
						for(int i = 0; i < JSONresponse.length(); i++){
							JSONObject object = JSONresponse.getJSONObject(i);
							JSONCours cours = (JSONCours) CoursRepository.GetBySysCode(object.optString("sysCode"));
							cours.setAnn(object.optBoolean("isAnn"));
							cours.setAnnNotif(object.optBoolean("annNotif"));
							cours.setDnL(object.optBoolean("isDnL"));
							cours.setDnlNotif(object.optBoolean("dnlNotif"));
							cours.saveInDB();
						}
						break;
					case getAnnounceList:
						JSONresponse = new JSONArray(readResponse(response));
						for(int i = 0; i < JSONresponse.length(); i++){
							JSONObject object = JSONresponse.getJSONObject(i);
							JSONAnnonce.fromJSONObject(object).saveInDB();
						}
						break;
					case getDocList:
						JSONresponse = new JSONArray(readResponse(response));
						for(int i = 0; i < JSONresponse.length(); i++){
							JSONObject object = JSONresponse.getJSONObject(i);
							JSONDocument.fromJSONObject(object).saveInDB();
						}
						break;
					case getSingleAnnounce:
						JSONresponse = new JSONArray(readResponse(response));
						JSONObject object = JSONresponse.getJSONObject(0);
						JSONAnnonce.fromJSONObject(object).saveInDB();
						break;
					case getUpdates:
						if(readResponse(response) != "[]"){
							JSONObject JSONResp = new JSONObject(readResponse(response));
							Iterator iterOnCours = JSONResp.keys();
							while(iterOnCours.hasNext()){
								Cours upCours;
								String syscode = (String) iterOnCours.next();
								if((upCours = CoursRepository.GetBySysCode(syscode)) == null){
									Execute(new CallbackArgs(AllowedOperations.getCourseList));
									if((upCours = CoursRepository.GetBySysCode(syscode)) != null){
										Execute(new CallbackArgs(upCours, AllowedOperations.updateCompleteCourse));
									}
									continue;
								} else {
									JSONObject jsonCours = JSONResp.getJSONObject(syscode);
									Iterator iterOnMod = jsonCours.keys();
									while(iterOnMod.hasNext()){
										String modKey = (String) iterOnMod.next();
										if(modKey == "CLANN"){
											if(!upCours.isAnn()){
												Execute(new CallbackArgs(upCours, AllowedOperations.getCourseToolList));
												if(upCours.isAnn()){
													Execute(new CallbackArgs(upCours, AllowedOperations.getAnnounceList));
												}
												continue;
											} else {
												JSONObject jsonAnn = jsonCours.getJSONObject(modKey);
												Iterator iterOnAnn = jsonAnn.keys();
												while(iterOnAnn.hasNext()){
													int resID = Integer.parseInt((String) iterOnAnn.next());
													Annonce upAnn;
													if((upAnn = AnnonceRepository.GetByRessourceId(resID)) == null){
														Execute(new CallbackArgs(upCours, AllowedOperations.getAnnounceList));
													} else {
														upAnn.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonAnn.optString(String.valueOf(resID))));
														upAnn.setNotified(true);
														AnnonceRepository.Update(upAnn);
														Execute(new CallbackArgs(upCours, resID, AllowedOperations.getSingleAnnounce));
													}
												}
											}
										}
										else if(modKey == "CLDOC"){
											if(!upCours.isDnL()){
												Execute(new CallbackArgs(upCours, AllowedOperations.getCourseToolList));
												if(upCours.isDnL()){
													Execute(new CallbackArgs(upCours, AllowedOperations.getDocList));
												}
												continue;
											} else {
												JSONObject jsonDoc = jsonCours.getJSONObject(modKey);
												Iterator iterOnDoc = jsonDoc.keys();
												while(iterOnDoc.hasNext()){
													String path = (String) iterOnDoc.next();
													Documents upDoc;
													if((upDoc = DocumentsRepository.GetByPath(path)) == null){
														Execute(new CallbackArgs(upCours, AllowedOperations.getDocList));
													} else {
														upDoc.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(jsonDoc.optString(path)));
														upDoc.setNotified(true);
														DocumentsRepository.Update(upDoc);
													}
												}
											}
										}
									}
								}
							}
						}
						break;
					case getUserData:
						break;
					default:
						break;
					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}

	public void invalidateClient(){
		cookieCreation = new Date(0);
		cookies.clear();
	}

	public boolean isExpired(){
		GregorianCalendar temp = new GregorianCalendar();
		temp.setTime(cookieCreation);
		temp.add(Calendar.HOUR_OF_DAY, 2);
		return cookieCreation.after(temp.getTime());
	}

	public boolean getSessionCookie(CallbackArgs args){
		try {
			Log.i("WEB", getClient(true, args).getURI().getPath());
			HttpResponse response = client.execute(getClient(true, args), httpContext);
			boolean empty =  readResponse(response).isEmpty();
			if(empty){
				cookieCreation = new Date();
			}
			return empty;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String readResponse(HttpResponse response) throws IllegalStateException, IOException{
		InputStream is = response.getEntity().getContent();
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayBuffer baf = new ByteArrayBuffer(10000000);

		int current = 0;
		while((current = bis.read()) != -1){
			baf.append((byte)current);
		}

		/* Convert the Bytes read to a String. */
		return new String(baf.toByteArray());
	}
}