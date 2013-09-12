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

import net.claroline.mobile.android.R;

import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;
import org.joda.time.DateTime;

import android.util.Base64;
import android.util.Log;
import app.App;

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
		if (App.isOnline()) {
			RequestParams params = new RequestParams();
			params.put("login",
					App.getPrefs().getString(App.SETTINGS_USER_LOGIN, ""));
			params.put("password",
					App.getPrefs().getString(App.SETTINGS_USER_PASSWORD, ""));
			params.put("sourceUrl", Base64.encodeToString(getUrl("/")
					.getBytes(), Base64.DEFAULT));
			post(getUrl("/claroline/auth/login.php"), params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(final Throwable arg0,
								final String arg1) {
							Log.w(ClarolineClient.TAG,
									"Authentication failed !");
							invalidateClient(false);
							handler.onFailure(arg0, arg1);
						}

						@Override
						public void onFinish() {
							handler.onFinish();
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
								handler.onFailure(new Throwable(
										"Authentication failed"), response);
							}
						}
					});
		} else {
			handler.onFinish();
			handler.onFailure(
					new Throwable(App.getInstance().getString(
							R.string.no_network)), "");
		}
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
	 * 
	 * @param calledFromApp
	 *            if it is called from App invalidate method
	 */
	public void invalidateClient(final boolean calledFromApp) {
		setExpirationTime(new DateTime(0L));
		if (mCookies != null) {
			mCookies.clear();
		}
		setValidAccount(false);
		if (!calledFromApp) {
			App.invalidateUser(true);
		}
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
		if (App.isOnline()) {
			if (mExpires.isBeforeNow()) {
				connect(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(final String response) {
						serviceQuery(parameters, handler);
					}
				});
			} else {
				Log.d("Client", "Query " + parameters);
				post(getUrl(App.getPrefs().getString(
						App.SETTINGS_PLATFORM_MODULE, "/module/MOBILE/")),
						parameters, handler);
			}
		} else {
			handler.onFinish();
			handler.onFailure(
					new Throwable(App.getInstance().getString(
							R.string.no_network)), "");
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

	/**
	 * Makes a GET request on url with authentication for Claroline.
	 * 
	 * @param url
	 *            the url to request authentified
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void siteCall(final String url,
			final AsyncHttpResponseHandler handler) {
		if (App.isOnline()) {
			if (mExpires.isBeforeNow()) {
				connect(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(final String response) {
						siteCall(url, handler);
					}
				});
			} else {
				Log.d("Client", "Query " + url);
				get(url, handler);
			}
		} else {
			handler.onFinish();
			handler.onFailure(
					new Throwable(App.getInstance().getString(
							R.string.no_network)), "");
		}
	}
}
