package fragments;

import java.text.SimpleDateFormat;

import model.Annonce;
import net.claroline.mobile.android.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import dataStorage.AnnonceRepository;
import dataStorage.Repository;

public class annonceDetailFragment extends Fragment {

	private Annonce currentAnnonce;

	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView t4;

	public Handler refreshList = new Handler() {
		@Override
		public void handleMessage(final Message mess) {
			currentAnnonce = AnnonceRepository.GetById(currentAnnonce.getId());

			t1.setText(currentAnnonce.getCours().getTitle());
			t2.setText(currentAnnonce.getTitle());
			t3.setText(new SimpleDateFormat("E dd/MMM/y").format(currentAnnonce
					.getDate()));
			t4.setText(currentAnnonce.getContent());
		}
	};

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater
				.inflate(R.layout.details_annonce, container, false);

		t1 = (TextView) view.findViewById(R.id.details_annonce_1);
		t2 = (TextView) view.findViewById(R.id.details_annonce_2);
		t3 = (TextView) view.findViewById(R.id.details_annonce_3);
		t4 = (TextView) view.findViewById(R.id.details_annonce_4);

		if (!Repository.isOpen()) {
			Repository.Open();
		}

		Bundle extras = getArguments();
		if (extras != null) {
			int annID = extras.getInt("annID");
			currentAnnonce = AnnonceRepository.GetById(annID);

			t1.setText(currentAnnonce.getCours().getTitle());
			t2.setText(currentAnnonce.getTitle());
			t3.setText(new SimpleDateFormat("E dd/MMM/y").format(currentAnnonce
					.getDate()));
			t4.setText(currentAnnonce.getContent());
		}

		return view;
	}
}
