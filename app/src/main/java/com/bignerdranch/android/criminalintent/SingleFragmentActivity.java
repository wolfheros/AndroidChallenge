package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by james_huker on 8/6/17.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {
    // 需要继承的抽象方法。
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);     // 注意此处的R.layout.activity_fragment.

        FragmentManager fm =getSupportFragmentManager();    //创建一个兼容的FragmentManager对象。
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);   //注意此处的是R.layout.fragment_container

        // 判断fragment是不是为null。
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container , fragment)
                    .commit();
        }
    }
}
