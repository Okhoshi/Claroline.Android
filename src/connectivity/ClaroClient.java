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
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

	private static final String TAG = "ClaroClient";
	private static CookieStore cookies = new BasicCookieStore();
	private static Date cookieCreation = new Date(0);

	private AllowedOperations op = AllowedOperations.authenticate;
	private Cours reqCours = null;
	private int resID = -1;
	private Handler handler = null;

	public ClaroClient(Handler handler, AllowedOperations op, Cours reqCours, int resID){
		this.op = op;
		this.reqCours = reqCours;
		this.resID = resID;
		this.handler = handler;
	}

	public ClaroClient(){
		this(null, null, null, -1);
	}

	private String getResponse(boolean forAuth, CallbackArgs args) throws UnsupportedEncodingException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		BasicHttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookies);

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

		Log.d(TAG, "Host:" + postMessage.getURI().getHost()
				+ "Path:" + postMessage.getURI().getPath()
				+ " " + EntityUtils.toString(postMessage.getEntity()));

		HttpResponse response = client.execute(postMessage, httpContext);

		Log.d(TAG, "Status: " + response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
		String result = null;
		
		switch(response.getStatusLine().getStatusCode()){
		case HttpStatus.SC_ACCEPTED:
		case HttpStatus.SC_OK:
		case HttpStatus.SC_MOVED_TEMPORARILY:
			result =  EntityUtils.toString(response.getEntity());
			break;
		case HttpStatus.SC_FORBIDDEN:
			invalidateClient();
		default:
			throw new NotOKResponseCode(response.getStatusLine().getStatusCode() + " - " + response.getStatusLine().getReasonPhrase());
		}
		response.getEntity().consumeContent();
		Log.d(TAG, "Response:" + result);
		return result;

	}

	public void run(){
		CallbackArgs args;
		String message = null;
		int ressource = -1;

		ConnectivityManager connMgr = (ConnectivityManager) GlobalApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnected()) {
			// Dismiss the ProgressDialog at the end of the treating thread
			if(handler != null){
				handler.sendEmptyMessage(5);
			}
			return;
		} else {
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
				if(doc != null){
					message = DownloadFile(doc)?GlobalApplication.getInstance().getResources().getString(R.string.download_finished_OK,
							Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
							GlobalApplication.getInstance().getResources().getString(R.string.app_name)):
								GlobalApplication.getInstance().getResources().getString(R.string.download_finished_KO);
					ressource = doc.getId(); 
				}
				break;
			}
		}

		// Dismiss the ProgressDialog at the end of the treating thread
		if(handler != null){
			Message msg = new Message();
			msg.what = 0;
			if(message != null){
				msg.arg1 = 1;
				msg.obj = message;
				if(ressource != -1)
					msg.arg2 = ressource;
			}
			handler.sendMessage(msg);
		}
		return;
	}

	public void Execute(CallbackArgs args){
		if(isExpired()){
			if(!getSessionCookie(new CallbackArgs(GlobalApplication.getPreferences().getString("user_login", ""),
					GlobalApplication.getPreferences().getString("user_password", ""),
					AllowedOperations.authenticate))){
				Log.e(TAG, "Authentication Failed!");
				//Reports the failure to the user
				if(handler != null){
					Message msg = new Message();
					msg.what = 3;
					msg.obj = R.string.failure_message;
					handler.sendMessage(msg);
				}
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
				String _res = getResponse(false, args);

				JSONArray JSONresponse;

				switch(args.operation){
				case getCourseList:
					JSONresponse = new JSONArray(_res);
					for (int i = 0; i < JSONresponse.length(); i++) {
						JSONObject object = JSONresponse.getJSONObject(i);
						JSONCours.fromJSONObject(object).saveInDB();
					}
					CoursRepository.CleanNotUpdated();
					break;
				case getCourseToolList:
					JSONObject jsonRes = new JSONObject(_res);
					Cours cours = CoursRepository.GetBySysCode(jsonRes.optString("sysCode"));
					cours.setAnn(jsonRes.optBoolean("isAnn"));
					cours.setAnnNotif(jsonRes.optBoolean("annNotif"));
					cours.setDnL(jsonRes.optBoolean("isDnL"));
					cours.setDnlNotif(jsonRes.optBoolean("dnlNotif"));
					cours.setNotified(true);
					cours.saveInDB();
					break;
				case getAnnounceList:
					JSONresponse = new JSONArray(_res);
					for(int i = 0; i < JSONresponse.length(); i++){
						JSONObject object = JSONresponse.getJSONObject(i);
						JSONAnnonce.fromJSONObject(object).saveInDB();
					}
					AnnonceRepository.CleanNotUpdated(args.cidReq.getId());
					break;
				case getDocList:
					JSONresponse = new JSONArray(_res);
					for(int i = 0; i < JSONresponse.length(); i++){
						JSONObject object = JSONresponse.getJSONObject(i);
						JSONDocument.fromJSONObject(object).saveInDB();
					}
					DocumentsRepository.CleanNotUpdated(args.cidReq.getId());
					break;
				case getSingleAnnounce:
					JSONresponse = new JSONArray(_res);
					JSONObject object = JSONresponse.getJSONObject(0);
					JSONAnnonce.fromJSONObject(object).saveInDB();
					break;
				case getUpdates:
					if(!_res.equals("[]")){
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
												if((upAnn = AnnonceRepository.GetByRessourceId(resID, upCours.getId())) == null){
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

				CoursRepository.ResetTable();
				DocumentsRepository.ResetTable();
				AnnonceRepository.ResetTable();

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

	public static void invalidateClient(){
		cookieCreation = new Date(0);
		cookies.clear();
	}

	public static boolean isExpired(){
		GregorianCalendar temp = new GregorianCalendar();
		temp.setTime(cookieCreation);
		temp.add(Calendar.HOUR_OF_DAY, 2);
		return (new GregorianCalendar()).getTime().after(temp.getTime());
	}

	public boolean getSessionCookie(CallbackArgs args){
		try {
			boolean empty =  getResponse(true, args).isEmpty();
			if(empty){
				cookieCreation = new Date();
			}
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
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				Log.d("DownloadManager", "Missing SDCard");
				return false;
			}

			File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);               

			File dir = new File (root.getAbsolutePath() + "/" + GlobalApplication.getInstance().getResources().getString(R.string.app_name));
			if(dir.exists()==false) {
				if(dir.mkdirs() == false)
					//Exits if the directory asked cannot be created!
					Log.d("DownloadManager", "Unable to write on SDCard");
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

			doc.setLoaded(new Date());
			DocumentsRepository.Update(doc);

			return true;

		} catch (IOException e) {
			Log.d("DownloadManager", "Error: " + e);
		}
		return false; // Something were wrong if it passes here
	}

	class NotOKResponseCode extends IOException{

		public NotOKResponseCode(String string) {
			super(string);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 4367959832676790410L;}
}