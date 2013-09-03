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

import model.Document;
import net.claroline.mobile.android.R;
import util.Tools;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DocumentsAdapter extends BaseAdapter {

	private static final int RESOURCE = R.layout.doc_item;
	private List<Document> mListeDocuments;
	private Context mContext;

	public DocumentsAdapter(final Context context,
			final List<Document> listeDocuments) {
		mListeDocuments = listeDocuments;
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public int getCount() {
		return mListeDocuments.size();
	}

	@Override
	public Document getItem(final int position) {
		return mListeDocuments.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return mListeDocuments.get(position).getId();
	}

	@Override
	public View getView(final int position, final View view,
			final ViewGroup viewGroup) {
		RelativeLayout v = (RelativeLayout) view;
		Document doc = getItem(position);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = (RelativeLayout) inflater.inflate(RESOURCE, viewGroup, false);
		}

		if (doc != null) {
			TextView name = (TextView) v.findViewById(R.id.name_item);
			TextView detail1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail2 = (TextView) v.findViewById(R.id.detail_2);
			TextView detail3 = (TextView) v.findViewById(R.id.detail_3);
			ImageView img = (ImageView) v.findViewById(R.id.ext_logo);
			TextView syscode = (TextView) v.findViewById(R.id.syscode);

			if (name != null) {
				name.setText(doc.getTitle());
			}
			if (detail1 != null) {
				detail1.setText(doc.getStringSize());
			}
			if (detail2 != null) {
				detail2.setText(doc.getDate().toString("E MMM y dd"));
			}
			if (detail3 != null) {
				detail3.setText(doc.getExtension());
			}
			if (img != null) {
				img.setImageResource(Tools.getResFromExt(doc.getExtension()));
			}
			if (syscode != null) {
				syscode.setVisibility(View.GONE);
			}
		}

		return v;
	}

	public void setDocuments(final List<Document> listeDocuments) {
		mListeDocuments = listeDocuments;
		notifyDataSetChanged();
	}
}
