package fragments;

import java.text.SimpleDateFormat;
import java.util.Locale;

import model.Annonce;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

public class AnnonceDetailFragment extends Fragment {

	/**
	 * The current annonce.
	 */
	private Annonce mCurrentAnnonce;

	/**
	 * UI references.
	 */
	private TextView mTV1, mTV2, mTV3, mTV4;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater
				.inflate(R.layout.details_annonce, container, false);

		mTV1 = (TextView) view.findViewById(R.id.details_annonce_1);
		mTV2 = (TextView) view.findViewById(R.id.details_annonce_2);
		mTV3 = (TextView) view.findViewById(R.id.details_annonce_3);
		mTV4 = (TextView) view.findViewById(R.id.details_annonce_4);

		Bundle extras = getArguments();
		if (extras != null) {
			mCurrentAnnonce = new Select().from(Annonce.class)
					.where("Id = ?", extras.getInt("annID")).executeSingle();

			refreshUI();
		}

		return view;
	}

	/**
	 * Refreshes the UI.
	 */
	public void refreshUI() {
		mTV1.setText(mCurrentAnnonce.getList().getCours().getName());
		mTV2.setText(mCurrentAnnonce.getTitle());
		mTV3.setText(new SimpleDateFormat("E dd/MMM/y", Locale.getDefault())
				.format(mCurrentAnnonce.getDate()));
		mTV4.setText(mCurrentAnnonce.getContent());
	}
}
