/**
 * @author Dim
 * @version 1
 * 
 * @description :  An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. 
 * 				   The Adapter provides access to the data items. 
 * 			       The Adapter is also responsible for making a View for each item in the data set. 
 */
package adapter;

import java.util.List;

import model.Cours;
import net.claroline.mobile.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CoursAdapter extends BaseAdapter {

	private Context mContext;
	private List<Cours> mCoursList;

	public CoursAdapter(final Context context, final List<Cours> listeCours) {
		mContext = context;
		mCoursList = listeCours;
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		return mCoursList.size();
	}

	@Override
	public Cours getItem(final int position) {
		return mCoursList.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return mCoursList.get(position).getId();
	}

	@Override
	public View getView(final int position, final View view,
			final ViewGroup parent) {
		Cours cours = getItem(position);
		LinearLayout v = (LinearLayout) view;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(R.layout.cours_two_lines_item,
					parent, false);
		}

		if (cours != null) {
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail2 = (TextView) v.findViewById(R.id.detail_2);

			if (name != null) {
				name.setText(cours.getName());
			}
			if (detail1 != null) {
				detail1.setText(cours.getTitular());
			}
			if (detail2 != null) {
				detail2.setText(cours.getOfficialCode());
			}
		}

		return v;
	}

	public void setCours(final List<Cours> listeCours) {
		mCoursList = listeCours;
	}

}
