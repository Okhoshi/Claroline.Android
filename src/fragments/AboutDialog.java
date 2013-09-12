/**
 * Claroline Mobile - Android
 * 
 * @package     com.sopura.consignmentstock.companion.android.fragments
 * 
 * @author      Devos Quentin
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin - Sopura
 */
package fragments;

import net.claroline.mobile.android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Claroline Mobile - Android
 * 
 * Written with Loic Fortemps for a project at the UCL.
 * 
 * @author Devos Quentin
 * @version 1.0
 */
public class AboutDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(final Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		// we keep the view to update UI
		View inflator = inflater.inflate(R.layout.dialog_about, null);

		// we change the default view and we implement a simple close button
		builder.setView(inflator).setPositiveButton("Close",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(final DialogInterface dialog,
							final int which) {
						dialog.dismiss();
					}
				});

		return builder.create();
	}
}
