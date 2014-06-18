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

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;

import model.Cours;
import model.ModelBase;
import model.ResourceList;
import model.ResourceModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Tools;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import app.App;

import com.activeandroid.query.Select;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Claroline Mobile - Android
 * 
 * Service providing all the methods callable on the web service of Claroline.
 * 
 * @author Devos Quentin (q.devos@student.uclouvain.be)
 * @version 1.0
 */
public class ClarolineService {

	/**
	 * Numeric Constant.
	 */
	private static final int C100 = 100;
	/**
	 * Regex pattern.
	 */
	private static final Pattern PLATFORM_SETTINGS = Pattern
			.compile("<!-- PLATFORM SETTINGS (.*) PLATFORM SETTINGS -->");

	/**
	 * Debug tag.
	 */
	private static final String TAG = "ClarolineService";

	/**
	 * {@link ClarolineClient}.
	 */
	private ClarolineClient mClient;

	/**
	 * Internal counter.
	 */
	private int mCounter = 0;

	/**
	 * Specific handler for the profile picture.
	 */
	private BinaryHttpResponseHandler mProfilePictureHandler = new BinaryHttpResponseHandler() {
		@Override
		public void onFailure(final Throwable error) {
			Log.e("ClarolineClient", error.getLocalizedMessage());
		};

		@Override
		public void onSuccess(final byte[] binaryData) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bm = BitmapFactory.decodeByteArray(binaryData, 0,
					binaryData.length);
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
	 * Default constructor without arguments.
	 */
	public ClarolineService() {
		mClient = ClarolineClient.getInstance();
	}

	/**
	 * Verify that the current host has a valid version of the module installed.
	 * 
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void checkForModuleValidity(final JsonHttpResponseHandler handler) {
		mClient.siteCall(
				mClient.getUrl(App.getPrefs().getString(
						App.SETTINGS_PLATFORM_MODULE, "/module/MODULE/")),
				handler);
		RequestParams p = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getPlatformData);
		mClient.serviceQuery(p, handler);
	}

	/**
	 * Verify that the passed host is a valid Claroline platform.
	 * 
	 * @param hostURL
	 *            the requested url
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void checkHostValidity(final String hostURL,
			final AsyncHttpResponseHandler handler) {
		mClient.get(hostURL.replace(' ', '\0'), new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(final Throwable error, final String content) {
				if (error.getCause() instanceof SSLException
						&& App.getPrefs().getBoolean(App.SETTINGS_HTTP_NO_SSL,
								false)) {
					checkHostValidity(hostURL.replace("https://", "http://"),
							handler);
				} else {
					handler.onFailure(error, content);
				}
			}

			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final String content) {
				handler.onSuccess(String.valueOf(content
						.contains("<!-- - - - - - - - - - - Claroline "
								+ "Body - - - - - - - - - -->"))
						+ "#" + hostURL);
			}
		});
	}

	/**
	 * Gets the cours list.
	 * 
	 * @param handler
	 *            the handler to execute after the request
	 * 
	 */
	public void getCourseList(final AsyncHttpResponseHandler handler) {
		RequestParams p = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getCourseList);
		mClient.serviceQuery(p, new JsonHttpResponseHandler() {
			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final JSONArray array) {
				for (int i = 0; i < array.length(); i++) {
					try {
						JSONObject item = array.getJSONObject(i);
						Cours c = new Select().from(Cours.class)
								.where("SysCode = ?", item.get("sysCode"))
								.executeSingle();
						if (c == null) {
							c = App.getGSON().fromJson(item.toString(),
									Cours.class);
						} else {
							c.update(item);
						}
						c.setUpdated(true);
						c.setLoadedDate(DateTime.now());
						c.save();

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Tools.cleanTableAfterUpdate(Cours.class, "1=1");
				handler.onSuccess(array.toString());
			}
		});
	}

	/**
	 * Gets the URL tokenized for the requested document. Can be used during 30
	 * secs.
	 * 
	 * @param syscode
	 *            the syscode of the document course
	 * @param resId
	 *            the unique resource identifier of the requested document.
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void getDownloadTokenizedUrl(final String syscode,
			final String resId, final AsyncHttpResponseHandler handler) {
		RequestParams rp = ClarolineClient.getRequestParams(
				SupportedModules.CLDOC, SupportedMethods.getSingleResource,
				syscode, resId);
		mClient.serviceQuery(rp, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(final JSONObject response) {
				if (response.has("token")) {
					try {
						handler.onSuccess(mClient.getUrl(App.getPrefs()
								.getString(App.SETTINGS_PLATFORM_MODULE,
										"/module/MOBILE/")
								+ "download.php?token="
								+ response.getString("token")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Retrieves the page at the specified url.
	 * 
	 * @param url
	 *            the url of the page to get
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void getPageFor(final String url,
			final AsyncHttpResponseHandler handler) {
		mClient.siteCall(url, handler);
	}

	/**
	 * Gets all the resources for a requested Tool.
	 * 
	 * @param list
	 *            the requested Tool
	 * @param handler
	 *            the handler to execute after the request
	 */
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
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final JSONArray array) {
				Log.i(TAG, array.toString());
				for (int i = 0; i < array.length(); i++) {
					try {
						ModelBase mb = new Select()
								.from(list.getResourceType())
								.where("List = ? AND ResourceString = ?",
										array.getJSONObject(i)
												.get("resourceId"))
								.executeSingle();
						if (mb == null) {
							mb = App.getGSON().fromJson(
									array.get(i).toString(),
									list.getResourceType());
						} else {
							mb.update(array.getJSONObject(i));
						}
						mb.setList(list);
						mb.setLoadedDate(DateTime.now());
						mb.setUpdated(true);
						mb.save();
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
				}
				list.setLoadedDate(DateTime.now());
				list.save();

				Tools.cleanTableAfterUpdate(list.getResourceType(), "List = ?",
						list);
				handler.onSuccess(array.toString());
			}
		});
	}

	/**
	 * Gets the content of one specific resource.
	 * 
	 * @param syscode
	 *            the course code
	 * @param label
	 *            the tool label
	 * @param type
	 *            the resource type
	 * @param resourceIdentifier
	 *            the resource unique identifier
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void getSingleResource(final String syscode, final String label,
			final Class<? extends ModelBase> type,
			final String resourceIdentifier,
			final AsyncHttpResponseHandler handler) {
		RequestParams p = ClarolineClient
				.getRequestParams(Enum.valueOf(SupportedModules.class, label),
						SupportedMethods.getSingleResource, syscode,
						resourceIdentifier);
		mClient.serviceQuery(p, new JsonHttpResponseHandler() {

			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final JSONObject response) {
				ModelBase mb;
				try {
					mb = new Select()
							.from(type)
							.where("List = ? AND ResourceString = ?",
									response.get("resourceId")).executeSingle();
					if (mb == null) {
						mb = App.getGSON().fromJson(response.toString(), type);
					} else {
						mb.update(response);
					}
					mb.setLoadedDate(DateTime.now());
					mb.save();

				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				handler.onSuccess(response.toString());
			}
		});
	}

	/**
	 * Gets the tool list for the given course.
	 * 
	 * @param cours
	 *            the course to load
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void getToolListForCours(final Cours cours,
			final AsyncHttpResponseHandler handler) {
		RequestParams rp = ClarolineClient.getRequestParams(
				SupportedModules.USER, SupportedMethods.getToolList,
				cours.getSysCode());
		mClient.serviceQuery(rp, new JsonHttpResponseHandler() {

			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final JSONArray response) {
				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject item = response.getJSONObject(i);
						ResourceList rl = new Select()
								.from(ResourceList.class)
								.where("Cours = ? AND label = ?", cours,
										item.getString("label"))
								.executeSingle();
						if (rl == null) {
							rl = App.getGSON().fromJson(item.toString(),
									ResourceList.class);
							rl.setCours(cours);
							rl.setResourceType(SupportedModules
									.getTypeForModule(rl.getLabel()));
						} else {
							rl.update(item);
						}
						rl.setUpdated(true);
						rl.save();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				cours.setLoadedDate(DateTime.now());
				cours.save();
				Tools.cleanTableAfterUpdate(ResourceList.class, "Cours = ?",
						cours);
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
			public void onFailure(final Throwable e, final String array) {
				Log.e("ClarolineClient",
						"FAILURE ! :" + e.getLocalizedMessage());
			}

			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final JSONArray response) {
				handler.onSuccess(response.toString());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(final JSONObject object) {

				Iterator<String> iterOnCours = object.keys();
				while (iterOnCours.hasNext()) {
					final String syscode = iterOnCours.next();
					final Cours upCours = new Select().from(Cours.class)
							.where("Syscode = ?", syscode).executeSingle();

					if (upCours == null) {
						getCourseList(new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(final String content) {
								Cours cours = new Select().from(Cours.class)
										.where("Syscode = ?", syscode)
										.executeSingle();

								if (cours != null) {
									updateCompleteCourse(cours, null,
											new AsyncHttpResponseHandler());
								}
							}
						});
						continue;
					} else {
						try {
							JSONObject jsonCours = object
									.getJSONObject(syscode);
							Iterator<String> iterOnMod = jsonCours.keys();
							while (iterOnMod.hasNext()) {
								final String modKey = iterOnMod.next();
								ResourceList upList = new Select()
										.from(ResourceList.class)
										.where("Cours = ? AND label = ?",
												upCours, modKey)
										.executeSingle();

								if (upList == null) {
									getToolListForCours(upCours,
											new AsyncHttpResponseHandler() {
												@Override
												public void onSuccess(
														final String content) {
													ResourceList list = new Select()
															.from(ResourceList.class)
															.where("Cours = ? AND label = ?",
																	upCours,
																	modKey)
															.executeSingle();
													if (list != null) {
														getResourcesForList(
																list,
																new AsyncHttpResponseHandler());
													}
												}
											});
									continue;
								} else {
									try {
										JSONObject jsonRes = jsonCours
												.getJSONObject(modKey);
										Iterator<String> iterOnKeys = jsonRes
												.keys();
										while (iterOnKeys.hasNext()) {
											String resourceString = iterOnKeys
													.next();
											ModelBase upRes = new Select()
													.from(SupportedModules
															.getTypeForModule(modKey))
													.where("List = ?", upList)
													.executeSingle();
											if (upRes == null) {
												getResourcesForList(
														upList,
														new AsyncHttpResponseHandler());
											} else {
												DateTime date = DateTime.parse(
														jsonRes.optString(
																resourceString,
																""),
														DateTimeFormat
																.forPattern("yyyy-MM-dd HH:mm:ss"));
												upRes.setDate(date);
												upRes.setNotifiedDate(date);
											}
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				handler.onSuccess(object.toString());
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
			public void onFinish() {
				handler.onFinish();
			}

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
					mClient.get(imgUrl, mProfilePictureHandler);
				}
				handler.onSuccess(jsonUser.toString());
			}
		});
	}

	/**
	 * Updates completely the course, loading first the Tool list, then the
	 * resources for each tool.
	 * 
	 * @param cours
	 *            the course to load
	 * @param activity
	 *            the calling activity
	 * @param handler
	 *            the handler to execute after the request
	 */
	public void updateCompleteCourse(final Cours cours,
			final Activity activity, final AsyncHttpResponseHandler handler) {
		getToolListForCours(cours, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				handler.onFinish();
			}

			@Override
			public void onSuccess(final String response) {
				mCounter = 0;
				for (ResourceList list : cours.lists()) {
					getResourcesForList(list, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(final String response) {
							synchronized (cours) {
								mCounter++;
								cours.notify();
							}
						}
					});
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							synchronized (cours) {
								while (mCounter < cours.lists().size()) {
									cours.wait();
								}
								mCounter = 0;

							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} finally {
							if (activity != null) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										handler.onSuccess(response);
									}
								});
							}
						}
					}
				}).start();
			}
		});
	}
}