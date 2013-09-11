/**
 * Claroline Mobile - Android
 * 
 * @package     adapter
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package adapter;

import java.util.List;
import java.util.Locale;

import model.Annonce;
import net.claroline.mobile.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Claroline Mobile - Android
 * 
 * The {@link Annonce} adapter.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
public class AnnonceAdapter extends BaseAdapter {
	/**
	 * Resource View to use for line.
	 */
	private static final int RESOURCE = R.layout.two_lines_item;
	private List<Annonce> mListeAnnonce;
	private Context mContext;

	public AnnonceAdapter(final Context context,
			final List<Annonce> listeAnnonce) {
		mListeAnnonce = listeAnnonce;
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		return mListeAnnonce.size();
	}

	@Override
	public Annonce getItem(final int position) {
		return mListeAnnonce.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return mListeAnnonce.get(position).getId();
	}

	@Override
	public View getView(final int position, final View view,
			final ViewGroup viewGroup) {
		LinearLayout v = (LinearLayout) view;
		Annonce ann = getItem(position);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (LinearLayout) inflater.inflate(RESOURCE, viewGroup, false);
		}

		if (ann != null) {
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail2 = (TextView) v.findViewById(R.id.detail_2);

			if (name != null) {
				name.setText(ann.getTitle());
			}
			if (detail1 != null) {
				detail1.setText(ann.getDate().toString("E MMM y dd",
						Locale.getDefault()));
			}
			if (detail2 != null) {
				detail2.setText("");
			}
		}

		return v;
	}

	public void setAnnonce(final List<Annonce> listeAnnonce) {
		mListeAnnonce = listeAnnonce;
		notifyDataSetChanged();
	}
}
