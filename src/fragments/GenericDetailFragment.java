package fragments;

import model.ResourceModel;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GenericDetailFragment extends DetailFragment {

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

		return view;
	}

	@Override
	public void refreshResource(final AsyncHttpResponseHandler handler) {
		((AppActivity) getActivity()).getService().getSingleResource(
				mCurrentResource.getList().getCours(),
				mCurrentResource.getList(),
				mCurrentResource.getResourceString(), handler);
	}

	@Override
	public void refreshUI() {
		((AppActivity) getActivity()).setTitle(mCurrentResource.getTitle(),
				mCurrentResource.getList().getCours().getName());
		mTV1.setVisibility(View.GONE);
	}
}
