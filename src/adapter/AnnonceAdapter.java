/**
 * @author Dim
 * @version 1
 * 
 * @description :  An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. 
 * 				   The Adapter provides access to the data items. 
 * 			       The Adapter is also responsible for making a View for each item in the data set. 
 */
package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import model.Annonce;
import net.claroline.mobile.android.R;

import java.text.SimpleDateFormat;
import java.util.List;


public class AnnonceAdapter extends BaseAdapter {
	
	private int RESOURCE = R.layout.two_lines_item;
	private List<Annonce> listeAnnonce;
	private Context context;

	public AnnonceAdapter(Context context, List<Annonce> listeAnnonce) {
		this.listeAnnonce=listeAnnonce;
		this.context=context;
	}
	
	public void setAnnonce(List<Annonce> listeAnnonce)
	{
		this.listeAnnonce=listeAnnonce;
	}

	public int getCount()
	{
		return listeAnnonce.size();
	}
	
	public Annonce getItem(int position)
	{
		return listeAnnonce.get(position);
	}

	public long getItemId(int position)
	{
		return listeAnnonce.get(position).getId();
	}
	
	public Context getContext()
	{
		return this.context;
	}
	
	public View getView(final int position, View view, ViewGroup viewGroup) 
	{
		LinearLayout v = (LinearLayout) view;
		Annonce ann = getItem(position);
		
		if(v==null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(RESOURCE, viewGroup, false);
		}
		
		if(ann != null){
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail_1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail_2 = (TextView) v.findViewById(R.id.detail_2);
			
			if(name != null){
				name.setText(ann.getTitle());
			}
			if(detail_1 != null){
				detail_1.setText((new SimpleDateFormat("E MMM y dd")).format(ann.getDate()));
			}
			if(detail_2 != null){
				detail_2.setText("");
			}
		}
		
		return v;
	}
}
