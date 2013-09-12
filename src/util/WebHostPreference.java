/**
 * Claroline Mobile - Android
 * 
 * @package     util
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package util;

import net.claroline.mobile.android.R;
import activity.Settings;
import activity.UrlBrowserSelectorActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import app.AppPreferenceActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.ClarolineService;

/**
 * Claroline Mobile - Android
 * 
 * Custom {@link EditTextPreference} class for the Platform Host setting.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class WebHostPreference extends EditTextPreference implements
		TextWatcher {

	/**
	 * Validation listener.
	 */
	private View.OnClickListener mValidateOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(final View v) {
			validateHost(getEditText().getText().toString());
		}
	};

	/**
	 * Ok listener.
	 */
	private View.OnClickListener mOkOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(final View v) {
			onDialogClosed(true);
			getDialog().dismiss();
		}
	};

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            the current context
	 */
	public WebHostPreference(final Context context) {
		super(context);
	}

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            the current context
	 * @param attrs
	 *            the attributes to apply
	 */
	public WebHostPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Default constructor.
	 * 
	 * @param context
	 *            the current context
	 * @param attrs
	 *            the attributes to apply
	 * @param defStyle
	 *            the default style
	 */
	public WebHostPreference(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void afterTextChanged(final Editable editable) {
		String newValue = editable.toString();
		if (!newValue.startsWith("http://") && !newValue.startsWith("https://")) {
			getEditText().setError(
					getContext().getText(R.string.pref_error_invalid_adress));
		}
	}

	@Override
	public void beforeTextChanged(final CharSequence arg0, final int arg1,
			final int arg2, final int arg3) {
		if (getEditText() != null) {
			getEditText().setError(null);
			getEditText().setCompoundDrawables(null, null, null, null);
		}

		if (getDialog() != null) {
			Button button = ((AlertDialog) getDialog())
					.getButton(DialogInterface.BUTTON_POSITIVE);
			if (button != null) {
				button.setText(R.string.pref_validate_host);
				button.setOnClickListener(mValidateOnClickListener);
			}
		}
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {

		builder.setNeutralButton(R.string.browse, new OnClickListener() {

			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				Intent data = new Intent(getContext(),
						UrlBrowserSelectorActivity.class);
				data.putExtra(UrlBrowserSelectorActivity.EXTRA_URL,
						getEditText().getText().toString());
				((AppPreferenceActivity) getContext()).startActivityForResult(
						data, Settings.REQUEST_URL_BROWSER);
			}
		});

		super.onPrepareDialogBuilder(builder);
	}

	@Override
	public void onTextChanged(final CharSequence s, final int start,
			final int before, final int count) {
		// Ignore
	}

	@Override
	protected void showDialog(final Bundle state) {
		super.showDialog(state);

		Button button = ((AlertDialog) getDialog())
				.getButton(DialogInterface.BUTTON_POSITIVE);
		if (button != null) {
			button.setText(R.string.pref_validate_host);
			button.setOnClickListener(mValidateOnClickListener);
		}
		getEditText().addTextChangedListener(this);
	}

	/**
	 * Validates the host proposed.
	 * 
	 * @param newValue
	 *            the new host url
	 */
	private void validateHost(final String newValue) {
		if (getDialog() != null) {
			((AppPreferenceActivity) getContext()).setProgressIndicator(true);

			new ClarolineService().checkHostValidity(newValue,
					new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(final Throwable error,
								final String content) {
							if (getEditText() != null) {
								String textError = getContext().getString(
										R.string.pref_error_server_error,
										error.getMessage());
								getEditText().setError(textError);
							}
						}

						@Override
						public void onFinish() {
							((AppPreferenceActivity) getContext())
									.setProgressIndicator(false);
						}

						@Override
						public void onSuccess(final String content) {
							boolean result = Boolean.parseBoolean(content);
							if (result) {
								getEditText().setError(null);
								Drawable icon = getContext()
										.getResources()
										.getDrawable(
												R.drawable.indicator_input_valid);
								getEditText()
										.setCompoundDrawablesWithIntrinsicBounds(
												null, null, icon, null);
								Button button = ((AlertDialog) getDialog())
										.getButton(DialogInterface.BUTTON_POSITIVE);
								if (button != null) {
									button.setText(android.R.string.ok);
									button.setOnClickListener(mOkOnClickListener);
								}
							} else {
								if (getEditText() != null) {
									CharSequence error = getContext()
											.getText(
													R.string.pref_error_no_claro_platform);
									getEditText().setError(error);
								}
							}
						}
					});
		}
	}
}
