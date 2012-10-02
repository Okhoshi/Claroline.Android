/**
 * author : Quentin
 */
package connectivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import mobile.claroline.R;
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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
		if(forAuth){
			postMessage = new HttpPost(GlobalApplication.getPreferences().getString("platform_host", "") + "/claroline/auth/login.php");
		} else {
			postMessage = new HttpPost(GlobalApplication.getPreferences().getString("platform_host", "")
					+ GlobalApplication.getPreferences().getString("platform_module", ""));
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

	public ClaroClient makeOperation(Handler handler, AllowedOperations op, int resID){
		return this.makeOperation(handler, op, null, resID);
	}

	public void run(){
		CallbackArgs args;
		String message = null;
		
		switch(op){
		case authenticate:
			args = new CallbackArgs(GlobalApplication.getPreferences().getString("user_login", "qdevos"),
					GlobalApplication.getPreferences().getString("user_password", "elegie24"),
					AllowedOperations.authenticate);
			getSessionCookie(args);
			break;
		case getSingleAnnounce:
			if(resID < 0 || reqCours == null)
				break;
			args = new CallbackArgs(reqCours, resID, op);
			Execute(args);
			break;
		case getCourseToolList:
		case getDocList:
		case getAnnounceList:
		case updateCompleteCourse:
			if(reqCours == null)
				break;
			args = new CallbackArgs(reqCours, op);
			Execute(args);
			break;
		case getCourseList:
		case getUserData:
		case getUpdates:
			args = new CallbackArgs(op);
			Execute(args);
			break;
		case downloadFile:
			if(resID < 0)
				break;
			Documents doc = DocumentsRepository.GetById(resID);
			if(doc != null)
				message = DownloadFile(doc)?GlobalApplication.getInstance().getResources().getString(R.string.download_finished_OK,
												GlobalApplication.getInstance().getResources().getString(R.string.app_name)):
											GlobalApplication.getInstance().getResources().getString(R.string.download_finished_KO);
			break;
		}
		
		// Dismiss the ProgressDialog at the end of the treating thread
		if(handler != null){
			Message msg = new Message();
			msg.what = 0;
			if(message != null){
				msg.arg1 = 1;
				msg.obj = message;
			}
			handler.sendMessage(msg);
		}
		return;
	}

	public void Execute(CallbackArgs args){
		if(!isExpired()){
			if(!getSessionCookie(new CallbackArgs(GlobalApplication.getPreferences().getString("user_login", ""),
					GlobalApplication.getPreferences().getString("user_password", ""),
					AllowedOperations.authenticate))){
				Log.e(this.toString(), "Authentication Failed!");
				return;
			}
		}

		if(args.operation == AllowedOperations.updateCompleteCourse){
			Execute(new CallbackArgs(args.cidReq, AllowedOperations.getCourseToolList));
			Execute(new CallbackArgs(args.cidReq, AllowedOperations.getDocList));
			Execute(new CallbackArgs(args.cidReq, AllowedOperations.getAnnounceList));
			args.cidReq.setIsLoaded(new Date());
			CoursRepository.Update(args.cidReq);
		} else {
			try {
				Log.d("WEB", "Host:" + getClient(false, args).getURI().getHost()
						+ "Path:" + getClient(false, args).getURI().getPath()
						+ " " + EntityUtils.toString(getClient(false, args).getEntity()));
				
				HttpResponse response = client.execute(getClient(false, args), httpContext);
				String _res = EntityUtils.toString(response.getEntity());
				//String _res = readResponse(response);
				Log.i("ClaroClient", "Response:" + _res);
				JSONArray JSONresponse;

				switch(args.operation){
				case getCourseList:
					JSONresponse = new JSONArray(_res);
					for (int i = 0; i < JSONresponse.length(); i++) {
						JSONObject object = JSONresponse.getJSONObject(i);
						JSONCours.fromJSONObject(object).saveInDB();
					}
					break;
				case getCourseToolList:
					JSONresponse = new JSONArray(_res);
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
					JSONresponse = new JSONArray(_res);
					for(int i = 0; i < JSONresponse.length(); i++){
						JSONObject object = JSONresponse.getJSONObject(i);
						JSONAnnonce.fromJSONObject(object).saveInDB();
					}
					break;
				case getDocList:
					JSONresponse = new JSONArray(_res);
					for(int i = 0; i < JSONresponse.length(); i++){
						JSONObject object = JSONresponse.getJSONObject(i);
						JSONDocument.fromJSONObject(object).saveInDB();
					}
					break;
				case getSingleAnnounce:
					JSONresponse = new JSONArray(_res);
					JSONObject object = JSONresponse.getJSONObject(0);
					JSONAnnonce.fromJSONObject(object).saveInDB();
					break;
				case getUpdates:
					if(_res != "[]"){
						JSONObject JSONResp = new JSONObject(_res);
						Iterator<?> iterOnCours = JSONResp.keys();
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
								Iterator<?> iterOnMod = jsonCours.keys();
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
											Iterator<?> iterOnAnn = jsonAnn.keys();
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
											Iterator<?> iterOnDoc = jsonDoc.keys();
											while(iterOnDoc.hasNext()){
												String path = (String) iterOnDoc.next();
												Documents upDoc;
												if((upDoc = DocumentsRepository.GetAllByPath(path, upCours.getId()).get(0)) == null){
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
					JSONObject jsonUser = new JSONObject(_res);
					Editor edit = GlobalApplication.getPreferences().edit();
					edit.putString("firstName", jsonUser.optString("firstName"))
					.putString("lastName", jsonUser.optString("lastName"))
					.putBoolean("isPlatformAdmin", jsonUser.optBoolean("isPlatformAdmin"))
					.putString("NOMA", jsonUser.optString("officialCode"))
					.putString("platformName", jsonUser.optString("platformName"))
					.putString("institutionName", jsonUser.optString("institutionName"))
					.putString("platformTextAnonym", jsonUser.optString("platformTextAnonym"))
					.putString("platformTextAuth", jsonUser.optString("platformTextAuth"))
					.apply();
					break;
				default:
					break;
				}

				response.getEntity().consumeContent();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
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
			Log.i("WEB", "Host:" + getClient(true, args).getURI().getHost()
					+ "Path:" + getClient(true, args).getURI().getPath());
			HttpResponse response = client.execute(getClient(true, args), httpContext);
			boolean empty =  EntityUtils.toString(response.getEntity()).isEmpty();
			if(empty){
				cookieCreation = new Date();
			}
			response.getEntity().consumeContent();
			return empty;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean DownloadFile(Documents doc) {
		try {
			//Exits the function if the storage is not writable!
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				return false;

			File root = Environment.getExternalStorageDirectory();               

			File dir = new File (root.getAbsolutePath() + "/" + GlobalApplication.getInstance().getResources().getString(R.string.app_name) + "/Downloaded_files");
			if(dir.exists()==false) {
				if(dir.mkdirs() == false)
					//Exits if the directory asked cannot be created!
					return false;
			}

			File file = new File(dir, doc.getName() + "." + doc.getExtension());
			

	        //create the new connection
			URL url = new URL("http://" + doc.getUrl());
	        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	      //Do this so that Java.net impl should work
	        List<Cookie> cookList = cookies.getCookies();
	        String requiredCookies = "";
			for (int i = 0; i < cookList.size(); i++) {
	             requiredCookies += cookList.get(i).getName()+"="+cookList.get(i).getValue()+";";
	        }
	        
	        //set up some things on the connection
	        urlConnection.setRequestProperty("Cookie", requiredCookies);
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);

	        //and connect!
	        urlConnection.connect();

		       //this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput = new FileOutputStream(file);

	        //this will be used in reading the data from the Internet
	        InputStream inputStream = urlConnection.getInputStream();

	        //this is the total size of the file
	        int totalSize = urlConnection.getContentLength();	        
	        //variable to store total downloaded bytes
	        int downloadedSize = 0;
	        int iterationSize = 1024;

			if(handler != null){
				Message msg = new Message();
				msg.what = 1;
				msg.obj = "Downloading " + file.getName() + "...";
				msg.arg1 = totalSize;
				msg.arg2 = iterationSize;
				handler.sendMessage(msg);
			}

	        //create a buffer...
	        byte[] buffer = new byte[iterationSize];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card)
	                fileOutput.write(buffer, 0, bufferLength);
	                //add up the size so we know how much is downloaded
	                downloadedSize += bufferLength;
	                
	                //Reports the progress to the UI
	    			if(handler != null){
	    				Message msg = new Message();
	    				msg.what = 2;
	    				msg.arg1 = downloadedSize;
	    				msg.arg2 = iterationSize;
	    				handler.sendMessage(msg);
	    			}
	        }
	        //close the output stream when done
	        fileOutput.close();
	        
	        return true;
	        
		} catch (IOException e) {
			Log.d("DownloadManager", "Error: " + e);
		}
		return false; // Something were wrong if it passes here
	}
}