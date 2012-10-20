package app;

import java.io.File;

import mobile.claroline.R;
import model.Documents;
import dataStorage.DocumentsRepository;
import activity.Settings;
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

public class AppHandler extends Handler {

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
		case 0:
			GlobalApplication.setProgressIndicator(false);
			if(mess.arg1 == 1){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(R.string.open_saved_doc_dialog)
				.setMessage((String) mess.obj)
				.setCancelable(true)
				.setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				})
				.setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						final Documents item = DocumentsRepository.GetById(resID);
						
						MimeTypeMap map = MimeTypeMap.getSingleton();
						final String mimeType = map.getMimeTypeFromExtension(item.getExtension().toLowerCase());
						
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" +
										context.getString(R.string.app_name) + "/" + item.getName() + "." + item.getExtension())), mimeType);
						try {
							context.startActivity(i);
						} catch (ActivityNotFoundException e) {
							Toast.makeText(context, context.getString(R.string.app_not_found), Toast.LENGTH_LONG).show();
						}
						dialog.dismiss();
					}
				})
				.show();
			}
			break;
		case 1: //Set the progress with downloading informations
			GlobalApplication.setProgressIndicator(null, true, (String) mess.obj, false, mess.arg1/mess.arg2, "%1d/%2d Ko");
			break;
		case 2: //Renew the progress status
			GlobalApplication.incrementProgression(mess.arg1/mess.arg2);
			break;
		case 3:
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.alert_auth_failed)
			.setMessage((Integer) mess.obj)
			.setCancelable(true)
			.setNegativeButton(R.string.alert_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			})
			.setPositiveButton(R.string.alert_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Intent settings_intent = new Intent(context, Settings.class);
					context.startActivity(settings_intent);
				}
			})
			.show();
		default:
			break;
		}
	}

}
