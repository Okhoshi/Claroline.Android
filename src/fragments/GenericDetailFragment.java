package fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ResourceModel;
import net.claroline.mobile.android.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description HERE.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class GenericDetailFragment extends DetailFragment {

	/**
	 * Claroline Mobile - Android
	 * 
	 * TODO Description HERE.
	 * 
	 * @author Devos Quentin
	 * @version 1.0
	 */
	private class GenericWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view,
				final String url) {

			((AppActivity) getActivity()).setProgressIndicator(true);
			((AppActivity) getActivity()).getService().getPageFor(url,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(final String content) {

							Matcher matcher = REGEX.matcher(content);
							String data = matcher.replaceAll(REGEX_REPLACE);
							data = data
									.replace("</head>",
											"<meta name=\"viewport\" content=\"width="
													+ mWV2.getWidth()
													+ "\"/>\n</head>");

							mWV2.loadDataWithBaseURL(url, data, "text/html",
									"utf-8", null);
							((AppActivity) getActivity())
									.setProgressIndicator(false);
						}
					});

			return true;
		}
	}

	/**
	 * Numeric constant.
	 */
	private static final int C100 = 100;

	/**
	 * Regex pattern.
	 */
	private static final Pattern REGEX = Pattern
			.compile(
					"(?:<div id=\"claroPage\">.*?<div id=\"courseRightContent\">(.+?)</div>\\s*?"
							+ "<!-- rightContent -->.*?<!-- end of claroPage -->.*?</div>)",
					Pattern.DOTALL);

	/**
	 * Regex replacement.
	 */
	public static final String REGEX_REPLACE = "<div id=\"claroPage\">$1"
			+ "<!-- end of claroPage --></div>";

	/**
	 * The current resource.
	 */
	private ResourceModel mCurrentResource;

	/**
	 * UI references.
	 */
	private TextView mTV1;

	/**
	 * UI references.
	 */
	private WebView mWV2;

	@Override
	public boolean isExpired() {
		return mCurrentResource.isExpired();
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater
				.inflate(R.layout.details_generic, container, false);

		mTV1 = (TextView) view.findViewById(R.id.details_generic_1);
		mWV2 = (WebView) view.findViewById(R.id.details_generic_2);

		Bundle extras = getArguments();
		if (extras != null) {
			mCurrentResource = new Select().from(ResourceModel.class)
					.where("Id = ?", extras.get("resID")).executeSingle();

			refreshUI();
		}

		mWV2.getSettings().setJavaScriptEnabled(true);

		mWV2.setWebViewClient(new GenericWebViewClient());

		return view;
	}

	@Override
	public void refreshResource(final AsyncHttpResponseHandler handler) {
		((AppActivity) getActivity()).getService().getSingleResource(
				mCurrentResource.getList().getCours().getSysCode(),
				mCurrentResource.getList().getLabel(),
				mCurrentResource.getList().getResourceType(),
				mCurrentResource.getResourceString(), handler);
	}

	@Override
	public void refreshUI() {
		((AppActivity) getActivity()).setTitle(mCurrentResource.getTitle(),
				mCurrentResource.getList().getCours().getName());
		mTV1.setVisibility(View.GONE);
		mWV2.loadUrl(mCurrentResource.getURL());
	}
}
