package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by james_huker on 8/6/17.
 *
 * 调整CrimeLab使用数据库储存数据。
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private static List<Crime> mCrimes;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // create a single CrimeLab
    public static CrimeLab get (Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        // 插入数据库数据。
        mDatabase.insert(CrimeTable.NAME , null , values);
    }
    // 当用户修改指定的Crime实例后，调用updateCrime可以更新数据库的信息。
    public void updateCrime(Crime crime , int updateOrDelete) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        if(updateOrDelete == 0) {
            mDatabase.update(CrimeTable.NAME , values
                , CrimeTable.Cols.UUID + "= ?"
                , new String[] {uuidString});
        } else {
            mDatabase.delete(CrimeTable.NAME
                    , CrimeTable.Cols.UUID + "= ?"
                    , new String[] {uuidString});
        }
    }

    public static void deleteCrime(Crime crime) {
       mCrimes.remove(crime.getId());
    }

    // 私有构造器
    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        // 写入数据库方法。
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        // 创建List数组为了保存Crime对象。
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null , null) ;

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        mCrimes = crimes;
        return crimes;
    }

    // 此处的方法为了返回指定ID 的Crime对象。
    // 采用了遍历的方式进行查找指定Id的Crime。
    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + "=?"
                , new String[] {id.toString()});
        try {
            if (cursor.getCount() ==0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID , crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE , crime.getTitle());
        values.put(CrimeTable.Cols.DATE , crime.getDate().getTime());
        // nice one choice a SOLVED values.
        values.put(CrimeTable.Cols.SOLVED , crime.isSolved() ? 1 : 0);

        return values;
    }

    // 读取数据库的方法
    private CrimeCursorWrapper queryCrimes(String whereClause , String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,   // Colums - null selects all columns
                whereClause,
                whereArgs,
                null,   // groupBy
                null,   // having
                null    // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }
}
