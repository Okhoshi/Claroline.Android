package activity;

import mobile.claroline.R;
import android.app.ActionBar;
import android.os.Bundle;
import app.AppActivity;

public class about_us extends AppActivity {

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        
        // permet de retourner sur la vue précédente
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void onRepositoryRefresh(String type) {
		// Ignore
	}

}
