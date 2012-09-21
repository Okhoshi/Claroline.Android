package activity;

import java.text.SimpleDateFormat;

import mobile.claroline.R;
import model.Annonce;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.TextView;
import app.AppActivity;
import app.GlobalApplication;
import connectivity.AllowedOperations;
import dataStorage.AnnonceRepository;

public class detailsAnnonce extends AppActivity
{


	
	Annonce currentAnnonce;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_annonce);

		
		Bundle extras = getIntent().getExtras();
	    if (extras != null)

	    {
	        int annID = extras.getInt("annID");
	        currentAnnonce=AnnonceRepository.GetById(annID);
	        TextView t1 = (TextView) findViewById(R.id.details_annonce_tv1);
			TextView t2 = (TextView) findViewById(R.id.details_annonce_tv2);
			TextView t3 = (TextView) findViewById(R.id.details_annonce_tv3);
			TextView t4 = (TextView) findViewById(R.id.details_annonce_tv4);

			t1.setText(currentAnnonce.getCours().getTitle());
			t2.setText(currentAnnonce.getTitle());
			t3.setText((new SimpleDateFormat("E MMM y dd HH:mm:ss")).format(currentAnnonce.getDate()));
			t4.setText(currentAnnonce.getContent());
	        
	    }
	    
		
		

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			GlobalApplication.setProgressIndicator(this, true);
			new Thread(GlobalApplication.getClient().makeOperation(handler,AllowedOperations.getSingleAnnounce, currentAnnonce.getCours(), currentAnnonce.getRessourceId())).start();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onRepositoryRefresh(String type) {
		// TODO Auto-generated method stub

	}
	
	public static Handler handler = new Handler(){
		public void handleMessage(Message mess){
			switch (mess.what) {
			case 0:
				GlobalApplication.setProgressIndicator(false);
				break;
			default:
				break;
			}
		}
	};

}
