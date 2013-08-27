package app;

import java.io.File;
import java.util.Locale;

import model.Document;
import net.claroline.mobile.android.R;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import fragments.LoginDialog;

public class AppHandler extends Handler {

	public static final int MISSING_NETWORK = 5;
	public static final int INCREMENT_STATUS = 2;
	public static final int SET_PROGRESS_DWL = 1;
	public static final int ASK_OPEN_SAVED = 0;
	public static final int AUTH_FAILED = 3;
	public static final int FAILURE = 4;

	private AppActivity mActivity;
	private int resID;

	public AppHandler() {
		super();
	}

	public AppHandler(final AppActivity context) {
		super();
		mActivity = context;
	}

	public AppHandler(final Callback callback) {
		super(callback);
	}

	public AppHandler(final Looper looper) {
		super(looper);
	}

	public AppHandler(final Looper looper, final Callback callback) {
		super(looper, callback);
	}

	@Override
	public void handleMessage(final Message mess) {
		resID = mess.arg2;
		switch (mess.what) {
		case ASK_OPEN_SAVED:
			mActivity.setProgressIndicator(false);
			if (mess.arg1 == 1) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				builder.setTitle(R.string.open_saved_doc_dialog)
						.setMessage((String) mess.obj)
						.setCancelable(true)
						.setNegativeButton(android.R.string.no,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										dialog.dismiss();
									}
								})
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										final Document item = null;// FIXME
																	// Get
																	// the
																	// resID
																	// doc !

										MimeTypeMap map = MimeTypeMap
												.getSingleton();
										final String mimeType = map
												.getMimeTypeFromExtension(item
														.getExtension()
														.toLowerCase(Locale.US));

										Intent i = new Intent(
												Intent.ACTION_VIEW);
										i.setDataAndType(
												Uri.fromFile(new File(
														Environment
																.getExternalStoragePublicDirectory(
																		Environment.DIRECTORY_DOWNLOADS)
																.getAbsolutePath()
																+ "/"
																+ mActivity
																		.getString(R.string.app_name)
																+ "/"
																+ item.getTitle()
																+ "."
																+ item.getExtension())),
												mimeType);
										try {
											mActivity.startActivity(Intent.createChooser(
													i,
													mActivity
															.getString(R.string.dialog_choose_app)));
										} catch (ActivityNotFoundException e) {
											Toast.makeText(
													mActivity,
													mActivity
															.getString(R.string.app_not_found),
													Toast.LENGTH_LONG).show();
										}
										dialog.dismiss();
									}
								}).show();
			}
			break;
		case SET_PROGRESS_DWL: // Set the progress with downloading informations
			mActivity.setProgressIndicator(true, (String) mess.obj, false,
					mess.arg1 / mess.arg2, "%1d/%2d Ko");
			break;
		case INCREMENT_STATUS: // Renew the progress status
			mActivity.incrementProgression(mess.arg1 / mess.arg2);
			break;
		case AUTH_FAILED:
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(R.string.alert_auth_failed)
					.setMessage((Integer) mess.obj)
					.setCancelable(true)
					.setNegativeButton(android.R.string.no,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									dialog.dismiss();
								}
							})
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int id) {
									dialog.dismiss();
									LoginDialog login = new LoginDialog(
											mActivity);
									login.show();
								}
							}).show();
			break;
		case FAILURE:
			mActivity.setProgressIndicator(false);
			AlertDialog.Builder f_alert = new AlertDialog.Builder(mActivity);
			f_alert.setMessage((String) mess.obj)
					.setCancelable(true)
					.setNeutralButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int which) {
									dialog.dismiss();
								}
							}).show();
		case MISSING_NETWORK: // Missing network
			mActivity.setProgressIndicator(false);
			AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);
			alert.setTitle(R.string.no_network_title)
					.setMessage(R.string.no_network)
					.setCancelable(true)
					.setNeutralButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
										final int which) {
									dialog.dismiss();
								}
							}).show();
			break;
		default:
			break;
		}
	}

}
