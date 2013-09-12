/**
 * Claroline Mobile - Android
 * 
 * @package     connectivity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package connectivity;

import java.util.Arrays;

import model.Annonce;
import model.Document;
import model.ModelBase;
import model.ResourceModel;
import util.Tools;

/**
 * Claroline Mobile - Android
 * 
 * TODO Description here.
 * 
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public enum SupportedModules {
	/**
	 * Announcements plugin.
	 */
	CLANN,
	/**
	 * Calendar plugin.
	 */
	CLCAL,
	/**
	 * Documents plugin.
	 */
	CLDOC,
	/**
	 * Descriptions plugin.
	 */
	CLDSC,
	/**
	 * Forum plugin.
	 */
	CLFRM,
	/**
	 * Not in MOBILE module.
	 */
	NOMOD,
	/**
	 * Not plugin specific.
	 */
	USER;

	/**
	 * @param label
	 *            the module label (equal to one of values of this enum)
	 * @return the Class corresponding to the module, if any. ResourceModel
	 *         class otherwise
	 */
	public static Class<? extends ModelBase> getTypeForModule(final String label) {
		if (Arrays.asList(Tools.enumValuesToStrings(SupportedModules.values()))
				.contains(label)) {
			switch (SupportedModules.valueOf(label)) {
			case CLANN:
				return Annonce.class;
			case CLDOC:
				return Document.class;
			default:
				return ResourceModel.class;
			}
		} else {
			return ResourceModel.class;
		}
	}
}
