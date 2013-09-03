/**
 * author : Quentin
 */
package connectivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import model.Cours;
import model.ModelBase;
import model.ResourceList;
import model.ResourceModel;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Tools;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import app.App;

import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author Quentin
 * 
 */
public class ClarolineService {

	/**
	 * Claroline Mobile - Android
	 * 
	 * Exception in case of Web Response different from OK.
	 * 
	 * @author Devos Quentin
	 * @version 1.0
	 */
	class NotOKResponseCode extends IOException {

		/**
		 * Serializable UID.
		 */
		private static final long serialVersionUID = 4367959832676790410L;

		/**
		 * Fires a new {@link NotOKResponseCode} with the message.
		 * 
		 * @param message
		 *            the message to associate
		 */
		public NotOKResponseCode(final String message) {
			super(message);
		}
	}

	/**
	 * 
	 */
	private static final int C100 = 100;

	/**
	 * Debug tag.
	 */
	private static final String TAG = "ClarolineService";

	/**
	 * {@link ClarolineClient}.
	 */
	private ClarolineClient mClient;

	/**
	 * Specific handler for the profile picture.
	 */
	private AsyncHttpResponseHandler mProfilePictureAsyncHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(final String response) {

			byte[] b = response.getBytes();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bm = BitmapFactory.decodeByteArray(b, 0, b.length);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, C100, baos);

