package fragments;

import android.support.v4.app.DialogFragment;
import app.AppActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;

public abstract class DetailFragment extends DialogFragment {

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
	public abstract void refresh(AsyncHttpResponseHandler handler);

	/**
	 * Refreshes the UI.
	 */
	public abstract void refreshUI();

	protected void setTitle(final String title, final String subTitle) {
		if (getShowsDialog()) {
			getDialog().setTitle(title);
		} else {
			((AppActivity) getActivity()).setTitle(title, subTitle);
		}
	}

}