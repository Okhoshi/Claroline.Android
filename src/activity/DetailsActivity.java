/**
 * Claroline Mobile - Android
 * 
 * @package     activity
 * 
 * @author      Devos Quentin (q.devos@student.uclouvain.be)
 * @version     1.0
 *
 * @license     ##LICENSE##
 * @copyright   2013 - Devos Quentin
 */
package activity;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

import model.Document;
import net.claroline.mobile.android.R;

import org.joda.time.DateTime;

import util.Tools;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import app.AppActivity;

import com.activeandroid.query.Select;
import com.activeandroid.util.Log;
import com.loopj.android.http.AsyncHttpResponseHandler;

import connectivity.SupportedModules;
import fragments.AnnonceDetailFragment;
import fragments.DetailFragment;
import fragments.GenericDetailFragment;

/**
 * Claroline Mobile - Android
 * 
 * Details activity.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
public class DetailsActivity extends AppActivity {

	/**
	 * @param label
	 *            the requested module label
	 * @return the fragment class name for the label module
	 */
	private static String getDetailFragmentName(final String label) {
		if (Arrays.asList(Tools.enumValuesToStrings(SupportedModules.values()))
				.contains(label)) {
			switch (SupportedModules.valueOf(label)) {
			case CLANN:
				return AnnonceDetailFragment.class.getName();
			default:
				return GenericDetailFragment.class.getName();
			}
		} else {
			return GenericDetailFragment.class.getName();
		}
	}

	/**
	 * Content fragment.
	 */
	private DetailFragment mFragment;
	/**
	 * Handler for the {@link Document} download with token.
	 */
	private final AsyncHttpResponseHandler mTokenizedURLHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onSuccess(final String content) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(content));
			try {
				startActivity(i);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(DetailsActivity.this,
						getString(R.string.app_not_found), Toast.LENGTH_LONG)
						.show();
			}
		}
	};

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			Bundle args = new Bundle(extras);

			String label = extras.getString("label");

			if ("CLDOC".equals(label)) {
				Document mCurrentResource = new Select().from(Document.class)
						.where("Id = ?", extras.get("resID")).executeSingle();
				openFileInMemory(mCurrentResource);
				finish();
			} else {
				mFragment = (DetailFragment) Fragment.instantiate(this,
						getDetailFragmentName(label), args);
				getSupportFragmentManager()
						.beginTransaction()
						.add(android.R.id.content, mFragment, "resource_detail")
						.commit();
			}
		} else {
			finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			// Comportement du bouton "Rafraichir"
			if (mFragment.isExpired()) {
				setProgressIndicator(true);
				mFragment.refreshResource(new AsyncHttpResponseHandler() {
					@Override
					public void onFinish() {
						setProgressIndicator(false);
					}

					@Override
					public void onSuccess(final String content) {
						refreshUI();
						updatesNow();
					}
				});
			} else {
				getService().getUpdates(new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(final String content) {
						if (!content.equals("[]")) {
							refreshUI();
						}
						updatesNow();
					}
				});
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Opens the document if it is on storage, else downloads it before open it.
	 * 
	 * @param item
	 *            the document to open
	 */
	public void openFileInMemory(final Document item) {
		MimeTypeMap map = MimeTypeMap.getSingleton();
		final String mime = map.getMimeTypeFromExtension(item.getExtension());

		if (mime != null) {
			if (item.isOnMemory()
					&& Environment.MEDIA_MOUNTED == Environment
							.getExternalStorageState()
					|| Environment.MEDIA_MOUNTED_READ_ONLY == Environment
							.getExternalStorageState()) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStoragePublicDirectory(
								Environment.DIRECTORY_DOWNLOADS)
						.getAbsolutePath()
						+ "/"
						+ getString(R.string.app_name)
						+ "/"
						+ item.getList().getCours().getOfficialCode()
						+ "/"
						+ item.getTitle() + "." + item.getExtension())), mime
						.toLowerCase(Locale.US));
				startActivity(Intent.createChooser(i,
						getString(R.string.dialog_choose_app)));
			} else if (Environment.MEDIA_MOUNTED == Environment
					.getExternalStorageState()) {
				getService().getDownloadTokenizedUrl(
						item.getList().getCours().getSysCode(),
						item.getResourceString(),
						new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(final Throwable error,
									final String content) {
								Log.e(error.getLocalizedMessage() + " : "
										+ content);
								super.onFailure(error, content);
							}

							@TargetApi(Build.VERSION_CODES.HONEYCOMB)
							@Override
							public void onSuccess(final String content) {
								Request request = new Request(Uri
										.parse(content));

								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
									request.allowScanningByMediaScanner();
									request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
								}
								request.setDestinationInExternalPublicDir(
										Environment
												.getExternalStoragePublicDirectory(
														Environment.DIRECTORY_DOWNLOADS)
												.getAbsolutePath()
												+ "/"
												+ getString(R.string.app_name)
												+ "/"
												+ item.getList().getCours()
														.getOfficialCode()
												+ "/", item.getTitle() + "."
												+ item.getExtension());

								final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
								final long id = manager.enqueue(request);

								BroadcastReceiver bcr = new BroadcastReceiver() {
									@Override
									public void onReceive(
											final Context context,
											final Intent intent) {
										String action = intent.getAction();
										if (DownloadManager.ACTION_DOWNLOAD_COMPLETE
												.equals(action)) {
											Bundle extras = intent.getExtras();
											if (id == extras
													.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)) {
												openFileInMemory(item);
												unregisterReceiver(this);
											}
										}
									}
								};

								registerReceiver(
										bcr,
										new IntentFilter(
												DownloadManager.ACTION_DOWNLOAD_COMPLETE));
								item.setLoadedDate(DateTime.now());
							}
						});
			} else {
				Toast.makeText(this, R.string.error_sdcard, Toast.LENGTH_SHORT)
						.show();
			}

		} else {
			DetailsActivity.this.getService().getDownloadTokenizedUrl(
					item.getList().getCours().getSysCode(),
					item.getResourceString(), mTokenizedURLHandler);
		}
	}

	@Override
	public void refreshUI() {
		mFragment.refreshUI();
	}
}
