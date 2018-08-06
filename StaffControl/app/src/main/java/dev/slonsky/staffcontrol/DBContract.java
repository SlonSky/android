package dev.slonsky.staffcontrol;

import android.provider.BaseColumns;

/**
 * Created by Slon on 11.08.2017.
 */

public final class DBContract {
    public DBContract() {}

    public static abstract class Employee implements BaseColumns {
        public static final String TABLE_NAME = "employee";

        public static final String COLUMN_NAME_EMPLOYEE_ID = "_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_POSITION = "position";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

    public static abstract class Shift implements BaseColumns {
        public static final String TABLE_NAME = "shift";

        public static final String COLUMN_NAME_SHIFT_ID = "shift_id";
        public static final String COLUMN_NAME_EMPLOYEE_ID = "employee_id";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_START_PHOTO_URI = "start_photo_uri";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_END_PHOTO_URI = "end_photo_uri";
        public static final String COLUMN_NAME_DURATION = "duration";
    }

    public static abstract class Rest implements BaseColumns {
        public static final String TABLE_NAME = "rest";

        public static final String COLUMN_NAME_REST_ID = "_id";
        public static final String COLUMN_NAME_SHIFT_ID = "shift_id";
        public static final String COLUMN_NAME_START_TIME = "start_time";
        public static final String COLUMN_NAME_END_TIME = "end_time";
        public static final String COLUMN_NAME_DURATION = "duration";
    }
}
