package dataStorage;

import java.util.HashMap;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Provides access to the cours database.
 */
public class CoursProvider extends ContentProvider {
    String TAG = "CoursProvider";

    public static String AUTHORITY = "mobile.claroline.CoursProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // MIME types used for searching words or looking up a single definition
    public static final String TITLE_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                                                  "/vnd.activity.home";
    public static final String TITULAR_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                                                       "/vnd.activity.home";


    // UriMatcher stuff
    private static final int SEARCH_COURS = 0;
    private static final int GET_COURS = 1;
    private static final int SEARCH_SUGGEST = 2;
    private static final int REFRESH_SHORTCUT = 3;
    private static final UriMatcher sURIMatcher = buildUriMatcher();

    /**
     * Builds up a UriMatcher for search suggestion and shortcut refresh queries.
     */
    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher =  new UriMatcher(UriMatcher.NO_MATCH);
        // to get definitions...
        matcher.addURI(AUTHORITY, "cours", SEARCH_COURS);
        matcher.addURI(AUTHORITY, "cours/#", GET_COURS);
        // to get suggestions...
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);

        /* The following are unused in this implementation, but if we include
         * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our suggestions table, we
         * could expect to receive refresh queries when a shortcutted suggestion is displayed in
         * Quick Search Box, in which case, the following Uris would be provided and we
         * would return a cursor with a single item representing the refreshed suggestion data.
         */
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT, REFRESH_SHORTCUT);
        matcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*", REFRESH_SHORTCUT);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    /**
     * Handles all the cours searches and suggestion queries from the Search Manager.
     * When requesting a specific cours, the uri alone is required.
     * When searching all of the cours for matches, the selectionArgs argument must carry
     * the search query as the first element.
     * All other arguments are ignored.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        // Use the UriMatcher to see what kind of query we have and format the db query accordingly
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                if (selectionArgs == null) {
                  throw new IllegalArgumentException(
                      "selectionArgs must be provided for the Uri: " + uri);
                }
                return getSuggestions(selectionArgs[0]);
            case SEARCH_COURS:
                if (selectionArgs == null) {
                  throw new IllegalArgumentException(
                      "selectionArgs must be provided for the Uri: " + uri);
                }
                return search(selectionArgs[0]);
            case GET_COURS:
                return getCours(uri);
            case REFRESH_SHORTCUT:
                return refreshShortcut(uri);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    private Cursor getSuggestions(String query) {
      query = query.toLowerCase();
      String[] columns = new String[] {
          BaseColumns._ID,
          SearchManager.SUGGEST_COLUMN_TEXT_1,
          SearchManager.SUGGEST_COLUMN_TEXT_2,
       /* SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
                        (only if you want to refresh shortcuts) */
          SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};

      return CoursProvider.getCoursMatches(query, columns);
    }

    private Cursor search(String query) {
      query = query.toLowerCase();
      String[] columns = new String[] {
          BaseColumns._ID,
          SearchManager.SUGGEST_COLUMN_TEXT_1,
          SearchManager.SUGGEST_COLUMN_TEXT_2};

      return CoursProvider.getCoursMatches(query, columns);
    }

    private Cursor getCours(Uri uri) {
      String rowId = uri.getLastPathSegment();
      String[] columns = new String[] {
    		  SearchManager.SUGGEST_COLUMN_TEXT_1,
              SearchManager.SUGGEST_COLUMN_TEXT_2};

      return CoursProvider.getCours(rowId, columns);
    }

    private Cursor refreshShortcut(Uri uri) {
      /* This won't be called with the current implementation, but if we include
       * {@link SearchManager#SUGGEST_COLUMN_SHORTCUT_ID} as a column in our suggestions table, we
       * could expect to receive refresh queries when a shortcutted suggestion is displayed in
       * Quick Search Box. In which case, this method will query the table for the specific
       * word, using the given item Uri and provide all the columns originally provided with the
       * suggestion query.
       */
      String rowId = uri.getLastPathSegment();
      String[] columns = new String[] {
          BaseColumns._ID,
          SearchManager.SUGGEST_COLUMN_TEXT_1,
          SearchManager.SUGGEST_COLUMN_TEXT_2,
          SearchManager.SUGGEST_COLUMN_SHORTCUT_ID,
          SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID};

      return CoursProvider.getCours(rowId, columns);
    }

    /**
     * This method is required in order to query the supported types.
     * It's also useful in our own query() method to determine the type of Uri received.
     */
    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_COURS:
                return TITLE_MIME_TYPE;
            case GET_COURS:
                return TITULAR_MIME_TYPE;
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case REFRESH_SHORTCUT:
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    // Other required implementations...

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
    
    
    private static final HashMap<String,String> mColumnMap = buildColumnMap();
    
    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(DBOpenHelper.COURS_COLUMN_TITLE, DBOpenHelper.COURS_COLUMN_TITLE);
        map.put(DBOpenHelper.COURS_COLUMN_TITULAR, DBOpenHelper.COURS_COLUMN_TITULAR);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }
    
    
    
    private static Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DBOpenHelper.COURS_TABLE);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(Repository.sqLiteOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
    
    
    
    public static Cursor getCours(String rowId, String[] columns) {
        String selection = "rowid = ?";
        String[] selectionArgs = new String[] {rowId};

        return query(selection, selectionArgs, columns);
    }
    
    
    
    public static Cursor getCoursMatches(String query, String[] columns) {
        String selection = DBOpenHelper.COURS_COLUMN_TITLE + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

}
