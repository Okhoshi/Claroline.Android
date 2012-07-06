package connectivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

public class CallbackArgs {
	
	public String cidReq, login, password;
	public int resId;
	public AllowedOperations operation;
	public UrlEncodedFormEntity entity;
	
	public CallbackArgs(String login,String password,String cidReq, int resId, AllowedOperations operation) throws UnsupportedEncodingException{
		this.login = login;
		this.password = password;
		this.cidReq = cidReq;
		this.operation = operation;
		this.resId = resId;
		List<NameValuePair> args = new ArrayList<NameValuePair>();
		
		switch (operation) {
		case authenticate:
			args.add(new BasicNameValuePair("login", login));
			args.add(new BasicNameValuePair("password", password));
			break;
		case getSingleAnnounce:
			args.add(new BasicNameValuePair("resId",resId + ""));
		case getAnnounceList:
		case getCourseToolList:
		case getDocList:
			args.add(new BasicNameValuePair("cidReq",cidReq));
		case getCourseList:
		case getUpdates:
		case getUserData:
			args.add(new BasicNameValuePair("Method",operation.name()));
			break;
		}
		
		entity = new UrlEncodedFormEntity(args);
	}
}
