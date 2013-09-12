/**
 * Claroline Mobile - Android
 * 
 * @package     activity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package activity;

import net.claroline.mobile.android.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.AppActivity;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description HERE.
 * 
 * @author Devos Quentin
 * @version 1.0
 * 
 */
public class UrlBrowserSelectorActivity extends AppActivity {

	/**
	 * Claroline Mobile - Android
	 * 
	 * TODO Description HERE.
	 * 
	 * @author Devos Quentin
	 * @version 1.0
	 * 
	 */
	public class HostWebViewClient extends WebViewClient {

		/**
		 * Numeric constant.
		 */
		private static final int C100 = 100;

		@Override
		public void onPageFinished(final WebView view, final String url) {
			mURL = url;
			mET.setText(url);
			setProgressIndicator(false);

			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(final WebView view, final String url,
				final Bitmap favicon) {
			setProgressIndicator(true, false, C100);
			super.onPageStarted(view, url, favicon);
		}

	}

	/**
	 * Intent Extra key.
	 */
	public static final String EXTRA_URL = "url";

	/**
	 * UI reference.
	 */
	private Button mB1;

	/**
	 * UI reference.
	 */
	private Button mB2;

	/**
	 * UI reference.
	 */
	private EditText mET;

	/**
	 * Current URL.
	 */
	private String mURL;

	/**
	 * Host Validity handler.
	 */
	private AsyncHttpResponseHandler mValidityHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onFailure(final Throwable error, final String content) {
			Toast.makeText(UrlBrowserSelectorActivity.this, error.getMessage(),
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFinish() {
			setProgressIndicator(false);
		}

		@Override
		public void onSuccess(final String content) {
			if (Boolean.parseBoolean(content)) {
				Intent data = new Intent();
				data.putExtra(EXTRA_URL, mURL);
				setResult(RESULT_OK, data);
				finish();
			} else {
				Toast.makeText(UrlBrowserSelectorActivity.this,
						getString(R.string.pref_error_no_claro_platform),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**
	 * UI reference.
	 */
	private WebView mWV;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webhost_activity);

		mET = (EditText) findViewById(R.id.webhost_address_bar);
		mB1 = (Button) findViewById(R.id.webhost_button_go);
		mB2 = (Button) findViewById(R.id.webhost_button_here);
		mWV = (WebView) findViewById(R.id.webView1);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			mURL = extras.getString(EXTRA_URL);
		}

		mWV.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(final WebView view,
					final int newProgress) {
				incrementProgress(newProgress);
			}
		});

		mWV.setWebViewClient(new HostWebViewClient());

		mB1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				mURL = mET.getText().toString();
				refreshUI();
			}
		});

		mB2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				getService().checkHostValidity(mURL, mValidityHandler);
			}
		});

		refreshUI();
	}

	@Override
	public void refreshUI() {
		if (mURL != null) {
			mET.setText(mURL);
			mWV.loadUrl(mURL);
		}
	}

}
