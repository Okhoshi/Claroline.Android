package fragments;

import net.claroline.mobile.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import app.App;

import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.ClarolineClient;
import connectivity.ClarolineClient.OnAccountStateChangedListener;
import connectivity.ClarolineService;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginDialog extends Dialog implements
		OnAccountStateChangedListener {

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(final Void... params) {
			Editor edit = App.getPrefs().edit();
			edit.putString(App.SETTINGS_USER_LOGIN, mLogin)
					.putString(App.SETTINGS_USER_PASSWORD, mPassword).apply();

			ClarolineClient.getInstance().connect(
					new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(final Throwable error,
								final String content) {
							synchronized (mAuthTask) {
								mAuthTask.notify();
							}

							((Activity) getContext())
									.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											showMessage(error.getMessage());
										}
									});

						}

						@Override
						public void onSuccess(final String content) {
							synchronized (mAuthTask) {
								mAuthTask.notify();
							}
						}
					});

			try {
				synchronized (mAuthTask) {
					mAuthTask.wait();
				}
			} catch (InterruptedException e) {
				mLoginView.setError(mContext
						.getString(R.string.error_occured_retry));
				e.printStackTrace();
			}
			return ClarolineClient.isValidAccount();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;

			if (success) {
				new ClarolineService()
						.checkForModuleValidity(new AsyncHttpResponseHandler() {
							@Override
							public void onFailure(final Throwable error,
									final String content) {
								showMessage(getContext().getString(
										R.string.pref_error_server_error,
										error.getLocalizedMessage()));
							}

							@Override
							public void onSuccess(final String content) {
								JSONObject object;
								try {
									object = new JSONObject(content);
									if (object.has("version")) {
										int version = object.getInt("version");
										if (version >= App.MIN_VERSION) {
											App.getPrefs()
													.edit()
													.putString(
															App.SETTINGS_PLATFORM_MODULE,
															object.getString("path"))
													.apply();
											dismiss();
										} else {
											showMessage(R.string.error_module_bad_version);
										}
									} else {
										showMessage(R.string.error_module_missing);
									}
								} catch (JSONException e) {
									e.printStackTrace();
									showMessage(R.string.error_module_missing);
								}
							}
						});
			} else {
				showProgress(false);

				Editor edit = App.getPrefs().edit();
				edit.putString(App.SETTINGS_USER_LOGIN, "")
						.putString(App.SETTINGS_USER_PASSWORD, "").apply();

				mPasswordView.setError(mContext
						.getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}
	}

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	/**
	 * The current context.
	 */
	private Context mContext;

	/**
	 * User login for login attempt.
	 */
	private String mLogin;

	/**
	 * UI references.
	 */
	private View mLoginFormView;

	/**
	 * UI references.
	 */
	private TextView mLoginStatusMessageView;

	/**
	 * UI references.
	 */
	private ProgressBar mLoginStatusProgressView;

	/**
	 * UI references.
	 */
	private View mLoginStatusView;

	/**
	 * UI references.
	 */
	private EditText mLoginView;

	/**
	 * User password for login attempt.
	 */
	private String mPassword;
	/**
	 * UI references.
	 */
	private EditText mPasswordView;

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            the current context
	 */
	public LoginDialog(final Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}
		// Reset errors.
		mLoginView.setError(null);
		mPasswordView.setError(null);
		// Store values at the time of the login attempt.
		mLogin = mLoginView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(mContext
					.getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid email address.
		if (TextUtils.isEmpty(mLogin)) {
			mLoginView.setError(mContext
					.getString(R.string.error_field_required));
			focusView = mLoginView;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	@Override
	public void onAccountStateChange(final boolean newValidity) {
		if (newValidity) {
			dismiss();
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.title_activity_login);
		setContentView(R.layout.dialog_login);
		mLogin = App.getPrefs().getString(App.SETTINGS_USER_LOGIN, null);

		// Set up the login form.
		mLoginView = (EditText) findViewById(R.id.login_text);
		if (mLogin != null) {
			mLoginView.setText(mLogin);
		}

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(final TextView textView,
							final int id, final KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		mLoginStatusProgressView = (ProgressBar) findViewById(R.id.login_status_progressbar);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public void show() {
		if (ClarolineClient.isValidAccount()
				|| App.getPrefs().getString(App.SETTINGS_PLATFORM_HOST, "")
						.equals("")) {
			dismiss();
		} else {
			super.show();
		}
	}

	/**
	 * Shows a message on the popup.
	 * 
	 * @param messageId
	 *            the id of the message to show
	 */
	private void showMessage(final int messageId) {
		showProgress(true);
		mLoginStatusProgressView.setVisibility(View.GONE);
		mLoginStatusMessageView.setText(messageId);
	}

	/**
	 * Shows a message on the popup.
	 * 
	 * @param message
	 *            the message to show
	 */
	private void showMessage(final String message) {
		showProgress(true);
		mLoginStatusProgressView.setVisibility(View.GONE);
		mLoginStatusMessageView.setText(message);
	}

	/**
	 * Shows the progress UI and hides the login form.
	 * 
	 * @param show
	 *            <code>true</code> if the progress view must be shown,
	 *            <code>false</code> if it must be hided
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// Revert changes made by showMessage
		mLoginStatusProgressView.setVisibility(View.VISIBLE);
		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);

		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = mContext.getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(final Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(final Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}
