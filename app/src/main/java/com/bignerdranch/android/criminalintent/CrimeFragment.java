package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by james_huker on 8/5/17.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String ARG_CRIME_ID = "crime_id";
    private static final int REQUEST_DATE = 0;
    private static int ud = 0;

    // 编写的一个可以给fragment添加Bundle对象的，并返回fragment方法。
    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID , crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 接受方法回调请求
        setHasOptionsMenu(true);

        // 获取一个crime对象。
        // 从Fragment中获取argument然后在获取里面的，UUID数据。
        UUID crimeId = (UUID) this.getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = (CrimeLab.get(getActivity())   // 获取一个CrimeLab对象，然后调用它的getCrime方法。
                    .getCrime(crimeId));
    }

    //  当用户离开CrimeFragment后，由系统调用此方法，实现更新数据库文件的作用。
    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime , ud);
        ud =0;
    }

    // hold up ID int resurce
    @Override
    public void onCreateOptionsMenu(Menu menu , MenuInflater inflater) {
        super.onCreateOptionsMenu(menu , inflater);
        inflater.inflate(R.menu.fragment_crime_delete , menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_crime_delete:
                CrimeLab mCrimeLab = CrimeLab.get(getActivity());
                CrimeLab.deleteCrime(mCrimeLab.getCrime(mCrime.getId()));
                ud = 1;
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container
            , Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime , container ,false);

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        // 获取包含在crime对象里的标题，并把它显示在Title里。
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start ,int count , int after
            ) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start , int before , int count
            ) {
                // Crime 中的getTitle方法
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 绑定按钮键
        mDateButton = (Button)v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                // 绑定两个fragment，DatePickerFragment , and target Crimefragment.
                dialog.setTargetFragment(CrimeFragment.this , REQUEST_DATE);
                // DIALOG_DATE 是 FragmentManager 用来识别 DatePickerFragment唯一符号！
                dialog.show(manager , DIALOG_DATE);
            }
        });

        // 绑定solved的Id值
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        // 检查包含的Crime里的isSolved值。
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        // 创建监听器，点击后改变isSolved得值。
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView , boolean isChecked) {
                // Set the Crime's solved property
                mCrime.setSolved(isChecked);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent intentDate) {
        if (resultCode != Activity.RESULT_OK) {     //
            return ;
        }

        if(requestCode == REQUEST_DATE ){   //
            Date date = (Date) intentDate.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
