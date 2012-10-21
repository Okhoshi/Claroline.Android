package activity;

import mobile.claroline.R;
import android.app.ActionBar;
import android.os.Bundle;
import android.widget.TextView;
import app.AppActivity;

public class about_us extends AppActivity {

	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        
        TextView version = (TextView) findViewById(R.id.about_us_tv2);
        version.setText(getString(R.string.about_us_version,new Object[]{getString(R.string.version)}));
        
        // permet de retourner sur la vue précédente
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void onRepositoryRefresh(String type) {
		// Ignore
	}

}
