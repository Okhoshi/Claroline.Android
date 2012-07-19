package activity;

import connectivity.AllowedOperations;
import connectivity.ClaroClient;
import mobile.claroline.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;



public class home extends Activity implements OnClickListener,OnTouchListener{
	
	
	
	Button button1;
	Button button2;
	Button button3;
	Button button4;
	Button button5;
	Button button6;
	Button button7;
	Button button8;
	Button button9;
	Button button10;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false); // Mettre true pour pouvoir faire un return vers une ancienne activité !
        
        
        String cours1 = getString(R.string.button_cours1);
        String cours2 = getString(R.string.button_cours2);
        String cours3 = getString(R.string.button_cours3);
        String cours4 = getString(R.string.button_cours4);
        String cours5 = getString(R.string.button_cours5);
        String cours6 = getString(R.string.button_cours6);
        String cours7 = getString(R.string.button_cours7);
        String cours8 = getString(R.string.button_cours8);
        String cours9 = getString(R.string.button_cours9);
        String cours10 = getString(R.string.button_cours10);
        
        
        button1 = (Button) findViewById(R.id.but1);
        button1.setOnClickListener(this);
        button1.setText(cours1);
        button1.setBackgroundColor(Color.BLACK);
        button2 = (Button) findViewById(R.id.but2);
        button2.setOnClickListener(this);
        button2.setText(cours2);
        button2.setBackgroundColor(Color.BLACK);
        button3 = (Button) findViewById(R.id.but3);
        button3.setOnClickListener(this);
        button3.setText(cours3);
        button3.setBackgroundColor(Color.BLACK);
        button4 = (Button) findViewById(R.id.but4);
        button4.setOnClickListener(this);
        button4.setText(cours4);
        button4.setBackgroundColor(Color.BLACK);
        button5 = (Button) findViewById(R.id.but5);
        button5.setOnClickListener(this);
        button5.setText(cours5);
        button5.setBackgroundColor(Color.BLACK);
        button6 = (Button) findViewById(R.id.but6);
        button6.setOnClickListener(this);
        button6.setText(cours6);
        button6.setBackgroundColor(Color.BLACK);
        button7 = (Button) findViewById(R.id.but7);
        button7.setOnClickListener(this);
        button7.setText(cours7);
        button7.setBackgroundColor(Color.BLACK);
        button8 = (Button) findViewById(R.id.but8);
        button8.setOnClickListener(this);
        button8.setText(cours8);
        button8.setBackgroundColor(Color.BLACK);
        button9 = (Button) findViewById(R.id.but9);
        button9.setOnClickListener(this);
        button9.setText(cours9);
        button9.setBackgroundColor(Color.BLACK);
        button10 = (Button) findViewById(R.id.but10);
        button10.setOnClickListener(this);
        button10.setText(cours10);
        button10.setBackgroundColor(Color.BLACK);
        
        Log.e("MO", AllowedOperations.authenticate.name());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
     // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);     
        searchView.setSubmitButtonEnabled(true);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_about:
            // Comportement du bouton "A Propos"
        	Intent monIntent = new Intent(this,about_us.class);
        	startActivity(monIntent);
            return true;
        case R.id.menu_help:
            // Comportement du bouton "Aide"
            return true;
        case R.id.menu_refresh:
            // Comportement du bouton "Rafraichir"
            return true;
        case R.id.menu_search:
            // Comportement du bouton "Recherche"
        	onSearchRequested();
            return true;
        case R.id.menu_settings:
            // Comportement du bouton "Paramètres"
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void onClick(View v) 
    {
    		 
    	// Vérification de la vue cliquée
    		 
    	if(v == button1) 
    	{
    		button1.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°1", Toast.LENGTH_SHORT).show();
    		 
    	}
    	
    	if(v == button2) 
    	{
    		button2.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°2", Toast.LENGTH_SHORT).show();

    	}
    	
    	if(v == button3) 
    	{
    		button3.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°1", Toast.LENGTH_SHORT).show();
    		 
    	}
    	
    	if(v == button4) 
    	{
    		button4.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°2", Toast.LENGTH_SHORT).show();

    	}
    	
    	if(v == button5) 
    	{
    		button5.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°1", Toast.LENGTH_SHORT).show();
    		 
    	}
    	
    	if(v == button6) 
    	{
    		button6.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°2", Toast.LENGTH_SHORT).show();

    	}
    	
    	if(v == button7) 
    	{
    		button7.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°1", Toast.LENGTH_SHORT).show();
    		 
    	}
    	
    	if(v == button8) 
    	{
    		button8.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°2", Toast.LENGTH_SHORT).show();

    	}
    	
    	if(v == button9) 
    	{
    		button9.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°1", Toast.LENGTH_SHORT).show();
    		 
    	}
    	
    	if(v == button10) 
    	{
    		button10.setBackgroundColor(Color.RED); 
    		Toast.makeText(this, "Coucou n°2", Toast.LENGTH_SHORT).show();

    	}
    	
    		 
    }
    

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
    
}