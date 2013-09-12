package fragments;

import android.support.v4.app.Fragment;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class DetailFragment extends Fragment {

	/**
	 * Checks if the resource is expired.
	 * 
	 * @return the expiration state of the resource
	 */
	public abstract boolean isExpired();

	/**
	 * Refreshes the resource.
	 * 
	 * @param handler
	 *            the handler to pass to the service call
	 */
	public abstract void refreshResource(AsyncHttpResponseHandler handler);

	/**
	 * Refreshes the UI.
	 */
	public abstract void refreshUI();

}