package com.bignerdranch.android.criminalintent.database;

/**
 * Created by james_huker on 8/30/17.
 */

public class CrimeDbSchema {
    // 定义数据表内部类。
    public static final class CrimeTable{
        public static final String NAME = "crimes";
        // 内嵌的数据表字段。内部类
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
