/**
 * Claroline Mobile - Android
 * 
 * @package     connectivity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package connectivity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;
import org.joda.time.DateTime;

import android.accounts.AuthenticatorException;
import android.util.Base64;
import app.App;

import com.activeandroid.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Claroline Mobile - Android
 * 
 * Claroline specific client.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class ClarolineClient extends AsyncHttpClient {

	/**
	 * Claroline Mobile - Android
	 * 
	 * Listen changes made to the Account state.
	 * 
	 * @author Devos Quentin
	 * @version 1.0
	 */
	public interface OnAccountStateChangedListener {
		/**
		 * @param newValidity
		 *            the new state of account validity
		 */
		void onAccountStateChange(boolean newValidity);
	}

	/**
	 * 
	 */
	private static final int C60 = 60 * 1000;

	/**
	 * @return the Singleton instance
	 */
	public static ClarolineClient getInstance() {
		if (sInstance == null) {
			sInstance = new ClarolineClient();
		}
		return ClarolineClient.sInstance;
	}

	/**
	 * @param module
	 *            The module requested
	 * @param operation
	 *            the method requested
	 * @param params
	 *            depending on the operation: <br />
	 *            - NOMOD/Authenticate requests : username, password <br />
	 *            - USER/getToolList<br />
	 *            - CLXXX/getResourcesList<br />
	 *            resquest : Cours syscode <br />
	 *            -CLXXX/getSingleResource requests : Cours syscode, resStr
	 * @return the {@link RequestParams} constructed
	 */
	public static RequestParams getRequestParams(final String module,
			final SupportedMethods operation, final String... params) {
		RequestParams p = new RequestParams();

		switch (operation) {
		case getSingleResource:
			p.add("resID", params[1]);
		case getResourcesList:
		case getToolList:
			p.add("cidReq", params[0]);
			break;
		default:
			break;
		}

		p.add("method", operation.name());
		p.add("module", module);
		return p;
	}

	/**
	 * @param module
	 *            The module requested
	 * @param operation
	 *            the method requested
	 * @param params
	 *            depending on the operation: <br />
	 *            - NOMOD/Authenticate requests : username, password <br />
	 *            - USER/getToolList<br />
	 *            - CLXXX/getResourcesList<br />
	 *            resquest : Cours syscode <br />
	 *            -CLXXX/getSingleResource requests : Cours syscode, resStr
	 * @return the {@link RequestParams} constructed
	 */
	public static RequestParams getRequestParams(final SupportedModules module,
			final SupportedMethods operation, final String... params) {
		return getRequestParams(module.name(), operation, params);
	}

	/**
	 * @return the current account state
	 */
	public static boolean isValidAccount() {
		return getInstance().mValidAccount;
	}

	/**
	 * 
	 * @param listener
	 *            the {@link OnAccountStateChangedListener} to register
	 */
	public static void registerOnAccountStateChangedListener(
			final OnAccountStateChangedListener listener) {
		if (getInstance().mListeners == null) {
			getInstance().mListeners = new ArrayList<OnAccountStateChangedListener>();
		}

		getInstance().mListeners.add(listener);
	}

	/**
	 * @param listener
	 *            the {@link OnAccountStateChangedListener} to unregister
	 */
	public static void unregisterOnAccountStateChangedListener(
			final OnAccountStateChangedListener listener) {
		if (ClarolineClient.getInstance().mListeners != null) {
			ClarolineClient.getInstance().mListeners.remove(listener);
		}
	}

	/**
	 * {@link CookieStore}.
	 */
	private CookieStore mCookies;

	/**
	 * Expiration date.
	 */
	private DateTime mExpires;

	/**
	 * Registered {@link OnAccountStateChangedListener}.
	 */
	private List<OnAccountStateChangedListener> mListeners;

	/**
	 * State of account.
	 */
	private boolean mValidAccount;

	/**
	 * Debug tag.
	 */
	private static final String TAG = "ClaroClient";

	/*
	 * private boolean downloadFile(final Document doc) { try { // Exits the
	 * function if the storage is not writable! if
	 * (!Environment.getExternalStorageState().equals(
	 * Environment.MEDIA_MOUNTED)) { Log.d("DownloadManager", "Missing SDCard");
	 * return false; }
	 * 
	 * File dir = App.getDownloadFolder(); if (!dir.exists()) { if
	 * (!dir.mkdirs()) { // Exits if the directory asked cannot be created!
	 * Log.d("DownloadManager", "Unable to write on SDCard"); } return false; }
	 * 
	 * File file = new File(dir, doc.getTitle() + "." + doc.getExtension());
	 * 
	 * // create the new connection URL url = new URL("http://" + doc.getURL());
	 * HttpURLConnection urlConnection = (HttpURLConnection) url
	 * .openConnection();
	 * 
	 * // Do this so that Java.net impl should work List<Cookie> cookList =
	 * mCookies.getCookies(); String requiredCookies = ""; for (int i = 0; i <
	 * cookList.size(); i++) { requiredCookies += cookList.get(i).getName() +
	 * "=" + cookList.get(i).getValue() + ";"; }
	 * 
	 * // set up some things on the connection
	 * urlConnection.setRequestProperty("Cookie", requiredCookies);
	 * urlConnection.setRequestMethod("GET"); urlConnection.setDoOutput(true);
	 * 
	 * // and connect! urlConnection.connect();
	 * 
	 * // this will be used to write the downloaded data into the file we //
	 * created FileOutputStream fileOutput = new FileOutputStream(file);
	 * 
	 * // this will be used in reading the data from the Internet InputStream
	 * inputStream = urlConnection.getInputStream();
	 * 
	 * // this is the total size of the file int totalSize =
	 * urlConnection.getContentLength(); // variable to store total downloaded
	 * bytes int downloadedSize = 0; int iterationSize = C1024;
	 * 
	 * if (mHandler != null) { Message msg = new Message(); msg.what =
	 * AppHandler.SET_PROGRESS_DWL; msg.obj = "Downloading " + file.getName() +
	 * "..."; msg.arg1 = totalSize; msg.arg2 = iterationSize;
	 * mHandler.sendMessage(msg); }
	 * 
	 * // create a buffer... byte[] buffer = new byte[iterationSize]; int
	 * bufferLength = 0; // used to store a temporary size of the // buffer
	 * 
	 * // now, read through the input buffer and write the contents to the //
	 * file while ((bufferLength = inputStream.read(buffer)) > 0) { // add the
	 * data in the buffer to the file in the file output // stream (the file on
	 * the sd card) fileOutput.write(buffer, 0, bufferLength); // add up the
	 * size so we know how much is downloaded downloadedSize += bufferLength;
	 * 
	 * // Reports the progress to the UI if (mHandler != null) { Message msg =
	 * new Message(); msg.what = AppHandler.INCREMENT_STATUS; msg.arg1 =
	 * downloadedSize; msg.arg2 = iterationSize; mHandler.sendMessage(msg); } }
	 * // close the output stream when done fileOutput.close();
	 * 
	 * doc.setLoadedDate(DateTime.now()); doc.save(); return true;
	 * 
	 * } catch (IOException e) { Log.d("DownloadManager", "Error: " + e); }
	 * return false; // Something were wrong if it passes here }
	 */

	/**
	 * Singleton instance.
	 */
	private static ClarolineClient sInstance;

	/**
	 * Default constructor without arguments.
	 */
	public ClarolineClient() {
		super();
		setCookieStore(mCookies);
		mExpires = new DateTime(0L);
		mValidAccount = App.getPrefs().getBoolean(
				App.SETTINGS_ACCOUNT_VERIFIED, false);
		setTimeout(C60);
		getHttpClient().getParams().setParameter(
				ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
	}

	/**
	 * Gets the Authentication Cookies.
	 * 
	 * @param handler
	 *            the handler to call when the process is finished
	 */
	public void connect(final AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("login",
				App.getPrefs().getString(App.SETTINGS_USER_LOGIN, ""));
		params.put("password",
				App.getPrefs().getString(App.SETTINGS_USER_PASSWORD, ""));
		params.put("sourceUrl",
				Base64.encodeToString(getUrl("/").getBytes(), Base64.DEFAULT));
		post(getUrl("/claroline/auth/login.php"), params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(final Throwable arg0,
							final String arg1) {
						Log.w(ClarolineClient.TAG, "Authentication failed !");
						invalidateClient();
						super.onFailure(arg0, arg1);
						handler.onFailure(arg0, arg1);
					}

					@Override
					public void onSuccess(final int arg0, final String arg1) {
						onSuccess(arg1);
					}

					@Override
					public void onSuccess(final String response) {
						boolean authenticated = response
								.contains("class=\"userName\"");
						setValidAccount(authenticated);
						if (authenticated) {
							Log.d(ClarolineClient.TAG,
									"Authentication passed !");
							setExpirationTime(DateTime.now().plusHours(2));
							handler.onSuccess(response);
						} else {
							Log.w(ClarolineClient.TAG,
									"Authentication failed !");
							handler.onFailure(new AuthenticatorException(
									"Authentication failed"), response);
						}
					}
				});
	}

	/**
	 * @param append
	 *            the URL portion to append to the base URL (with a / at the
	 *            beginning)
	 * @return the complete URL
	 */
	public String getUrl(final String append) {
		return App.getPrefs().getString(App.SETTINGS_PLATFORM_HOST, "")
				+ append;
	}

	/**
	 * Removes all settings related to the current account.
	 */
	public void invalidateClient() {
		setExpirationTime(new DateTime(0L));
		if (mCookies != null) {
			mCookies.clear();
		}
		setValidAccount(false);
		App.getPrefs().edit().remove(App.SETTINGS_USER_LOGIN)
				.remove(App.SETTINGS_USER_PASSWORD)
				.remove(App.SETTINGS_FIRST_NAME).remove(App.SETTINGS_LAST_NAME)
				.remove(App.SETTINGS_IS_PLATFORM_ADMIN)
				.remove(App.SETTINGS_OFFICIAL_CODE)
				.remove(App.SETTINGS_PLATFORM_NAME)
				.remove(App.SETTINGS_INSTITUTION_NAME)
				.remove(App.SETTINGS_USER_IMAGE).remove(App.SETTINGS_PICTURE)
				.remove(App.SETTINGS_ACCOUNT_VERIFIED).apply();
	}

	/**
	 * Notify the {@link OnAccountStateChangedListener} registered.
	 * 
	 * @param newValidity
	 *            new account state
	 */
	protected void notifyAccountStateListener(final boolean newValidity) {
		if (mListeners != null && mListeners.size() > 0) {
			for (OnAccountStateChangedListener listener : mListeners) {
				listener.onAccountStateChange(newValidity);
			}
		}
	}

	/**
	 * @param parameters
	 *            the RequestParams to use for this query
	 * @param handler
	 *            the {@link AsyncHttpResponseHandler} to execute on the query
	 *            response
	 */
	public void serviceQuery(final RequestParams parameters,
			final JsonHttpResponseHandler handler) {
		if (mExpires.isBeforeNow()) {
			connect(new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(final String response) {
					serviceQuery(parameters, handler);
				}
			});
		} else {
			Log.i("Client", "Query " + parameters);
			post(getUrl(App.getPrefs().getString(App.SETTINGS_PLATFORM_MODULE,
					"/module/MOBILE/")), parameters, handler);
		}
	}

	/**
	 * @param pExpirationTime
	 *            the ExpirationTime to set
	 */
	private void setExpirationTime(final DateTime pExpirationTime) {
		mExpires = pExpirationTime;
	}

	/**
	 * @param isValid
	 *            the Account state to set
	 */
	protected void setValidAccount(final boolean isValid) {
		mValidAccount = isValid;
		App.getPrefs().edit()
				.putBoolean(App.SETTINGS_ACCOUNT_VERIFIED, isValid).apply();
		notifyAccountStateListener(isValid);
	}
}
