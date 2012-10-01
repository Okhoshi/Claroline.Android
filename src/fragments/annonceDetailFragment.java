package fragments;

import java.text.SimpleDateFormat;

import mobile.claroline.R;
import model.Annonce;
import dataStorage.AnnonceRepository;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class annonceDetailFragment extends Fragment {

	private Annonce currentAnnonce;

	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView t4;
	
	public Handler refreshList = new Handler(){
		public void handleMessage(Message mess){
	        currentAnnonce=AnnonceRepository.GetById(currentAnnonce.getId());

			t1.setText(currentAnnonce.getCours().getTitle());
			t2.setText(currentAnnonce.getTitle());
			t3.setText((new SimpleDateFormat("E dd/MMM/y")).format(currentAnnonce.getDate()));
			t4.setText(currentAnnonce.getContent());
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.details_annonce, container, false);
		
        t1 = (TextView) view.findViewById(R.id.details_annonce_1);
		t2 = (TextView) view.findViewById(R.id.details_annonce_2);
		t3 = (TextView) view.findViewById(R.id.details_annonce_3);
		t4 = (TextView) view.findViewById(R.id.details_annonce_4);

		Bundle extras = getArguments();
	    if (extras != null)
	    {
	        int annID = extras.getInt("annID");
	        currentAnnonce=AnnonceRepository.GetById(annID);

			t1.setText(currentAnnonce.getCours().getTitle());
			t2.setText(currentAnnonce.getTitle());
			t3.setText((new SimpleDateFormat("E dd/MMM/y")).format(currentAnnonce.getDate()));
			t4.setText(currentAnnonce.getContent());
	    }
	
		return view;
	}
}
