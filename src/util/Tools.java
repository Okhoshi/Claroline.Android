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

import java.util.Arrays;

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

	/**
	 * Deletes the remanent entries in db.
	 * 
	 * @param table
	 *            the table class to clean
	 * @param cond
	 *            the condition to apply
	 * @param args
	 *            the arguments of the cond
	 */
	public static void cleanTableAfterUpdate(
			final Class<? extends Model> table, final String cond,
			final Object... args) {
		new Delete().from(table).where("Updated = 0 AND " + cond, args)
				.execute();
		new Update(table).set("Updated = ?", false).where(cond, args).execute();
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

	/**
	 * @param pos
	 *            the position of requested Fragment in ViewPager
	 * @return the tag of requested fragment for the FragmentManager
	 */
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
		} else if (Arrays.asList(
				new String[] { "gz", "bz2", "zip", "tar", "rar" }).contains(
				extension)) {
			img = R.drawable.package_x_generic;
		} else if (extension.equals("pgp")) {
			img = R.drawable.text_x_pgp;
		} else if (Arrays.asList(
				new String[] { "url", "htm", "html", "htx", "swf" }).contains(
				extension)) {
			img = R.drawable.link;
		} else if (Arrays.asList(new String[] { "sh", "exe" }).contains(
				extension)) {
			img = R.drawable.applications_system;
		} else if (Arrays.asList(
				new String[] { "js", "css", "xsl", "pl", "plm", "ml", "lsp",
						"cls" }).contains(extension)) {
			img = R.drawable.text_x_script;
		} else if (extension.equals("php")) {
			img = R.drawable.application_x_php;
		} else if (extension.equals("py")) {
			img = R.drawable.text_x_python;
		} else if (extension.equals("rb")) {
			img = R.drawable.application_x_ruby;
		} else if (Arrays.asList(new String[] { "c", "h", "cpp", "java" })
				.contains(extension)) {
			img = R.drawable.text_x_code;
		} else if (Arrays.asList(new String[] { "xml", "tex", "txt", "rtf" })
				.contains(extension)) {
			img = R.drawable.text_x_generic;
		} else if (extension.equals("pdf")) {
			img = R.drawable.pdf;
		} else if (extension.equals("ps")) {
			img = R.drawable.x_office_document;
		} else if (Arrays
				.asList(new String[] { "ogg", "wav", "midi", "mp2", "mp3",
						"mp4", "vqf" }).contains(extension)) {
			img = R.drawable.audio_x_generic;
		} else if (Arrays.asList(
				new String[] { "avi", "mpg", "mpeg", "mov", "wmv" }).contains(
				extension)) {
			img = R.drawable.video_x_generic;
		} else if (Arrays.asList(
				new String[] { "png", "jpeg", "jpg", "xcf", "gif", "bmp" })
				.contains(extension)) {
			img = R.drawable.image_x_generic;
		} else if (Arrays.asList(new String[] { "svg", "odg" }).contains(
				extension)) {
			img = R.drawable.x_office_drawing;
		} else if (Arrays.asList(
				new String[] { "odt", "doc", "docx", "dot", "mcw", "wps" })
				.contains(extension)) {
			img = R.drawable.x_office_document;
		} else if (Arrays.asList(new String[] { "ods", "xls", "xlsx", "xlt" })
				.contains(extension)) {
			img = R.drawable.x_office_spreadsheet;
		} else if (Arrays.asList(new String[] { "odp", "ppt", "pptx", "pps" })
				.contains(extension)) {
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
