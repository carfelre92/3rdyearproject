package com.google.sample.cloudvision;

import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
/**
 * Created by SuKim on 15/08/2017.
 */

public class FragmentsClassesPagerAdapter extends FragmentPagerAdapter {

    public FragmentsClassesPagerAdapter(FragmentManager fragmentManager, Context context, List<Class<? extends Fragment>> pages) {
        super(fragmentManager);
        mPagesClasses = pages;
        mContext = context;
    }

    private List<Class<? extends Fragment>> mPagesClasses;
    private Context mContext;

    @Override
    public Fragment getItem(int posiiton) {
        return Fragment.instantiate(mContext, mPagesClasses.get(posiiton).getName());
    }

    @Override
    public int getCount() {
        return mPagesClasses.size();
    }

}
