package activity;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;
import mobile.claroline.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.e("MO", AllowedOperations.authenticate.name());
    }
}