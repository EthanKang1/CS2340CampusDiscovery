package com.example.campusdiscovery.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.campusdiscovery.R;
import com.example.campusdiscovery.interfaces.EventListListener;
import com.example.campusdiscovery.interfaces.UpdateListener;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter{

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;
    private Bundle extras;
    private UpdateListener updateListener;
    private FragmentManager fm;

    // instances
    private Fragment allEventsFragment;
    private Fragment userEventsFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm, Bundle extras) {
        super(fm);
        this.fm = fm;
        mContext = context;
        this.extras = extras;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                allEventsFragment = AllEventsFragment.newInstance(this.extras);
                return allEventsFragment;
            case 1: // Fragment # 0 - This will show FirstFragment different title
                userEventsFragment = UserEventsFragment.newInstance(this.extras);
                return userEventsFragment;
            default:
                return new Fragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    // method to make fragments refresh data source
    public void refreshFragments() {
        System.out.println("Adapter freshing");
        this.fm.beginTransaction().detach(this.allEventsFragment).commit();
        this.fm.beginTransaction().attach(this.allEventsFragment).commit();
        this.fm.beginTransaction().detach(this.userEventsFragment).commit();
        this.fm.beginTransaction().attach(this.userEventsFragment).commit();
    }
}