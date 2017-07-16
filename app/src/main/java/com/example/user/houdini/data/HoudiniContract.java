package com.example.user.houdini.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by USER on 7/16/2017.
 */

public class HoudiniContract {

    private HoudiniContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.user.houdini";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH = "election_info";

    public static final class StateEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        /**
         * Name of database table for movies
         */
        public final static String TABLE_NAME = "states";

        /**
         * Unique ID number for the movies (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;


        /**
         * The MIME type of the {@link #CONTENT_URI} for a single movie.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date_added";
        public static final String COLUMN_ORDER_BY = "order_by";

    }
}


