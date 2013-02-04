package app;

import java.io.File;
import java.util.Locale;

import mobile.claroline.R;
import model.Documents;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import dataStorage.DocumentsRepository;
import fragments.LoginDialog;

public class AppHandler extends Handler {

	public static final int MISSING_NETWORK = 5;
	public static final int INCREMENT_STATUS = 2;
	public static final int SET_PROGRESS_DWL = 1;
	public static final int ASK_OPEN_SAVED = 0;
	public static final int AUTH_FAILED = 3;
	public static final int FAILURE = 4;
	private Context context;
	private int resID;

	public AppHandler() {
		super();
	}

	public AppHandler(Callback callback) {
		super(callback);
	}

	public AppHandler(Looper looper) {
		super(looper);
	}

	public AppHandler(Looper looper, Callback callback) {
		super(looper, callback);
	}
	
	public AppHandler(Context context){
		super();
		this.context = context;
	}
	
	public void handleMessage(final Message mess){
		resID = mess.arg2;
		switch (mess.what){
		case ASK_OPEN_SAVED:
			GlobalApplication.setProgressIndicator(false);
			if(mess.arg1 == 1){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.open_saved_doc_dialog)
				.setMessage((String) mess.obj)
				.setCancelable(true)
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				})
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						final Documents item = DocumentsRepository.GetById(resID);
						
						MimeTypeMap map = MimeTypeMap.getSingleton();
						final String mimeType = map.getMimeTypeFromExtension(item.getExtension().toLowerCase(Locale.US));
						
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" +
										context.getString(R.string.app_name) + "/" + item.getName() + "." + item.getExtension())), mimeType);
						try {
							context.startActivity(Intent.createChooser(i,context.getString(R.string.dialog_choose_app)));
						} catch (ActivityNotFoundException e) {
							Toast.makeText(context, context.getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
						}
						dialog.dismiss();
					}
				})
				.show();
			}
			break;
		case SET_PROGRESS_DWL: //Set the progress with downloading informations
			GlobalApplication.setProgressIndicator(null, true, (String) mess.obj, false, mess.arg1/mess.arg2, "%1d/%2d Ko");
			break;
		case INCREMENT_STATUS: //Renew the progress status
			GlobalApplication.incrementProgression(mess.arg1/mess.arg2);
			break;
		case AUTH_FAILED:
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.alert_auth_failed)
			.setMessage((Integer) mess.obj)
			.setCancelable(true)
			.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			})
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
					LoginDialog login = new LoginDialog(context);
					login.show();
				}
			})
			.show();
			break;
		case FAILURE:
			GlobalApplication.setProgressIndicator(false);
			AlertDialog.Builder f_alert = new AlertDialog.Builder(context);
			f_alert.setMessage((String) mess.obj)
			.setCancelable(true)
			.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
		case MISSING_NETWORK: //Missing network
			GlobalApplication.setProgressIndicator(false);
			AlertDialog.Builder alert = new AlertDialog.Builder(context);
			alert.setTitle(R.string.no_network_title)
			.setMessage(R.string.no_network)
			.setCancelable(true)
			.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.show();
			break;
		default:
			break;
		}
	}

}