			App.getPrefs()
					.edit()
					.putString(
							App.SETTINGS_USER_IMAGE,
							Base64.encodeToString(baos.toByteArray(),
									Base64.DEFAULT)).apply();
		};
	};

	/**
	 * Internal counter.
	 */
	private int mCounter = 0;

	/**
	 * Default constructor without arguments.
	 */
	public ClarolineService() {
		mClient = ClarolineClient.getInstance();
	}

	public void getCourseList(final AsyncHttpResponseHandler handler) {
		RequestParams p = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getCourseList);
		mClient.serviceQuery(p, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(final JSONArray array) {
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject item = array.getJSONObject(i);
						Cours c = App.getGSON().fromJson(item.toString(),
								Cours.class);
						c.setUpdated(true);
						c.setLoadedDate(DateTime.now());
						c.save();

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Tools.cleanTableAfterUpdate(Cours.class);
				handler.onSuccess(array.toString());
			}
		});
	}

	public void getResourcesForList(final ResourceList list,
			final AsyncHttpResponseHandler handler) {
		RequestParams rp = ClarolineClient
				.getRequestParams(list.getLabel(),
						SupportedMethods.getResourcesList, list.getCours()
								.getSysCode());
		if (ResourceModel.class.equals(list.getResourceType())) {
			rp.add("forceGeneric", "1");
		}

		mClient.serviceQuery(rp, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(final JSONArray array) {
				Log.i(TAG, array.toString());
				for (int i = 0; i < array.length(); i++) {
					try {
						ModelBase mb = App.getGSON()
								.fromJson(array.get(i).toString(),
										list.getResourceType());
						mb.setList(list);
						mb.setLoadedDate(DateTime.now());
						mb.setUpdated(true);
						mb.save();
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (JsonSyntaxException e) {
						android.os.Debug.waitForDebugger();
						e.printStackTrace();
					}
				}
				Tools.cleanTableAfterUpdate(list.getResourceType());
				handler.onSuccess(array.toString());
			}
		});
	}

	public void getSingleResource(final Cours cours, final ResourceList list,
			final String resourceIdentifier,
			final AsyncHttpResponseHandler handler) {
		RequestParams p = ClarolineClient.getRequestParams(
				Enum.valueOf(SupportedModules.class, list.getLabel()),
				SupportedMethods.getSingleResource, cours.getSysCode(),
				resourceIdentifier);
		mClient.serviceQuery(p, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(final JSONObject response) {
				ModelBase mb = App.getGSON().fromJson(response.toString(),
						list.getResourceType());
				mb.setLoadedDate(DateTime.now());
				mb.save();

				handler.onSuccess(response.toString());
			}
		});
	}

	public void getToolListForCours(final Cours cours,
			final AsyncHttpResponseHandler handler) {
		RequestParams rp = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getToolList,
				cours.getSysCode());
		mClient.serviceQuery(rp, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(final JSONArray response) {
				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject item = response.getJSONObject(i);
						ResourceList rl = App.getGSON().fromJson(
								item.toString(), ResourceList.class);
						rl.setCours(cours);
						rl.setLoadedDate(DateTime.now());
						rl.setUpdated(true);
						rl.setResourceType(SupportedModules.getTypeForModule(rl
								.getLabel()));
						rl.save();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Tools.cleanTableAfterUpdate(ResourceList.class);
				handler.onSuccess(response.toString());
			}
		});
	}

	/**
	 * Gets the last updates.
	 * 
	 * @param handler
	 *            the handler to execute if the request is successful
	 */
	public void getUpdates(final AsyncHttpResponseHandler handler) {
		RequestParams p = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getUpdates);
		mClient.serviceQuery(p, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(final Throwable e, final JSONArray array) {
				System.out.println("FAILURE ! :" + e.getLocalizedMessage());
			}

			@Override
			public void onSuccess(final JSONArray array) {
				if (array.length() > 0) {
					System.out.println("Text");
					// TODO Write the Update logic
				}
				handler.onSuccess(array.toString());
			}
		});
	}

	/**
	 * Gets the data about the current user.
	 * 
	 * @param handler
	 *            the handler to execute if the request is successful
	 */
	public void getUserData(final AsyncHttpResponseHandler handler) {
		RequestParams p = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getUserData);
		mClient.serviceQuery(p, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(final JSONObject jsonUser) {
				String previousPicture = App.getPrefs().getString(
						App.SETTINGS_PICTURE, "");
				App.getPrefs()
						.edit()
						.putString(App.SETTINGS_FIRST_NAME,
								jsonUser.optString(App.SETTINGS_FIRST_NAME))
						.putString(App.SETTINGS_LAST_NAME,
								jsonUser.optString(App.SETTINGS_LAST_NAME))
						.putBoolean(
								App.SETTINGS_IS_PLATFORM_ADMIN,
								jsonUser.optBoolean(App.SETTINGS_IS_PLATFORM_ADMIN))
						.putString(
								App.SETTINGS_OFFICIAL_CODE,
								String.valueOf(jsonUser
										.optInt(App.SETTINGS_OFFICIAL_CODE)))
						.putString(App.SETTINGS_PLATFORM_NAME,
								jsonUser.optString(App.SETTINGS_PLATFORM_NAME))
						.putString(
								App.SETTINGS_INSTITUTION_NAME,
								jsonUser.optString(App.SETTINGS_INSTITUTION_NAME))
						.putString(App.SETTINGS_PICTURE,
								jsonUser.optString(App.SETTINGS_PICTURE))
						.apply();

				String imgUrl = jsonUser.optString(App.SETTINGS_PICTURE, "");
				if (!imgUrl.equals("") && !imgUrl.equals(previousPicture)) {
					mClient.get(imgUrl, mProfilePictureAsyncHandler);
				}
				handler.onSuccess(jsonUser.toString());
			}
		});
	}

	public void updateCompleteCourse(final AsyncHttpResponseHandler handler,
			final Cours cours) {
		getToolListForCours(cours, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(final String response) {
				synchronized (cours) {
					mCounter = 0;
					for (ResourceList list : cours.lists()) {
						getResourcesForList(list,
								new AsyncHttpResponseHandler() {
									@Override
									public void onSuccess(final String response) {
										synchronized (cours) {
											mCounter++;
											cours.notify();
										}
									}
								});
					}
					try {
						while (mCounter < cours.lists().size()) {
							cours.wait();
						}
						mCounter = 0;

					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						handler.onSuccess(response);
					}
				}
			}
		});
	}
}