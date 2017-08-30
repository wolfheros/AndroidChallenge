package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by james_huker on 8/5/17.
 * 创建Crime实例，并赋予UUID值，和Date日期值，实现整个操作。
 */

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    // mTitle get and set method;
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    // mId get method;
    public UUID getId() {

        return mId;
    }

    // mSolved get and set method;
    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    // mDate get and set method;
    public Date getDate() {

        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
    /**
     * ****************
     * 这是整个程序的开始位置。
     * *****************
     * */
    public Crime () {
        mId = UUID.randomUUID();
        mDate = new Date();
    }
    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }
}
