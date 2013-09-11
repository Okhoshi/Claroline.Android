package fragments;

import java.util.Locale;

import model.Annonce;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class AnnonceDetailFragment extends DetailFragment {

	/**
	 * The current announce.
	 */
	private Annonce mCurrentAnnonce;

	/**
	 * UI references.
	 */
	private TextView mTV1, mTV2;

	@Override
	public boolean isExpired() {
		return mCurrentAnnonce.isExpired();
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater
				.inflate(R.layout.details_annonce, container, false);

		mTV1 = (TextView) view.findViewById(R.id.details_annonce_1);
		mTV2 = (TextView) view.findViewById(R.id.details_annonce_2);

		Bundle extras = getArguments();
		if (extras != null) {
			mCurrentAnnonce = new Select().from(Annonce.class)
					.where("Id = ?", extras.get("resID")).executeSingle();

			refreshUI();
		}

		return view;
	}

	@Override
	public void refreshResource(final AsyncHttpResponseHandler handler) {
		((AppActivity) getActivity()).getService().getSingleResource(
				mCurrentAnnonce.getList().getCours().getSysCode(),
				mCurrentAnnonce.getList().getLabel(), Annonce.class,
				mCurrentAnnonce.getResourceString(), handler);
	}

	@Override
	public void refreshUI() {
		((AppActivity) getActivity()).setTitle(mCurrentAnnonce.getTitle(),
				mCurrentAnnonce.getList().getCours().getName());

		mTV1.setText(mCurrentAnnonce.getDate().toString("E dd/MMM/y",
				Locale.getDefault()));
		mTV2.setText(mCurrentAnnonce.getContent());
	}
}
