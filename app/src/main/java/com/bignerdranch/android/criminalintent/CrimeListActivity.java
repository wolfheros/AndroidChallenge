package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by james_huker on 8/6/17.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
