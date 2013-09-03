/**
 * Claroline Mobile - Android
 * 
 * @package     util
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package util;

import net.claroline.mobile.android.R;

import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Update;

/**
 * Claroline Mobile - Android
 * 
 * Utility class.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public final class Tools {

	public static void cleanTableAfterUpdate(final Class<? extends Model> table) {
		new Delete().from(table).where("Updated = ?", false).execute();
		new Update(table).set("Updated = ?", false).execute();
	}

	/**
	 * Cast values from Enumeration to String array.
	 * 
	 * @param <T>
	 *            the type of the Enumeration
	 * @param values
	 *            the values to convert
	 * @return the translated array
	 */
	public static <T extends Enum<T>> String[] enumValuesToStrings(
			final T[] values) {
		String[] array = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			array[i] = values[i].name();
		}
		return array;
	}

	public static String getFragmentTag(final int pos) {
		return "android:switcher:" + R.id.pager + ":" + pos;
	}

	/**
	 * @param extension
	 *            the extension to request
	 * @return the id of drawable for requested extension icon
	 */
	public static int getResFromExt(final String extension) {
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

	/**
	 * Private consturctor for Utility Class.
	 */
	private Tools() {

	}
}
