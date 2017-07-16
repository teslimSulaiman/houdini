package com.example.user.houdini.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by USER on 7/16/2017.
 */

public class HoudiniProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = HoudiniProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the movies table
     */
    private static final int INFOS = 100;

    /**
     * URI matcher code for the content URI for a single movie in the movies table
     */
    private static final int INFO_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        uriMatcher.addURI(HoudiniContract.CONTENT_AUTHORITY, HoudiniContract.PATH, INFOS);

        // The content URI of the form "content://com.example.abu.phones/phones/#" will map to the
        // integer code {@link #PHONES_ID}. This URI is used to provide access to ONE single row
        // of the phones table.
        uriMatcher.addURI(HoudiniContract.CONTENT_AUTHORITY, HoudiniContract.PATH + "/#", INFO_ID);

        return uriMatcher;

    }

    private HoudiniDbHelper houdiniDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        houdiniDbHelper = new HoudiniDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = houdiniDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case INFOS:
                retCursor =  db.query(HoudiniContract.StateEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INFO_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = HoudiniContract.StateEntry._ID + "=?";
                String [] mSelectionArgs = new String []{id};

                retCursor = db.query(HoudiniContract.StateEntry.TABLE_NAME, projection, mSelection, mSelectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = houdiniDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case INFOS:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(HoudiniContract.StateEntry.TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(HoudiniContract.StateEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = houdiniDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int tasksDeleted; // starts as 0

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case INFOS:

                tasksDeleted = db.delete(HoudiniContract.StateEntry.TABLE_NAME,s,strings);

                break;
            case INFO_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(HoudiniContract.StateEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (tasksDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    private int updateTask(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase database = houdiniDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(HoudiniContract.StateEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INFOS:
                return updateTask(uri, values, selection, selectionArgs);
            case INFO_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = HoudiniContract.StateEntry._ID + "=?";
                String [] mSelectionArgs = new String []{id};

                return updateTask(uri, values, mSelection, mSelectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
}
