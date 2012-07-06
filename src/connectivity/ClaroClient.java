/**
 * 
 */
package connectivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;


/**
 * @author Quentin
 *
 */
/**
 * @author Quentin
 *
 */
public class ClaroClient {
	
	private HttpClient client;
	private HttpContext httpContext;
	
	private Thread backgroundWork;
	
	private CookieStore cookies;
	private Date cookieCreation;
	
	public ClaroClient(){
		
		cookieCreation = new Date(0);
		cookies = new BasicCookieStore();
		
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		client = new DefaultHttpClient();
	}
	
	public HttpPost getClient(boolean forAuth) throws UnsupportedEncodingException{
		HttpPost postMessage;
		if(forAuth){
			postMessage = new HttpPost("http://10.0.2.2/claroline/claroline/auth/login.php");
		} else {
			postMessage = new HttpPost("http://10.0.2.2/claroline/module/MOBILE/");
		}
		postMessage.addHeader("Content-Type", "application/x-www-form-urlencoded");
		HttpClientParams.setRedirecting(postMessage.getParams(), false);
		return postMessage;
	}
	
	public void makeOperation(AllowedOperations op, Object reqCours, int resID){
		CallbackArgs args;
		switch (op) {
		case authenticate:
			
			break;

		default:
			break;
		}
	}
	
	public void makeOperation(AllowedOperations op){
		this.makeOperation(op, null, -1);
	}
	
	public void makeOperation(AllowedOperations op, Object reqCours){
		this.makeOperation(op, reqCours, -1);
	}
	
	public void Execute(){
		try {
			client.execute(getClient(true), httpContext);
			HttpResponse response = client.execute(getClient(false), httpContext);
			InputStream is = response.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(10000000);

            int current = 0;
            while((current = bis.read()) != -1){
                baf.append((byte)current);
            }

            /* Convert the Bytes read to a String. */
            String html = new String(baf.toByteArray());
			Log.e("HTTPRESPONSE", html);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void invalidateClient(){
		cookieCreation = new Date(0);
		cookies.clear();
	}
	
	public boolean isExpired(){
		Date temp = cookieCreation;
		temp.setHours(cookieCreation.getHours()+1);
		return cookieCreation.after(temp);
	}
	
}