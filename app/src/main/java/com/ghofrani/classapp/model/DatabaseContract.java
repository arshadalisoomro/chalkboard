package com.ghofrani.classapp.model;

import android.provider.BaseColumns;

public final class DatabaseContract {

    public static abstract class Sunday implements BaseColumns {

        public static final String TABLE_NAME = "sunday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class Monday implements BaseColumns {

        public static final String TABLE_NAME = "monday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class Tuesday implements BaseColumns {

        public static final String TABLE_NAME = "tuesday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class Wednesday implements BaseColumns {

        public static final String TABLE_NAME = "wednesday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class Thursday implements BaseColumns {

        public static final String TABLE_NAME = "thursday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class Friday implements BaseColumns {

        public static final String TABLE_NAME = "friday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class Saturday implements BaseColumns {

        public static final String TABLE_NAME = "saturday";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_CLASS = "class";
        public static final String COLUMN_START_TIME = "classstarttime";
        public static final String COLUMN_END_TIME = "classendtime";

    }

    public static abstract class ClassInfo implements BaseColumns {

        public static final String TABLE_NAME = "classinfo";
        public static final String COLUMN_ID = "classid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TEACHER = "teacher";
        public static final String COLUMN_LOCATION = "location";

    }

}