/**
 * @author Dim
 * @version 1
 * 
 * @description :  An Adapter object acts as a bridge between an AdapterView and the underlying data for that view. 
 * 				   The Adapter provides access to the data items. 
 * 			       The Adapter is also responsible for making a View for each item in the data set. 
 */
package adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import model.Document;
import net.claroline.mobile.android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DocumentsAdapter extends BaseAdapter {

	private int RESOURCE = R.layout.doc_item;
	private List<Document> listeDocuments;
	private Context context;

	public DocumentsAdapter(final Context context,
			final List<Document> listeDocuments) {
		this.listeDocuments = listeDocuments;
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public int getCount() {
		return listeDocuments.size();
	}

	@Override
	public Document getItem(final int position) {
		return listeDocuments.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return listeDocuments.get(position).getId();
	}

	private int getResFromExt(final String extension) {
		int img;
		if (extension.equals("")) {
			img = R.drawable.folder;
		} else if (extension.equals("gz") || extension.equals("bz2")
				|| extension.equals("zip") || extension.equals("tar")
				|| extension.equals("rar")) {
			img = R.drawable.package_x_generic;
		} else if (extension.equals("pgp")) {
			img = R.drawable.text_x_pgp;
		} else if (extension.equals("url") || extension.equals("htm")
				|| extension.equals("html") || extension.equals("htx")
				|| extension.equals("swf")) {
			img = R.drawable.link;
		} else if (extension.equals("sh") || extension.equals("exe")) {
			img = R.drawable.applications_system;
		} else if (extension.equals("js") || extension.equals("css")
				|| extension.equals("xsl") || extension.equals("pl")
				|| extension.equals("plm") || extension.equals("ml")
				|| extension.equals("lsp") || extension.equals("cls")) {
			img = R.drawable.text_x_script;
		} else if (extension.equals("php")) {
			img = R.drawable.application_x_php;
		} else if (extension.equals("py")) {
			img = R.drawable.text_x_python;
		} else if (extension.equals("rb")) {
			img = R.drawable.application_x_ruby;
		} else if (extension.equals("c") || extension.equals("h")
				|| extension.equals("cpp") || extension.equals("java")) {
			img = R.drawable.text_x_code;
		} else if (extension.equals("xml") || extension.equals("tex")
				|| extension.equals("txt") || extension.equals("rtf")) {
			img = R.drawable.text_x_generic;
		} else if (extension.equals("pdf")) {
			img = R.drawable.pdf;
		} else if (extension.equals("ps")) {
			img = R.drawable.x_office_document;
		} else if (extension.equals("ogg") || extension.equals("wav")
				|| extension.equals("midi") || extension.equals("mp2")
				|| extension.equals("mp3") || extension.equals("mp4")
				|| extension.equals("vqf")) {
			img = R.drawable.audio_x_generic;
		} else if (extension.equals("avi") || extension.equals("mpg")
				|| extension.equals("mpeg") || extension.equals("mov")
				|| extension.equals("wmv")) {
			img = R.drawable.video_x_generic;
		} else if (extension.equals("png") || extension.equals("jpeg")
				|| extension.equals("jpg") || extension.equals("xcf")
				|| extension.equals("gif") || extension.equals("bmp")) {
			img = R.drawable.image_x_generic;
		} else if (extension.equals("svg") || extension.equals("odg")) {
			img = R.drawable.x_office_drawing;
		} else if (extension.equals("odt") || extension.equals("doc")
				|| extension.equals("docx") || extension.equals("dot")
				|| extension.equals("mcw") || extension.equals("wps")) {
			img = R.drawable.x_office_document;
		} else if (extension.equals("ods") || extension.equals("xls")
				|| extension.equals("xlsx") || extension.equals("xlt")) {
			img = R.drawable.x_office_spreadsheet;
		} else if (extension.equals("odp") || extension.equals("ppt")
				|| extension.equals("pptx") || extension.equals("pps")) {
			img = R.drawable.x_office_presentation;
		} else if (extension.equals("odf")) {
			img = R.drawable.x_office_formula;
		} else if (extension.equals("ttf")) {
			img = R.drawable.font_x_generic;
		} else {
			img = R.drawable.default_mime;
		}
		return img;
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
			TextView detail_1 = (TextView) v.findViewById(R.id.detail_1);
			TextView detail_2 = (TextView) v.findViewById(R.id.detail_2);
			TextView detail_3 = (TextView) v.findViewById(R.id.detail_3);
			ImageView img = (ImageView) v.findViewById(R.id.ext_logo);
			TextView syscode = (TextView) v.findViewById(R.id.syscode);

			if (name != null) {
				name.setText(doc.getTitle());
			}
			if (detail_1 != null) {
				detail_1.setText(doc.getStringSize());
			}
			if (detail_2 != null) {
				detail_2.setText(new SimpleDateFormat("E MMM y dd").format(doc
						.getDate()));
			}
			if (detail_3 != null) {
				detail_3.setText(doc.getExtension());
			}
			if (img != null) {
				img.setImageResource(getResFromExt(doc.getExtension()));
			}
			if (syscode != null) {
				syscode.setVisibility(View.GONE);
			}
		}

		return v;
	}

	public void setDocuments(final List<Document> listeDocuments) {
		this.listeDocuments = listeDocuments;
	}
}
